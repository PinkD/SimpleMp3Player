package com.helloworld.simlplemp3player.Dataclass;

/**
 * Created by Administrator on 2016/1/4 0004.
 */
public class Operations {
    public final static int NO_ACTION = 0;
    public final static int START = 1;
    public final static int PAUSE = 2;
    public final static int STOP = 3;
    public final static int NEXT = 4;
    public final static int LAST = 5;
    public final static int SEEK = 6;
    public final static int TEXTVIEW = 7;
    public final static int INIT = 8;
    public final static int CLOSE_NOTIFICATION = 9;

    public final static void EXIT(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }
//本来还以为要杀程序才能干掉，后来发现普通的处理就可以了//发现有后台缓存程序，我还是杀了吧


}
