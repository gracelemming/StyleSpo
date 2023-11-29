package com.example.stylespo.model;

import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class User {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String DOB;
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static final String DOB_PATTERN = "MM/dd/yyyy";
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

    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }

    public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    public static boolean isDOBValid(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            return false; // DOB cannot be empty
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DOB_PATTERN, Locale.US);
        sdf.setLenient(false); // Strict date parsing

        try {
            // Try parsing the DOB string to a Date object
            Date parsedDate = sdf.parse(dob);
            if (parsedDate != null) {
                // Successfully parsed, so the DOB is valid
                return true;
            }
        } catch (ParseException e) {
            // Parsing failed, indicating invalid DOB format
            e.printStackTrace(); // Handle or log the exception if needed
        }

        return false;
    }
}
