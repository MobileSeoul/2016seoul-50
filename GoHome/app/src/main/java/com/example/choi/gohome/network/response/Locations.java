package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-25.
 */
public class Locations {
    private String name;
    private double nowLat;
    private double nowLng;
    private String mod_date;

    public Locations(String name, double nowLat, double nowLng, String mod_date) {
        this.name = name;
        this.nowLat = nowLat;
        this.nowLng = nowLng;
        this.mod_date = mod_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMod_date() {
        return mod_date;
    }

    public void setMod_date(String mod_date) {
        this.mod_date = mod_date;
    }
}
