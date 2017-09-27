package com.example.choi.gohome;

/**
 * Created by choi on 2016-09-21.
 */
public class LatLngSize {
    private double lat;
    private double lng;
    private int size;

    public LatLngSize(double lat, double lng, int size) {
        this.lat = lat;
        this.lng = lng;
        this.size = size;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
