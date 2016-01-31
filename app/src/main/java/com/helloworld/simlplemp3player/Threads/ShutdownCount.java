package com.helloworld.simlplemp3player.Threads;

import android.content.Context;

import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.R;

/**
 * Created by Administrator on 2016/1/7 0007.
 *
 */
public class ShutdownCount extends Thread{
    private Context context = null;
    private boolean oncounting = false;

    public void setOncounting(boolean oncounting) {
        this.oncounting = oncounting;
    }

    public ShutdownCount(Context context) {
        this.context = context;
    }

    public boolean isOncounting() {
        return oncounting;
    }

    @Override
    public void run() {

            while (OtherData.shutdown_time_left > 0 && oncounting){
                synchronized (this){
                    try {
                        wait(60000);
                        OtherData.shutdown_time_left -= 1;
                        System.out.println(R.string.changed+"------------");
                    } catch (InterruptedException e) {
//                        Toast.makeText(context, R.string.changed,Toast.LENGTH_SHORT).show();
                        System.out.println(R.string.changed);
                    }
                }
            }
        if (oncounting){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
