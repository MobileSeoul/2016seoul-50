package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-08-30.
 */
public class AddressRequest {
    private String phone;
    private String token;

    public AddressRequest(String phone, String token) {
        this.phone = phone;
        this.token = token;
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
