package com.example.choi.gohome.gcmService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.choi.gohome.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by choi on 2016-09-10.
 */
public class RegistrationIntentService extends IntentService {

    private String token = null;

    public RegistrationIntentService() {
        super("MyInstanceIDService");
        Log.e("MyInstanceIDService", "RegistrationIntentService()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //TODO server로 token 전송(token으로 각각의 디바이스 구분) -> 이벤트가 발생했을때 google서버(gcm서버)로 토큰과 메시지를 push하면 해당 gcm은 해당하는 폰에 알람 동작
            Log.e("MyInstanceIDService", "token : " + token);
            String phone = intent.getStringExtra("phone");
            GcmClass gcmClass = new GcmClass();
            gcmClass.setMyGcmToken(token, phone);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MyInstanceIDService", "토큰 생성 실패", e);
        }
    }
}
