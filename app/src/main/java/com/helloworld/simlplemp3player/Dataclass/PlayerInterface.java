package com.helloworld.simlplemp3player.Dataclass;

/**
 * Created by Administrator on 2016/1/4 0004.
 */
public interface PlayerInterface {
    void start(int position);
    void stop();
    void pause();
    void next();
    void last();
    void seekto();
}
