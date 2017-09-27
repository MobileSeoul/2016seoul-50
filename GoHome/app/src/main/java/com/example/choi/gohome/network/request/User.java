package com.example.choi.gohome.network.request;

/**
 * Created by choi on 2016-08-16.
 */
public class User {
    private String myPhone;
    private String pw;
    private String email;
    private String name;
    private int age;
    private int gender;

    public User(String myPhone, String pw, String email, String name, int age, int gender) {
        this.myPhone = myPhone;
        this.pw = pw;
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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
}
