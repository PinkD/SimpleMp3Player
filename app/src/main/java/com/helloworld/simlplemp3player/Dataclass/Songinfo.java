package com.helloworld.simlplemp3player.Dataclass;

import com.helloworld.simlplemp3player.Sort.CharParser;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/3 0003.
 */
public class Songinfo implements Serializable{

    private String path;
    private String name;
    private String singer;
    private String time;
    private String fisrt_char = null;
    private int size;
    private int m_time;

    public Songinfo(String name, String singer, String time, int size, int m_time,String path) {
        this.m_time = m_time;
        this.name = name;
        this.singer = singer;
        this.size = size;
        this.time = time;
        this.path = path;
        this.fisrt_char = string_to_char(name);
    }

    public String getFisrt_char() {
        return fisrt_char;
    }

    private String string_to_char(String name){
        String first = CharParser.getSpelling(name).substring(0, 1);
        if (first.equals("#")){
            return "#";
        }else return first.toUpperCase();
    }

    public String getHQ() {
        if (size/m_time > 50)
        return "HQ";
        else return "LQ";
    }

    public String getPath() {
        return path;
    }

    public int getM_time() {
        return m_time;
    }


    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getTime() {
        return time;
    }

}
