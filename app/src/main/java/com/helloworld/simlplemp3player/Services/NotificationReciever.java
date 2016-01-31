package com.helloworld.simlplemp3player.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class NotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getIntExtra("operation",-1) != -1){
                intent.setClass(context,Playsongs.class);
                context.startService(intent);
        }
    }
}
