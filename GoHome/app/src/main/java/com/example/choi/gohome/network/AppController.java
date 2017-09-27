package com.example.choi.gohome.network;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by choi on 2016-08-16.
 */
public class AppController extends Application {
    private static volatile AppController instance = null;
    private static volatile HttpService httpService = null;
    private static final String API_URL = "";   //개인 서버 URL

    public static AppController getInstance() {
        if(instance == null) {
            synchronized (AppController.class) {
                if(instance == null) {
                    instance = new AppController();
                }
            }
        }
        return instance;
    }

    public static HttpService getHttpService() {
        if(httpService == null) {
            synchronized (HttpService.class) {
                if(httpService == null) {
                    httpService = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(HttpService.class);
                }
            }
        }
        return httpService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance();
        AppController.instance = this;
    }
}
