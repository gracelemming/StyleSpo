package com.example.stylespo.model;

import androidx.annotation.NonNull;

import java.util.Objects;

\
public class UserAccount {
    private int mUid;

    private String mName;
    private String mEmail;
    private String mPassword;

    public UserAccount(@NonNull String name, @NonNull String password, @NonNull String email) {
        mName = name;
        mPassword = password;
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public String getPassword() {
        return mPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return mName.equals(that.mName) && mPassword.equals(that.mPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUid, mName, mPassword);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserAccount{" +
                "uid=" + mUid +
                "; name='" + mName + '\'' +
                "; password='" + mPassword + '\'' +
                '}';
    }
}
