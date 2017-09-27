package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-10-11.
 */
public class UpdateProfile {

    private String email, name, myPhone, token;
    private int age, gender;

    public UpdateProfile(String email, String name, int age, int gender, String myPhone, String token) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.myPhone = myPhone;
        this.token = token;
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
}
