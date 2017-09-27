package com.example.choi.gohome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.choi.gohome.MyViewPagerAdapter;
import com.example.choi.gohome.R;

/**
 * Created by choi on 2016-09-20.
 */
public class GuardiansActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager = null;
    private MyViewPagerAdapter adapter;
    private Handler handler = null;
    private Button[] menuBtn = new Button[2];  //0:protector, 1:ward
    private boolean menuFlag = false;
    private int currentPageNum = 0;
    private int direction = 1;      //페이지 전환 방향 - 0:왼쪽, 1:오른쪽

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardians);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);

        menuBtn[0] = (Button) findViewById(R.id.guradian);
        menuBtn[1] = (Button) findViewById(R.id.ward);
        for (Button aMenuBtn : menuBtn) {
            aMenuBtn.setOnClickListener(this);
        }
        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (currentPageNum == 0) {
                    menuBtn[0].performClick();
                    viewPager.setCurrentItem(1);
                    currentPageNum++;
                    direction = 1;
                } else if (currentPageNum == 1 && direction == 0) {
                    menuBtn[1].performClick();
                    viewPager.setCurrentItem(1);
                    currentPageNum--;
                    direction = 0;
                }
            }
        };

        onPageSelected(0);
        viewPager.addOnPageChangeListener(this);
        Intent intent = getIntent();
        menuFlag = intent.getBooleanExtra("ward", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(menuFlag) {
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guradian:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ward:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPageNum = position;
        if(currentPageNum == 0) {
            menuBtn[0].setBackground(getResources().getDrawable(R.drawable.protector_btn));
            menuBtn[1].setBackground(getResources().getDrawable(R.drawable.protector_btn2));
        } else if(currentPageNum == 1) {
            menuBtn[0].setBackground(getResources().getDrawable(R.drawable.ward_btn));
            menuBtn[1].setBackground(getResources().getDrawable(R.drawable.ward_btn2));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
