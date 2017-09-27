package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-09-12.
 */
public class GcmTokenUpdateResult {
    private boolean result;

    public GcmTokenUpdateResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
