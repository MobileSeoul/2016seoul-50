package com.example.choi.gohome.gcmService;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.choi.gohome.SettingPreference;
import com.example.choi.gohome.network.AppController;
import com.example.choi.gohome.network.request.GcmTokenUpdate;
import com.example.choi.gohome.network.request.MsgData;
import com.example.choi.gohome.network.request.Phone;
import com.example.choi.gohome.network.request.SendGcm;
import com.example.choi.gohome.network.response.GcmTokenReceive;
import com.example.choi.gohome.network.response.GcmTokenUpdateResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by choi on 2016-09-12.
 */
public class GcmClass {

    private String gcm_token;
    private SettingPreference authData;

    public void setMyGcmToken(String token, String phone) {
        gcm_token = token;
        GcmTokenUpdate gcmTokenUpdate = new GcmTokenUpdate(gcm_token, phone);
        Call<GcmTokenUpdateResult> gcmTokenUpdateResultCall = AppController.getHttpService().setGcmToken(gcmTokenUpdate);
        gcmTokenUpdateResultCall.enqueue(new Callback<GcmTokenUpdateResult>() {
            @Override
            public void onResponse(Call<GcmTokenUpdateResult> call, Response<GcmTokenUpdateResult> response) {
                GcmTokenUpdateResult gcmTokenUpdateResult = response.body();
                if(gcmTokenUpdateResult.isResult()) {
                    Log.e("setGcmToken", "true");
                } else {
                    Log.e("setGcmToken", "false");
                }
            }

            @Override
            public void onFailure(Call<GcmTokenUpdateResult> call, Throwable t) {
                Log.e("setGcmToken", "server error");
            }
        });
    }

    public void getGcmTokenList(String phone, Context context) {
        authData = new SettingPreference("auth", (Activity) context);
        Phone myPhone = new Phone(phone);
        Call<GcmTokenReceive> gcmTokenReceiveCall = AppController.getHttpService().getGcmTokenList(myPhone);
        gcmTokenReceiveCall.enqueue(new Callback<GcmTokenReceive>() {
            @Override
            public void onResponse(Call<GcmTokenReceive> call, Response<GcmTokenReceive> response) {
                GcmTokenReceive gcmTokenReceive = response.body();
                if(response.isSuccessful()) {
                    if (gcmTokenReceive.isResult()) {
                        for (int i = 0; i < gcmTokenReceive.getGcm_token().size(); i++) {
                            if (gcmTokenReceive.getGcm_token().get(i) != null) {
                                authData.setPref("gcm_token" + i, gcmTokenReceive.getGcm_token().get(i).getGcm_token());
                                Log.e("getGcmTokenList", gcmTokenReceive.getGcm_token().get(i).getGcm_token());
                            }
                        }
//                    authData.setPref("myPhone", phone);
//                    authData.setPref("pw", pw);
//                    authData.setPref("token", loginResponse.getToken());
                    }
                }
            }

            @Override
            public void onFailure(Call<GcmTokenReceive> call, Throwable t) {
                Log.e("getGcmTokenList", "false");
            }
        });
    }

    public void sendGcm(String to, String message) {
        MsgData data = new MsgData(message);
        SendGcm sendGcm = new SendGcm(to, data);

        Call<Void> voidCall = AppController.getHttpService().gcmAuth(sendGcm);
        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("sendGcm", "msg send");
                response.body();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
