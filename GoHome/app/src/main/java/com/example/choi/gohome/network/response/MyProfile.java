package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-22.
 */
public class MyProfile {
    private String myPhone;
    private String email;
    private String name;
    private int age;
    private int gender;
    private String guardian_phone1;
    private String guardian_phone2;
    private String guardian_phone3;
    private String user_code;

    public MyProfile(String myPhone, String email, String name, int age, int gender, String guardian_phone1,
                     String guardian_phone2, String guardian_phone3, String user_code) {
        this.myPhone = myPhone;
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.guardian_phone1 = guardian_phone1;
        this.guardian_phone2 = guardian_phone2;
        this.guardian_phone3 = guardian_phone3;
        this.user_code = user_code;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }
}
