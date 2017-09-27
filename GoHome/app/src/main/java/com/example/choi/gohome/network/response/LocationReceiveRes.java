package com.example.choi.gohome.network.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-08-25.
 */
public class LocationReceiveRes {
    private boolean result;
    private String message;
    private List<Locations> locations = new ArrayList<>();

    public LocationReceiveRes(boolean result, String message, List<Locations> locations) {
        this.result = result;
        this.message = message;
        this.locations = locations;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Locations> getLocations() {
        return locations;
    }

    public void setLocations(List<Locations> locations) {
        this.locations = locations;
    }
}
