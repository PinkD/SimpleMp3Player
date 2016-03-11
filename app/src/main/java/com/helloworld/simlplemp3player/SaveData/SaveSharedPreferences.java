package com.helloworld.simlplemp3player.SaveData;

import android.content.Context;
import android.content.SharedPreferences;

import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.R;

/**
 * Created by Administrator on 2016/1/6 0006.
 */
public class SaveSharedPreferences {

    private boolean random_play = false;
    private String user_name = null;
    private String user_pass = null;
//    private Context context = null;
    private SharedPreferences sharedPreferences = null;

    public SaveSharedPreferences(Context context) {
//        this.context = context;
        getData(context);
    }

    private void getData(Context context){

        sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        OtherData.on_play_path = sharedPreferences.getString("on_play_path", null);
        OtherData.on_play_position = sharedPreferences.getInt("on_play_position", -1);
        OtherData.on_play_song = sharedPreferences.getString("on_play_song", context.getText(R.string.nosong).toString());
        OtherData.on_play_singer = sharedPreferences.getString("on_play_singer", null);
        OtherData.random_play = sharedPreferences.getBoolean("random_play", false);

//        user_name = sharedPreferences.getString("user_name", null);
//        user_pass = sharedPreferences.getString("user_pass",null);
    }

    public void saveData() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("on_play_path", OtherData.on_play_path);
        editor.putString("on_play_song", OtherData.on_play_song);
        editor.putString("on_play_singer", OtherData.on_play_singer);
        editor.putInt("on_play_position", OtherData.on_play_position);
//        editor.putString("user_name",user_name);
//        editor.putString("user_name",user_name);
        editor.apply();
    }

//     public boolean getRandom_play(){
//         return OtherData.random_play;
//     }
//    public void setRandom_play(boolean random_play){
//        OtherData.random_play = random_play;
//        }
}
