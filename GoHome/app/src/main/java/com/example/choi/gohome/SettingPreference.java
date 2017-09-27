package com.example.choi.gohome;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by choi on 2016-08-23.
 * Login myPhone(ID), token값 저장
 */
public class SettingPreference {
    private static SharedPreferences pref;

    public SettingPreference(String auth, Activity activity) {
        pref = activity.getSharedPreferences(auth, Activity.MODE_PRIVATE);
    }

    public String getPref(String key) {
        return pref.getString(key, null);
    }

    public void setPref(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean getFlagPref(String key) {
        return pref.getBoolean(key, false);
    }

    public void setFlagPref(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //리스트 길이를 구하는 메서드
    public int getListPref(String key) {
        return pref.getInt(key, 0);
    }

    public void setListPref(String key, ArrayList<String> value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value.size());
        for(int i=0; i<value.size(); i++) {
            editor.putString(key+"_"+i, value.get(i));
        }
        editor.apply();
    }

    public void deletePref(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }
}
