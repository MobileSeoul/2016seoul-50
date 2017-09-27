package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-11.
 */
public class WithdrawRequest {

    private String myPhone, pw, token;

    public WithdrawRequest(String myPhone, String pw, String token) {
        this.myPhone = myPhone;
        this.pw = pw;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
