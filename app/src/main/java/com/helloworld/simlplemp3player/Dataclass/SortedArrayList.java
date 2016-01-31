package com.helloworld.simlplemp3player.Dataclass;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/30 0030.
 *
 */
public class SortedArrayList extends ArrayList<Songinfo>{
    public void sort() {
        ArrayList<Songinfo> songinfos = new ArrayList();
        for (int i = 0; i < 27; i++) {
            for (int j = 0,array_size = size(); j < array_size; j++) {
                if (get(j).getFisrt_char().equals(OtherData.FIRST_CHAR[i])) {
                    songinfos.add(get(j));
                }
            }
        }

        for (int i = 0,array_size = songinfos.size(); i < array_size; i++) set(i, songinfos.get(i));
    }
}
