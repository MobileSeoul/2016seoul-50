package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-16.
 */
public class RegisterResponse {
    private boolean result;
    private String message;

    public RegisterResponse(boolean result, String message) {
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
