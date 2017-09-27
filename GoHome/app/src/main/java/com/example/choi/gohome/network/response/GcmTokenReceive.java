package com.example.choi.gohome.network.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-09-12.
 */
public class GcmTokenReceive {
    private boolean result;
    private List<GcmTokenList> gcm_token = new ArrayList<>();

    public GcmTokenReceive(boolean result, List<GcmTokenList> gcm_token) {
        this.result = result;
        this.gcm_token = gcm_token;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<GcmTokenList> getGcm_token() {
        return gcm_token;
    }

    public void setGcm_token(List<GcmTokenList> gcm_token) {
        this.gcm_token = gcm_token;
    }
}
