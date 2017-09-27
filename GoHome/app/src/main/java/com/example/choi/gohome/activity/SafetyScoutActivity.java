package com.example.choi.gohome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.choi.gohome.R;
import com.example.choi.gohome.ScoutViewPagerAdapter;

/**
 * Created by choi on 2016-09-20.
 */
public class SafetyScoutActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager = null;
    private ScoutViewPagerAdapter adapter;
    private Handler handler = null;
    private Button[] menuBtn = new Button[3];
    private boolean menuFlag = false;
    private int currentPageNum = 0;
    private int direction = 1;      //페이지 전환 방향 - 0:왼쪽, 1:오른쪽

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_scout);

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

        viewPager = (ViewPager)findViewById(R.id.container);
        adapter = new ScoutViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);

        menuBtn[0] = (Button) findViewById(R.id.page1);
        menuBtn[1] = (Button) findViewById(R.id.page2);
        menuBtn[2] = (Button) findViewById(R.id.page3);
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
                } else if (currentPageNum == 1 && direction == 1) {
                    menuBtn[1].performClick();
                    viewPager.setCurrentItem(2);
                    currentPageNum++;
                } else if (currentPageNum == 2) {
                    menuBtn[2].performClick();
                    viewPager.setCurrentItem(1);
                    currentPageNum--;
                    direction = 0;
                }
            }
        };

        onPageSelected(0);
        viewPager.addOnPageChangeListener(this);
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
            menuBtn[0].setBackground(getResources().getDrawable(R.drawable.scout_page_btn2));
            menuBtn[1].setBackground(getResources().getDrawable(R.drawable.scout_page_btn));
            menuBtn[2].setBackground(getResources().getDrawable(R.drawable.scout_page_btn));
        } else if(currentPageNum == 1) {
            menuBtn[0].setBackground(getResources().getDrawable(R.drawable.scout_page_btn));
            menuBtn[1].setBackground(getResources().getDrawable(R.drawable.scout_page_btn2));
            menuBtn[2].setBackground(getResources().getDrawable(R.drawable.scout_page_btn));
        } else if(currentPageNum == 2) {
            menuBtn[0].setBackground(getResources().getDrawable(R.drawable.scout_page_btn));
            menuBtn[1].setBackground(getResources().getDrawable(R.drawable.scout_page_btn));
            menuBtn[2].setBackground(getResources().getDrawable(R.drawable.scout_page_btn2));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_scout);

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
         * The fragment argument representing the section number for this
         * fragment.
         *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
         * Returns a new instance of this fragment for the given section
         * number.
         *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_safety_scout, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    *//**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *//*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }*/
}
