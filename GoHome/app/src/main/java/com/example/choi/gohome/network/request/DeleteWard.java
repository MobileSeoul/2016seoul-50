package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-12.
 */
public class DeleteWard {
    private String myPhone, phone, token;

    public DeleteWard(String myPhone, String phone, String token) {
        this.myPhone = myPhone;
        this.phone = phone;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
