package com.example.choi.gohome;

/**
 * Created by choi on 2016-08-16.
 */
public class MyLatLng {
    private double lat;
    private double lng;
    private boolean connected;

    public MyLatLng(double lat, double lng, boolean connected) {
        this.lat = lat;
        this.lng = lng;
        this.connected = connected;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
