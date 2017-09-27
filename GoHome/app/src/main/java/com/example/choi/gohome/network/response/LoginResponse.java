package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-16.
 */
public class LoginResponse {
    private boolean result;
    private String token;
    private String message;
//    private MyProfile myProfile;

    public LoginResponse(boolean result, String token, String message) {
        this.result = result;
        this.token = token;
        this.message = message;
//        this.myProfile = myProfile;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public MyProfile getMyProfile() {
//        return myProfile;
//    }
//
//    public void setMyProfile(MyProfile myProfile) {
//        this.myProfile = myProfile;
//    }
}
