package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-08-16.
 */
public class LoginRequest {
    private String myPhone;
    private String pw;

    public LoginRequest(String myPhone, String pw) {
        this.myPhone = myPhone;
        this.pw = pw;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
