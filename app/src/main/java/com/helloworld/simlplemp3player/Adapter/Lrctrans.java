package com.helloworld.simlplemp3player.Adapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/6 0006.
 */
public class Lrctrans {
    private int min;
    private int sec;
    private int msc;
    private String lrc_path;
    private boolean exsist = false;

    private FileReader getFileReader() {
        try {
            exsist = true;
            return new FileReader(lrc_path);
        } catch (FileNotFoundException e) {
            exsist = false;
            e.printStackTrace();
            return null;
        }
    }

    public void setLrc_path(String song_path) {
        this.lrc_path = path_to_path(song_path);
    }

    public ArrayList<Queue> getLrcQueue() {
        Queue<Integer> lrc_time = new LinkedList<>();
        Queue<String> lrc_str = new LinkedList<>();
        ArrayList<Queue> lrc_queue = new ArrayList<>();
        FileReader fileReader = getFileReader();
        if (exsist){
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tmp;
            String result;
            Pattern pattern = Pattern.compile("\\[\\d{2}\\:\\d{2}\\.\\d{2}\\]");
            try {
                while ((tmp = bufferedReader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(tmp);
                    if (matcher.find()) {
                        String timestr = matcher.group();
                        Integer m_time = getTime(timestr.substring(1,timestr.length() - 1));
                        lrc_time.add(m_time);
                        result = tmp.substring(10);
                        lrc_str.add(result);
                    }
                }
                lrc_queue.add(lrc_time);
                lrc_queue.add(lrc_str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lrc_queue;
        }else {
            return null;
        }
    }
    public int getTime(String time_str) {
        String str1[] = time_str.split("\\:");
        String str2[] = str1[1].split("\\.");
        min = Integer.parseInt(str1[0]);
        sec = Integer.parseInt(str2[0]);
        msc = Integer.parseInt(str2[1]);
        return min*60000+sec*1000+msc*10;
    }
    public String path_to_path(String mp3_path){
        String str[] = mp3_path.split("\\.");
        return str[0] + ".lrc";
    }
}

