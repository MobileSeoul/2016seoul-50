package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-08-23.
 */
public class LocationUpdateReq {
    private String phone;
    private double nowLat;
    private double nowLng;
    private String token;

    public LocationUpdateReq(String myPhone, double nowLat, double nowLng, String token) {
        this.phone = myPhone;
        this.nowLat = nowLat;
        this.nowLng = nowLng;
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String myPhone) {
        this.phone = myPhone;
    }

    public double getNowLat() {
        return nowLat;
    }

    public void setNowLat(double nowLat) {
        this.nowLat = nowLat;
    }

    public double getNowLng() {
        return nowLng;
    }

    public void setNowLng(double nowLng) {
        this.nowLng = nowLng;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
