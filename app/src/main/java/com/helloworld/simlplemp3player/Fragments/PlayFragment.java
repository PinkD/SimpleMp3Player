package com.helloworld.simlplemp3player.Fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.helloworld.simlplemp3player.Dataclass.Operations;
import com.helloworld.simlplemp3player.Dataclass.Viewtmp;
import com.helloworld.simlplemp3player.Services.Playsongs;
import com.helloworld.simlplemp3player.R;

/**
 * Created by Administrator on 2015/12/29.
 */
public class PlayFragment extends Fragment implements View.OnClickListener{

    private View view = null;
    private ImageButton last = null;
    private ImageButton play = null;
    private ImageButton next = null;
    private TextView songname_textView = null;
    private TextView singer_textView = null;
    private TextView lrc_last = null;
    private TextView lrc_now = null;
    private TextView lrc_next = null;
    private SeekBar seekBar = null;
    private Intent intent = null;
    private boolean init = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_playing,container, false);
        }
        setView();
        return view;
    }
    private void setView(){
        if (last == null) {
            last = (ImageButton) view.findViewById(R.id.last);
            last.setOnClickListener(this);
        }
        if (play == null) {
            play = (ImageButton) view.findViewById(R.id.play);
            Viewtmp.play_song = play;
            play.setOnClickListener(this);
        }
        if (next == null) {
            next = (ImageButton) view.findViewById(R.id.next);
            next.setOnClickListener(this);
        }
        if (songname_textView == null){
            songname_textView = (TextView) view.findViewById(R.id.playing);
            singer_textView = (TextView) view.findViewById(R.id.play_singer);
            Viewtmp.song_name = songname_textView;
            Viewtmp.singer = singer_textView;
        }
        if (lrc_last == null) {
            lrc_last = (TextView) view.findViewById(R.id.lrc1);
            lrc_now = (TextView) view.findViewById(R.id.lrc2);
            lrc_next = (TextView) view.findViewById(R.id.lrc3);
            Viewtmp.lrc_last = lrc_last;
            Viewtmp.lrc_now = lrc_now;
            Viewtmp.lrc_next = lrc_next;
        }
        if (seekBar == null){
            seekBar = (SeekBar) view.findViewById(R.id.seekbar);
            intent = new Intent(getActivity(), Playsongs.class);
            intent.putExtra("operation", Operations.SEEK);
            Viewtmp.seekBar = seekBar;
            getActivity().startService(intent) ;
        }
        if (init){
            init = false;
            intent = new Intent(getActivity(), Playsongs.class);
            intent.putExtra("operation", Operations.INIT);
            getActivity().startService(intent) ;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last:
                intent = new Intent(getActivity(), Playsongs.class);
                intent.putExtra("operation", Operations.LAST);
                getActivity().startService(intent) ;
                break;
            case R.id.play:
                intent = new Intent(getActivity(), Playsongs.class);
                intent.putExtra("operation", Operations.PAUSE);
                getActivity().startService(intent) ;
                break;
            case R.id.next:
                intent = new Intent(getActivity(), Playsongs.class);
                intent.putExtra("operation", Operations.NEXT);
                getActivity().startService(intent) ;
                break;
        }
    }
}
