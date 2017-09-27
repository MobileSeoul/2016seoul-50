package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-11.
 */
public class UpdatePW {

    private String myPhone, now_pw, new_pw, token;

    public UpdatePW(String myPhone, String now_pw, String new_pw, String token) {
        this.myPhone = myPhone;
        this.now_pw = now_pw;
        this.new_pw = new_pw;
        this.token = token;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getNow_pw() {
        return now_pw;
    }

    public void setNow_pw(String now_pw) {
        this.now_pw = now_pw;
    }

    public String getNew_pw() {
        return new_pw;
    }

    public void setNew_pw(String new_pw) {
        this.new_pw = new_pw;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
