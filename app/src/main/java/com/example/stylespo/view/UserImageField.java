package com.example.stylespo.view;


import com.google.firebase.firestore.FirebaseFirestore;

public class UserImageField {
    private String userID;
    private String name;
    private String profileImageRes;
    private String todayImageRes;

    public UserImageField(String userID) {
        this.userID = userID;
        this.profileImageRes = userID + "/profile_image.jpg";
        this.todayImageRes = userID + "/today_image.jpg";
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public String getProfileImageRes() {
        return profileImageRes;
    }

    public String getTodayImageRes() {
        return todayImageRes;
    }
}