package com.helloworld.simlplemp3player.Dataclass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.helloworld.simlplemp3player.Sort.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/4 0004.
 */
public class OtherData {
    public final static String SD_CARD0 = "/storage/sdcard0";
    public final static String SD_CARD1 = "/storage/sdcard1";
    // 26个字母
    public final static String[] FIRST_CHAR = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    public static boolean random_play = false;
    public static List<Songinfo> songinfos = new ArrayList<>();
    public static int shutdown_time_left = 0;

    public static RecyclerView.Adapter adapter = null;

    public static String on_play_song = null;
    public static String on_play_singer = null;
    public static String on_play_path = null;
    public static int on_play_position = 0;

    public static int last_song_position = 0;
    public static int play_status = 0;

    public static Context context = null;

    public static SideBar sideBar = null;

}
