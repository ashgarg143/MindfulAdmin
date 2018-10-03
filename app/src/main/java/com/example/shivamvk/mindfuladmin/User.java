package com.example.shivamvk.mindfuladmin;

import java.util.List;

public class User {

    public User(){

    }

    String name,email,number,password,emailverified,numberverified,usertype;

    public User(String name, String email, String number, String password, String emailverified, String numberverified, String usertype) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password;
        this.emailverified = emailverified;
        this.numberverified = numberverified;
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailverified() {
        return emailverified;
    }

    public String getNumberverified() {
        return numberverified;
    }

    public String getUsertype() {
        return usertype;
    }
}
