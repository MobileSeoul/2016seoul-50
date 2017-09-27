package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-11.
 */
public class CheckUserCode {

    private String myPhone, user_code, token;

    public CheckUserCode(String myPhone, String user_code, String token) {
        this.myPhone = myPhone;
        this.user_code = user_code;
        this.token = token;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
