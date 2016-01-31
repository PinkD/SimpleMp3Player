package com.helloworld.simlplemp3player.Fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.helloworld.simlplemp3player.Adapter.DividerItemDecoration;
import com.helloworld.simlplemp3player.Dataclass.Operations;
import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.Services.Playsongs;
import com.helloworld.simlplemp3player.R;
import com.helloworld.simlplemp3player.Adapter.ReAdapter;
import com.helloworld.simlplemp3player.Sort.SideBar;

/**
 * Created by Administrator on 2015/12/29.
 */
public class Allsongsfragment extends Fragment{

    private RecyclerView recyclerView = null;
    private View view = null;
    private ReAdapter myadapter = null;
    private SideBar sideBar = null;



    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            if (view == null) {
                view = inflater.inflate(R.layout.allsongs,container, false);
                initViews();
                setRecyclerViewAdapter();
            }
            OtherData.adapter = myadapter;
            return view;
        }

    private void initViews(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        sideBar = (SideBar) view.findViewById(R.id.sidebar);
        sideBar.setTextView((TextView) view.findViewById(R.id.dialog));
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(char ch) {
                recyclerView.scrollToPosition(myadapter.getLetter_positions(ch));
            }
        });
    }

    private RecyclerView setRecyclerViewAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        myadapter = new ReAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myadapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));//分割线网上找的- -
//        recyclerView.setOnLongClickListener();
        myadapter.setOnItemClickListener(new ReAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setBackground(getResources().getDrawable(R.drawable.pressed));
                Intent intent = new Intent(getActivity(), Playsongs.class);
                intent.putExtra("operation", Operations.START);
                intent.putExtra("position", position);
                getActivity().startService(intent);
            }
        });
        return recyclerView;
    }
}
