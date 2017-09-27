package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-16.
 */
public class ResultResponse {
    private boolean result;

    public ResultResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
