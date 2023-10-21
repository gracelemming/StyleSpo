package com.example.stylespo.model;

import android.util.Patterns;

public class User {
    private String email;
    private String password;

    private String username;


    public User(String email, String password) {
        this.email = email;
        this.password = password;


    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public boolean validEmail(){

        return  Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();

    }


}
