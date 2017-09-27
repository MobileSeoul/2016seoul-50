package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-23.
 */
public class LocationUpdateRes {
    private boolean result;
    private String message;

    public LocationUpdateRes(boolean result, String message) {
        this.result = result;
        this.message = message;
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
}
