package com.example.choi.gohome.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.choi.gohome.R;

/**
 * Created by choi on 2016-09-20.
 */
public class GuideActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            View rootView = inflater.inflate(R.layout.guide_fragment_main, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.bg);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                drawBigImage(imageView, R.drawable.guide_0);
//                imageView.setBackground(getResources().getDrawable(R.drawable.guide_0));  //OOM발생!
            } else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                drawBigImage(imageView, R.drawable.guide_1);
//                imageView.setBackground(getResources().getDrawable(R.drawable.guide_1));
            } else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                drawBigImage(imageView, R.drawable.guide_2);
//                imageView.setBackground(getResources().getDrawable(R.drawable.guide_2));
            } else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                drawBigImage(imageView, R.drawable.guide_3);
//                imageView.setBackground(getResources().getDrawable(R.drawable.guide_3));
            }
            return rootView;
        }

        protected void drawBigImage(ImageView imageView, int resId) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 1;
                options.inPurgeable = true;
                Bitmap src = BitmapFactory.decodeResource(getResources(), resId, options);
                Bitmap resize = Bitmap.createScaledBitmap(src, options.outWidth, options.outHeight, true);
                imageView.setImageBitmap(resize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1";
                case 1:
                    return "2";
                case 2:
                    return "3";
                case 3:
                    return "4";
            }
            return null;
        }
    }
}
