package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-11.
 */
public class UpdateGuard {

    private String guardian_phone1, guardian_phone2, guardian_phone3, myPhone, token;

    public UpdateGuard(String guardian_phone1, String guardian_phone2, String guardian_phone3, String myPhone, String token) {
        this.guardian_phone1 = guardian_phone1;
        this.guardian_phone2 = guardian_phone2;
        this.guardian_phone3 = guardian_phone3;
        this.myPhone = myPhone;
        this.token = token;
    }

    public String getGuardian_phone1() {
        return guardian_phone1;
    }

    public void setGuardian_phone1(String guardian_phone1) {
        this.guardian_phone1 = guardian_phone1;
    }

    public String getGuardian_phone2() {
        return guardian_phone2;
    }

    public void setGuardian_phone2(String guardian_phone2) {
        this.guardian_phone2 = guardian_phone2;
    }

    public String getGuardian_phone3() {
        return guardian_phone3;
    }

    public void setGuardian_phone3(String guardian_phone3) {
        this.guardian_phone3 = guardian_phone3;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
