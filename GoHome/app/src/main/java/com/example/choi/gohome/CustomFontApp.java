package com.example.choi.gohome;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by choi on 2016-10-08.
 */
public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addBold(Typekit.createFromAsset(this, "NotoSansKR-Bold-Hestia.otf"));
    }
}
