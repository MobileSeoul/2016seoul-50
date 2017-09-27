package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-23.
 */
public class ProfileResponse {
    private boolean result;
    private String message;
    private MyProfile myProfile;

    public ProfileResponse(boolean result, String message, MyProfile myProfile) {
        this.result = result;
        this.message = message;
        this.myProfile = myProfile;
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

    public MyProfile getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(MyProfile myProfile) {
        this.myProfile = myProfile;
    }
}
