package com.helloworld.simlplemp3player.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.R;


/**
 * Created by Administrator on 2015/12/15.
 */
public class ReAdapter extends RecyclerView.Adapter<ReAdapter.ViewHolder> implements View.OnClickListener{

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
//    private List<Songinfo> data = OtherData.songinfos;//妈了个蛋，数据还没读完就绑定完了- -，绑了个空数据上去，怪不得怎么是空的，直接绑源数据吧- -
    private int letter_positions[] = new int[27];

    @Override
    public ReAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View main_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songinfo,null);
        main_view.setOnClickListener(this);
        initLetterPosition();
        return new ViewHolder(main_view);
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public int getLetter_positions(char ch) {
        if (ch - 35 == 0) {
            return letter_positions[26];
        } else {
            return letter_positions[ch - 65];
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.songname.setText(OtherData.songinfos.get(position).getName());//holder.songname.setText(data.get(position).getname());
        holder.singer.setText(OtherData.songinfos.get(position).getSinger());
        holder.time.setText(OtherData.songinfos.get(position).getTime());
        holder.HQ.setText(OtherData.songinfos.get(position).getHQ());
        holder.icon.setImageResource(R.mipmap.play);
        if (OtherData.on_play_position == position) {
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }
        if (holder.HQ.getText().equals("HQ")) holder.HQ.setTextColor(Color.rgb(0, 155, 0));
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }
    @Override
    public int getItemCount() {
        return OtherData.songinfos.size();
    }

    private void initLetterPosition() {
        char ch = 'A';
        int ch_position = 0;
        for (int i = 0, size = OtherData.songinfos.size(); i < size; i++) {
            if (OtherData.songinfos.get(i).getFisrt_char().charAt(0) != ch && ch_position < 27) {
                ch = OtherData.songinfos.get(i).getFisrt_char().charAt(0);
                letter_positions[ch_position] = i;
                ch_position++;
            }
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView songname;
        public TextView singer;
        private TextView time;
        private TextView HQ;
        private ImageView icon;
        public ViewHolder(View view){
            super(view);
            songname = (TextView) view.findViewById(R.id.songname);
            singer = (TextView) view.findViewById(R.id.singer);
            time = (TextView) view.findViewById(R.id.time);
            HQ = (TextView) view.findViewById(R.id.HQ);
            icon = (ImageView) view.findViewById(R.id.on_play_icon);
        }
    }
}
