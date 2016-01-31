package com.helloworld.simlplemp3player;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.helloworld.simlplemp3player.Adapter.PagAdapter;
import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.Fragments.Allsongsfragment;
import com.helloworld.simlplemp3player.Fragments.PlayFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnPageChangeListener{

    public static MainActivity THIS = null;
    private ImageButton setting = null;
    private Button button_allsong = null;
    private Button button_playing = null;
    private Fragment allsongs = null;
    private Fragment playing = null;
    private ArrayList<Fragment> fragments;
    FragmentManager fragmentManager = null;

//    private ArrayList<View> views = null;
    private ViewPager viewPager = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        THIS = this;
        setting = (ImageButton) findViewById(R.id.setting);
        button_allsong = (Button) findViewById(R.id.allsong);
        button_playing = (Button) findViewById(R.id.playingsong);
        setting.setOnClickListener(this);
        button_allsong.setOnClickListener(this);
        button_playing.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        initViewPager();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagAdapter(fragmentManager, fragments));
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.allsong:
                if (viewPager.getCurrentItem() != 0){
                    viewPager.setCurrentItem(0);
                }
                setColor(0);
                break;
            case R.id.playingsong:
                if (viewPager.getCurrentItem() != 1){
                    viewPager.setCurrentItem(1);
                }
                setColor(1);
                break;

        }
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        playing = new PlayFragment();
        setColor(1);
        allsongs = new Allsongsfragment();
        fragments.add(allsongs);
        fragments.add(playing);
        OtherData.context = this;
    }

    private void setColor(int position){
        switch (position){
            case 0:
                button_allsong.setBackgroundColor(getResources().getColor(R.color.none));
                button_playing.setBackgroundColor(getResources().getColor(R.color.background_gray));
                break;
            case 1:
                button_playing.setBackgroundColor(getResources().getColor(R.color.none));
                button_allsong.setBackgroundColor(getResources().getColor(R.color.background_gray));
                break;
        }
    }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }


    @Override
        public void onPageSelected(int position) {
            setColor(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
}
