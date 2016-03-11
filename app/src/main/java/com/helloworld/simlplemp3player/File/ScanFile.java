package com.helloworld.simlplemp3player.File;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.helloworld.simlplemp3player.Dataclass.Songinfo;
import com.helloworld.simlplemp3player.Dataclass.SortedArrayList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/3 .
 */
public class ScanFile {

    List<String> fileList = new ArrayList<>();
    SortedArrayList songinfos = new SortedArrayList();
    private String name;
    private String singer;
    private String time;
    private int m_time;
    private int  size;
    private String path;

    public ScanFile(Context context,String aim_path) {
        final File[] file = new File(aim_path).listFiles();//设定扫描路径


        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "duration > 100000", null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            m_time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            time = Ms_To_Min(m_time);
            size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//            if (cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) > 100000){
                fileList.add(name);
                songinfos.add(new Songinfo(name, singer, time, size, m_time, path));
//            }
        }
        songinfos.sort();

//        readFile(file);
    }

    public List<Songinfo> getFileList() {
        return songinfos;
    }

    private void readFile(final File[] file) {
        for(int i=0 ; file!= null && i<file.length ;i++) {
            //判读是否文件以及文件后缀名
            if(file[i].isFile() && (file[i].getName().endsWith("mp3")||file[i].getName().endsWith("ape")||file[i].getName().endsWith("flac")||file[i].getName().endsWith("wav"))){
                fileList.add(file[i].toString());
                System.out.println(file[i].toString());
            }
            //如果是文件夹，递归扫描
            else if(file[i].isDirectory()) {
                final File[] newFileList = new File(file[i].getAbsolutePath()).listFiles();
                readFile(newFileList);
                //通过多线程来加速
                /*
                new Thread(new Runnable() {
                    public void run() {
                        readFile(newFileList);
                    }
                }).start();
                */
            }
        }
    }
    private String Ms_To_Min(int m_time){
        int min;
        int sec;
        min = m_time / 60000;
        m_time %= 60000;
        sec = m_time / 1000;
        return min+":"+sec;
    }
}
