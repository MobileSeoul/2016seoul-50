package com.example.choi.gohome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by choi on 2016-10-28.
 */
public class ScoutViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments = new Fragment[3];

    public ScoutViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new FirstPage();
        fragments[1] = new SecondPage();
        fragments[2] = new ThirdPage();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @SuppressLint("ValidFragment")
    public static class FirstPage extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_scout_one, container, false);
            return rootView;
        }
    }

    @SuppressLint("ValidFragment")
    public static class SecondPage extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_scout_two, container, false);
            return rootView;
        }
    }

    @SuppressLint("ValidFragment")
    public static class ThirdPage extends Fragment {
        Button scoutCall;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_scout_three, container, false);
            scoutCall = (Button) rootView.findViewById(R.id.scout_call);

            scoutCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                        getActivity().startActivityForResult(intent, 200);
                        Toast.makeText(getActivity(), "\'전화\'에 대한 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:120"));
                        startActivity(intent);
                    }
                }
            });
            return rootView;
        }
    }
}
