package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-11.
 */
public class UpdateWard {

    private String myPhone, phone, user_code, token;

    public UpdateWard(String myPhone, String phone, String user_code, String token) {
        this.myPhone = myPhone;
        this.phone = phone;
        this.user_code = user_code;
        this.token = token;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
