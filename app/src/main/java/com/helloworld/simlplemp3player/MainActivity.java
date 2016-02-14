package com.helloworld.simlplemp3player;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.helloworld.simlplemp3player.About.About;
import com.helloworld.simlplemp3player.About.Attention;
import com.helloworld.simlplemp3player.Adapter.PagAdapter;
import com.helloworld.simlplemp3player.Dataclass.Operations;
import com.helloworld.simlplemp3player.Dataclass.OtherData;
import com.helloworld.simlplemp3player.Fragments.Allsongsfragment;
import com.helloworld.simlplemp3player.Fragments.PlayFragment;
import com.helloworld.simlplemp3player.Services.Playsongs;
import com.helloworld.simlplemp3player.Sort.SideBar;
import com.helloworld.simlplemp3player.Threads.ShutdownCount;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnPageChangeListener,DrawerLayout.DrawerListener{

    //settings------------------------------------------------
    private ImageButton left_more = null;
    private CheckBox random_play_checkbox = null;
    private TextView scan = null;
    private TextView attention = null;
    private TextView about = null;
    private TextView shutdown = null;
    private TextView exit = null;
    //settings------------------------------------------------
    private DrawerLayout drawerLayout = null;

    public static MainActivity THIS = null;
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
        drawerLayout.setDrawerListener(this);
        button_allsong = (Button) findViewById(R.id.allsong);
        button_playing = (Button) findViewById(R.id.playingsong);
        button_allsong.setOnClickListener(this);
        button_playing.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        initViewPager();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagAdapter(fragmentManager, fragments));
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(this);


        //settings------------------------------------------------
        scan = (TextView) findViewById(R.id.scan_file);
        scan.setOnClickListener(this);
        left_more = (ImageButton) findViewById(R.id.left_more);
        left_more.setOnClickListener(this);
        random_play_checkbox = (CheckBox) findViewById(R.id.is_random);
        random_play_checkbox.setChecked(OtherData.random_play);
        shutdown = (TextView) findViewById(R.id.shutdown);
        shutdown.setOnClickListener(this);
        attention = (TextView) findViewById(R.id.attention_app);
        attention.setOnClickListener(this);
        about = (TextView) findViewById(R.id.about_app);
        about.setOnClickListener(this);
        exit = (TextView) findViewById(R.id.exit_app);
        exit.setOnClickListener(this);
        //settings------------------------------------------------
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            //settings------------------------------------------------
            case R.id.scan_file:
                Toast.makeText(MainActivity.this,"此功能暂未实现",Toast.LENGTH_SHORT).show();
                break;
            case R.id.left_more:
                left_more.setBackgroundResource(R.color.gray);
                drawerLayout.openDrawer(GravityCompat.START);
            break;
            case R.id.shutdown:
                setShutdownTime();
                break;
            case R.id.attention_app:
                startActivity(new Intent(this, Attention.class));
                break;
            case R.id.about_app:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.exit_app:
                MainActivity.THIS.finish();
                finish();
                stopService(new Intent(this, Playsongs.class));
                Operations.EXIT();
                break;
            //settings------------------------------------------------

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
                button_allsong.setBackgroundResource(R.color.none);
                button_playing.setBackgroundResource(R.color.background_gray);
                break;
            case 1:
                button_playing.setBackgroundResource(R.color.none);
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
            if (position == 0){
                OtherData.sideBar.finger_up();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    //settings------------------------------------------------
    private void setShutdownTime(){
        Dialog dialog = new Dialog(this);
        dialog.setTitle(R.string.shutdown);
        dialog.setContentView(R.layout.shutdown);
        SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.shutdown_seekbar);
        TextView textView  = (TextView) dialog.findViewById(R.id.min);
        Button confirm = (Button) dialog.findViewById(R.id.confirm);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        Button clear = (Button) dialog.findViewById(R.id.clear);
        confirm.setOnClickListener(new ShutdownOnclickListener(dialog,seekBar));
        cancel.setOnClickListener(new ShutdownOnclickListener(dialog,seekBar));
        clear.setOnClickListener(new ShutdownOnclickListener(dialog,seekBar));
        seekBar.setMax(120);
        seekBar.setProgress(OtherData.shutdown_time_left);
        textView.setText(OtherData.shutdown_time_left + "");
        seekBar.setOnSeekBarChangeListener(new ShutdownOnSeekBarChangeListener(textView));
        dialog.show();
    }


    class ShutdownOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        TextView textView = null;

        public ShutdownOnSeekBarChangeListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textView.setText(progress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
    class ShutdownOnclickListener implements View.OnClickListener{

        Dialog dialog = null;
        SeekBar seekBar = null;
        ShutdownCount shutdownCount = null;

        public ShutdownOnclickListener(Dialog dialog, SeekBar seekBar) {
            this.dialog = dialog;
            this.seekBar = seekBar;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.confirm:
                    if ((OtherData.shutdown_time_left = seekBar.getProgress()) != 0) {
                        if (shutdownCount == null || !shutdownCount.isOncounting()) {
                            shutdownCount = new ShutdownCount(MainActivity.this);
                            shutdownCount.setOncounting(true);
                            shutdownCount.start();
                        } else {
                            shutdownCount.notify();
                        }
                    }
                    Toast.makeText(MainActivity.this,R.string.savesuccess,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case R.id.cancel:
                    dialog.dismiss();
                    break;
                case R.id.clear:
                    Toast.makeText(MainActivity.this,R.string.clearsuccess,Toast.LENGTH_SHORT).show();
                    if (shutdownCount != null && shutdownCount.isOncounting()) {
                        shutdownCount.setOncounting(false);
                        shutdownCount.notify();
                    }
                    OtherData.shutdown_time_left = 0;
                    dialog.dismiss();
                    break;
            }
        }
    }
    //settings------------------------------------------------

//drawer---------------------------------------------------
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        left_more.setBackgroundResource(R.color.gray);

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        left_more.setBackgroundResource(R.color.none);
        //保存数据到sharedprefrences
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("random_play", random_play_checkbox.isChecked());
        editor.apply();
        OtherData.random_play = random_play_checkbox.isChecked();
        Toast.makeText(this, R.string.savesuccess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onBackPressed() {//在drawer打开时按下back不直接退出，而是关闭drawer
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }

//drawer---------------------------------------------------
}
