package com.example.demo_app.models;

public class User {
    public String uid, fname, lname, username, age, gender, phone, addr, dob, yowexp, email, doj, userType;

    public User() {

    }

    public User(String uid, String fname, String lname, String username, String age,
                String gender, String phone, String addr, String dob, String yowexp, String email, String doj, String userType) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.addr = addr;
        this.dob = dob;
        this.yowexp = yowexp;
        this.email = email;
        this.doj = doj;
        this.userType = userType;
    }

    public User(String uid) {
        this.uid = uid;
    }

}