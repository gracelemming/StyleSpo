package com.example.stylespo.model;

import android.util.Patterns;

public class User {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String DOB;

    public User(String email, String firstName, String lastName, String DOB) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = DOB;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}
