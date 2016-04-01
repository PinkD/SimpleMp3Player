package com.helloworld.simlplemp3player.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.helloworld.simlplemp3player.Adapter.Lrctrans;
import com.helloworld.simlplemp3player.Dataclass.Operations;
import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.Dataclass.Songinfo;
import com.helloworld.simlplemp3player.Dataclass.Viewtmp;
import com.helloworld.simlplemp3player.Dataclass.PlayerInterface;
import com.helloworld.simlplemp3player.File.ScanFile;
import com.helloworld.simlplemp3player.MainActivity;
import com.helloworld.simlplemp3player.Notification.PlayNotification;
import com.helloworld.simlplemp3player.R;
import com.helloworld.simlplemp3player.SaveData.SaveSharedPreferences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Playsongs extends Service implements PlayerInterface,MediaPlayer.OnCompletionListener {

    private final static int LRC_TIME = 0;
    private final static int LRC_STR = 1;
    private boolean isplaying = false;
    private boolean isreleasing = true;
    private boolean saved = true;
    private boolean isChanging = false;//互斥变量，防止定时器与SeekBar拖动时进度冲突
    private boolean random_play = false;
    private int operation = Operations.NO_ACTION;
    private boolean first = true;
    private SeekBar seekBar = null;
    private TextView songname_textView = null;
    private TextView singer_textView = null;
    private TimerTask timerTask = null;
    private Timer timer = new Timer();
    private List<Songinfo> songinfos;
    private Random random = null;
    private Notification notification = null;
    private PlayNotification playNotification = null;
    private NotificationManager notificationManager = null;
    private SaveSharedPreferences saveSharedPreferences = null;
    private int last_song = OtherData.last_song_position;
    private MediaPlayer mediaPlayer = null;

    private TextView lrc_last = null;
    private TextView lrc_now = null;
    private TextView lrc_next = null;
    private Lrctrans lrctrans = null;
    private ArrayList<Queue> lrc_queue = null;
    private Queue<String> lrc_str_queue = null;
    private Queue<Integer> lrc_time_queue = null;
    private Handler handler = null;
    private int lrc_time_next = 0;
    private int duration = 0;
    private String lrc_last_tmp;
    private String lrc_now_tmp;
    private String lrc_next_tmp;

    @Override
    @SuppressWarnings("unchecked")
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {//防止后台service莫名其妙空指针
            getRandom_play();
            operation = intent.getIntExtra("operation", 0);
            if (operation == Operations.NO_ACTION) {

            } else if (operation == Operations.START) {
                int position = intent.getIntExtra("position", -1);
                start(position);
            } else if (operation == Operations.PAUSE) {
                pause();
            } else if (operation == Operations.STOP) {
                stop();
            } else if (operation == Operations.NEXT) {
                next();
            } else if (operation == Operations.LAST) {
                last();
            } else if (operation == Operations.SEEK) {
                seekto();
            } else if (operation == Operations.INIT) {
                if (!first) {
                    songname_textView = Viewtmp.song_name;
                    singer_textView = Viewtmp.singer;
                    lrc_last = Viewtmp.lrc_last;
                    lrc_now = Viewtmp.lrc_now;
                    lrc_next = Viewtmp.lrc_next;
                    songname_textView.setText(OtherData.on_play_song);
                    singer_textView.setText(OtherData.on_play_singer);
                    System.out.println(OtherData.on_play_song);
                    System.out.println(songname_textView.getText());
                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    Viewtmp.play_song.setImageResource(OtherData.play_status);
                    lrc_last.setText(lrc_last_tmp);
                    lrc_now.setText(lrc_now_tmp);
                    lrc_next.setText(lrc_next_tmp);
                }
            } else if (operation == Operations.CLOSE_NOTIFICATION) {
                notificationManager.cancel(R.string.app_name);
                MainActivity.THIS.finish();
                this.stopSelf();
                Operations.EXIT();
            } else {
                throw new IllegalArgumentException();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        notificationManager.cancel(R.string.app_name);
        saveSharedPreferences.saveData();
        super.onDestroy();
    }

    @Override
    public void onCreate() {

        ScanFile scanFile = new ScanFile(this, OtherData.SD_CARD1);
        System.out.println("判断是否中存在外置SD卡" + new File(OtherData.SD_CARD1).exists());//判断是否中存在外置SD卡
        songinfos = scanFile.getFileList();
        OtherData.songinfos = songinfos;
        saveSharedPreferences = new SaveSharedPreferences(this);//早读早轻松
        songname_textView = Viewtmp.song_name;
        singer_textView = Viewtmp.singer;
        lrc_last = Viewtmp.lrc_last;
        lrc_now = Viewtmp.lrc_now;
        lrc_next = Viewtmp.lrc_next;
//        lrc_now.setText(getText(R.string.nosong).toString());
        saved = (OtherData.on_play_path != null);
        if (songname_textView == null) android.os.Process.killProcess(android.os.Process.myPid());//第一次杀不掉进程，到这儿就崩，再杀一次
        songname_textView.setText(OtherData.on_play_song);
        singer_textView.setText(OtherData.on_play_singer);
        playNotification = new PlayNotification(this);
        notification = playNotification.getPlayNotification();
        lrctrans = new Lrctrans();
        handler = new Handler();
        start_lrc();
        startForeground(R.string.app_name, notification);
        notificationManager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(R.string.app_name, notification);



        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createPlayer(){
        if (OtherData.on_play_path == null){
            mediaPlayer = new MediaPlayer();
        }else {
            mediaPlayer = MediaPlayer.create(Playsongs.this, Uri.parse(OtherData.on_play_path));
        }
        updateOnPlayData(OtherData.on_play_song, OtherData.on_play_singer, OtherData.on_play_path, OtherData.on_play_position);
        isreleasing = false;
        mediaPlayer.setOnCompletionListener(this);

    }
    @Override
    public void start(int position) {
        if (!isreleasing) {
            mediaPlayer.reset();
        }
        if (first) {
            createPlayer();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(songinfos.get(position).getPath());
                mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(this, R.string.file_not_found, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            isreleasing = false;
            mediaPlayer.setOnCompletionListener(this);
            first = false;
            timerTask = new MusicTimerTask();
            timer.schedule(timerTask, 0, 200);

        } else {
            try {
                mediaPlayer.setDataSource(songinfos.get(position).getPath());
                mediaPlayer.prepare();
            } catch (IOException e) {
                Toast.makeText(this, R.string.file_not_found, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        songname_textView.setText(songinfos.get(position).getName());
        singer_textView.setText(songinfos.get(position).getSinger());
        seekBar.setMax(songinfos.get(position).getM_time() - 50);
        mediaPlayer.start();
        updateOnPlayData(songinfos.get(position).getName(), songinfos.get(position).getSinger(), songinfos.get(position).getPath(), position);
        setPlayStatus(R.mipmap.pause);
        playNotification.refreshNotification();
        saveSharedPreferences.saveData();
        OtherData.adapter.notifyDataSetChanged();
        start_lrc();
        isreleasing = false;
        isplaying = true;
    }

    private void start_lrc() {
        if (OtherData.on_play_path != null) {
            lrctrans.setLrc_path(OtherData.on_play_path);
            lrc_queue = lrctrans.getLrcQueue();
            if (lrc_queue == null) {
                lrc_last.setText(null);
                lrc_now.setText(getText(R.string.lrc_not_found));
                lrc_next.setText(null);
            } else {
                lrc_time_queue = lrc_queue.get(LRC_TIME);
                lrc_str_queue = lrc_queue.get(LRC_STR);
                lrc_last.setText(null);
                lrc_now.setText(lrc_str_queue.poll());
                lrc_time_queue.poll();
                lrc_next.setText(lrc_str_queue.poll());
                lrc_time_queue.poll();
            }
        }
    }


    @Override
    public void stop() {
    }

    @Override
    public void pause() {
            if (isplaying) {
                mediaPlayer.pause();
                setPlayStatus(R.mipmap.play);
                isplaying = false;
            } else {
                if (saved||!first) {
                if (first) {
                    createPlayer();
                    first = false;
                    seekBar.setMax(songinfos.get(OtherData.on_play_position).getM_time() - 50);
                    timerTask = new MusicTimerTask();
                    timer.schedule(timerTask, 0, 200);
                }
                if (isreleasing) {
                    try {
                        mediaPlayer.setDataSource(OtherData.on_play_path);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mediaPlayer.start();
                setPlayStatus(R.mipmap.pause);
                isreleasing = false;
                isplaying = true;
            }
        }
    }

    @Override
    public void next() {
        if (songinfos.size() == 0) {
            Toast.makeText(this, R.string.no_song, Toast.LENGTH_SHORT).show();
            return;
        }
        int next_song;
        int random_number;
        if (random_play) {
            random_number = new Random(System.currentTimeMillis()).nextInt() % songinfos.size();
            if (random_number < 0) {
                random_number *= -1;
            }
            if (OtherData.on_play_position + random_number >= songinfos.size()) {
                next_song = OtherData.on_play_position + random_number - songinfos.size();
            } else {
                next_song = OtherData.on_play_position + random_number;
            }
        } else {
            if (OtherData.on_play_position + 1 >= songinfos.size()) {
                next_song = OtherData.on_play_position + 1 - songinfos.size();
            } else {
                next_song = OtherData.on_play_position + 1;
            }
        }
        Viewtmp.play_song.setImageResource(R.mipmap.pause);
        OtherData.last_song_position = OtherData.on_play_position;
        OtherData.adapter.notifyDataSetChanged();
        start(next_song);
    }

    @Override
    public void last() {
        if (songinfos.size() == 0) {
            Toast.makeText(this, R.string.no_song, Toast.LENGTH_SHORT).show();
            return;
        }
        int last_song;
        int random_number;
        if (random_play) {
            if (this.last_song != -1) {
                last_song = this.last_song;
                this.last_song = -1;
            } else {
                random_number = random.nextInt() % songinfos.size();
                if (random_number < 0) {
                    random_number *= -1;
                }
                if (OtherData.on_play_position - random_number < 0) {
                    last_song = OtherData.on_play_position - random_number + songinfos.size();
                } else {
                    last_song = OtherData.on_play_position - random_number;
                }
            }

        } else {
            if (OtherData.on_play_position - 1 < 0) {
                last_song = OtherData.on_play_position - 1 + songinfos.size();
            } else {
                last_song = OtherData.on_play_position - 1;
            }
        }
        Viewtmp.play_song.setImageResource(R.mipmap.pause);
        start(last_song);
    }

    @Override
    public void seekto() {
        seekBar = Viewtmp.seekBar;
        seekBar.setOnSeekBarChangeListener(new MySeekbar());
    }

    class MySeekbar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (OtherData.on_play_path != null) {
                if (first) {
                    createPlayer();
                    seekBar.setMax(mediaPlayer.getDuration() - 50);
                    timerTask = new MusicTimerTask();
                    timer.schedule(timerTask, 0, 200);
                    first = false;
                    isreleasing = false;
                }
                isChanging = true;
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (OtherData.on_play_path == null) {
                seekBar.setProgress(0);
            }else {
                mediaPlayer.seekTo(seekBar.getProgress());
                isChanging = false;
            }
        }

    }

    class MusicTimerTask extends TimerTask {
        @Override
        public void run() {
            if (isChanging) {
                return;
            }
            if (!isreleasing) {
                handler.post(refreshlrc);
            }

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        seekBar.setProgress(0);
        mediaPlayer.reset();
        setPlayStatus(R.mipmap.play);
        next();
    }

    private void getRandom_play() {
        random_play =  OtherData.random_play;
    }

    private void updateOnPlayData(String on_play_song, String on_play_singer, String on_play_path, int on_play_position) {
        OtherData.on_play_song = on_play_song;
        OtherData.on_play_singer = on_play_singer;
        OtherData.on_play_path = on_play_path;
        OtherData.on_play_position = on_play_position;
    }

    private void setPlayStatus(int id) {
        OtherData.play_status = id;
        Viewtmp.play_song.setImageResource(OtherData.play_status);
        playNotification.refreshNotification();
        notificationManager.notify(R.string.app_name, notification);
    }

    Runnable refreshlrc = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            System.out.println(lrc_time_next);
//            System.out.println(mediaPlayer.getCurrentPosition());
            if (lrc_time_queue != null && lrc_time_queue.peek() != null && lrc_time_next < mediaPlayer.getCurrentPosition()) {
                lrc_last_tmp = lrc_now.getText().toString();
                lrc_now_tmp = lrc_next.getText().toString();
                lrc_last.setText(lrc_now.getText());
                lrc_now.setText(lrc_next.getText());
                lrc_next.setText(lrc_str_queue.poll());
                lrc_time_next = lrc_time_queue.poll();
                lrc_next_tmp = lrc_next.getText().toString();
            }
        }
    };

}