package com.helloworld.simlplemp3player.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.helloworld.simlplemp3player.Dataclass.Operations;
import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.Dataclass.Viewtmp;
import com.helloworld.simlplemp3player.MainActivity;
import com.helloworld.simlplemp3player.R;
import com.helloworld.simlplemp3player.Services.NotificationReciever;

/**
 * Created by Administrator on 2016/1/9 0009.
 */
public class PlayNotification {

    Notification playNotification = null;
    Context context = null;
    Notification.Builder builder = null;
    RemoteViews remoteViews;

    public PlayNotification(Context context) {
        this.context = context;
        initNoti();
    }

    public Notification getPlayNotification() {
        return playNotification;
    }

    private void initNoti() {//初始化参数基本上都会改
        //Notification.FLAG_NO_CLEAR;
//        if (Viewtmp.song_name == null)return;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_play);
        remoteViews.setTextViewText(R.id.noti_songname, Viewtmp.song_name.getText());
        remoteViews.setTextViewText(R.id.noti_singer, Viewtmp.singer.getText());
        remoteViews.setImageViewResource(R.id.noti_icon, R.mipmap.cover);
        remoteViews.setImageViewResource(R.id.noti_play, R.mipmap.play);
        remoteViews.setImageViewResource(R.id.noti_next, R.mipmap.next);
        remoteViews.setImageViewResource(R.id.noti_close, R.mipmap.close);
//        PendingIntent.getBroadcast(context,R.string.app_name,listenerIntent,0);

        ComponentName componentName = new ComponentName(context, NotificationReciever.class);

        Intent play = new Intent();
        play.putExtra("operation", Operations.PAUSE);
        play.setComponent(componentName);
        PendingIntent playPendIntent = PendingIntent.getBroadcast(context, Operations.PAUSE,
                play, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_play, playPendIntent);
        Intent next_song = new Intent();
        next_song.putExtra("operation", Operations.NEXT);
        next_song.setComponent(componentName);
        PendingIntent nextPendIntent = PendingIntent.getBroadcast(context, Operations.NEXT,
                next_song, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, nextPendIntent);
        Intent close = new Intent();
        close.putExtra("operation", Operations.CLOSE_NOTIFICATION);
        close.setComponent(componentName);
        PendingIntent closePendIntent = PendingIntent.getBroadcast(context, Operations.CLOSE_NOTIFICATION,
                close, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_close, closePendIntent);
        builder = new Notification.Builder(context);
        builder.setContent(remoteViews)
                .setSmallIcon(R.mipmap.play)
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis());
        playNotification = builder.build();
        playNotification.flags = Notification.FLAG_NO_CLEAR;
        Intent intent = new Intent(context, MainActivity.class);
        playNotification.contentIntent = PendingIntent.getActivity(context, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void refreshNotification() {
        playNotification.contentView.setTextViewText(R.id.noti_songname, OtherData.on_play_song);
        playNotification.contentView.setTextViewText(R.id.noti_singer, OtherData.on_play_singer);
        remoteViews.setImageViewResource(R.id.noti_play, OtherData.play_status);
        playNotification.contentView.setImageViewResource(R.id.noti_icon, R.mipmap.cover);//OtherData.on_play_icon == null?R.mipmap.play:OtherData.on_play_icon;

    }

}
