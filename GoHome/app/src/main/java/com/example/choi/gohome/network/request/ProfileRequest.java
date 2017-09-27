package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-08-26.
 */
public class ProfileRequest {
    private String myPhone;
    private String token;

    public ProfileRequest(String myPhone, String token) {
        this.myPhone = myPhone;
        this.token = token;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
