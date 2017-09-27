package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-09-12.
 */
public class GcmTokenList {
    private String phone;
    private String gcm_token;

    public GcmTokenList(String phone, String gcm_token) {
        this.phone = phone;
        this.gcm_token = gcm_token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGcm_token() {
        return gcm_token;
    }

    public void setGcm_token(String gcm_token) {
        this.gcm_token = gcm_token;
    }
}
