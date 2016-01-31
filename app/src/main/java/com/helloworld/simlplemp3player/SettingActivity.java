package com.helloworld.simlplemp3player;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.helloworld.simlplemp3player.About.About;
import com.helloworld.simlplemp3player.About.Attention;
import com.helloworld.simlplemp3player.Dataclass.Operations;
import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.Services.Playsongs;
import com.helloworld.simlplemp3player.Threads.ShutdownCount;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back = null;
    private CheckBox random_play_checkbox = null;
    private TextView attention = null;
    private TextView about = null;
    private TextView shutdown = null;
    private TextView exit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        random_play_checkbox = (CheckBox) findViewById(R.id.is_random);
        random_play_checkbox.setChecked(OtherData.random_play);
        shutdown = (TextView) findViewById(R.id.shutdown);
        shutdown.setOnClickListener(this);
        attention = (TextView) findViewById(R.id.attention_app);
        attention.setOnClickListener(this);
        about = (TextView) findViewById(R.id.about_app);
        about.setOnClickListener(this);
        exit = (TextView) findViewById(R.id.exit_app);
        exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                //保存数据到sharedprefrences
                SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("random_play", random_play_checkbox.isChecked());
                editor.apply();
                OtherData.random_play = random_play_checkbox.isChecked();
                Toast.makeText(this,R.string.savesuccess,Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.shutdown:
                setShutdownTime();
                break;
            case R.id.attention_app:
                startActivity(new Intent(this, Attention.class));
                break;
            case R.id.about_app:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.exit_app:
                MainActivity.THIS.finish();
                finish();
                stopService(new Intent(this, Playsongs.class));
                Operations.EXIT();
                break;
        }
    }

    private void setShutdownTime(){
        Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.shutdown);
        dialog.setContentView(R.layout.shutdown);
        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.shutdown_seekbar);
        TextView textView  = (TextView) dialog.findViewById(R.id.min);
        Button confirm = (Button) dialog.findViewById(R.id.confirm);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button clear = (Button) dialog.findViewById(R.id.clear);
        confirm.setOnClickListener(new ShutdownOnclickListener(dialog,seekBar));
        cancel.setOnClickListener(new ShutdownOnclickListener(dialog,seekBar));
        clear.setOnClickListener(new ShutdownOnclickListener(dialog,seekBar));
        seekBar.setMax(120);
        seekBar.setProgress(OtherData.shutdown_time_left);
        textView.setText(OtherData.shutdown_time_left + "");
        seekBar.setOnSeekBarChangeListener(new ShutdownOnSeekBarChangeListener(textView));
        dialog.show();
    }
    class ShutdownOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        TextView textView = null;

        public ShutdownOnSeekBarChangeListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textView.setText(progress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
    class ShutdownOnclickListener implements View.OnClickListener{

        Dialog dialog = null;
        SeekBar seekBar = null;
        ShutdownCount shutdownCount = null;

        public ShutdownOnclickListener(Dialog dialog, SeekBar seekBar) {
            this.dialog = dialog;
            this.seekBar = seekBar;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.confirm:
                    if ((OtherData.shutdown_time_left = seekBar.getProgress()) != 0) {
                        if (shutdownCount == null || !shutdownCount.isOncounting()) {
                            shutdownCount = new ShutdownCount(SettingActivity.this);
                            shutdownCount.setOncounting(true);
                            shutdownCount.start();
                        } else {
                            shutdownCount.notify();
                        }
                    }
                    Toast.makeText(SettingActivity.this,R.string.savesuccess,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case R.id.cancel:
                    dialog.dismiss();
                    break;
                case R.id.clear:
                    Toast.makeText(SettingActivity.this,R.string.clearsuccess,Toast.LENGTH_SHORT).show();
                    if (shutdownCount != null && shutdownCount.isOncounting()) {
                        shutdownCount.setOncounting(false);
                        shutdownCount.notify();
                    }
                    OtherData.shutdown_time_left = 0;
                    dialog.dismiss();
                    break;
            }
        }
    }
}
