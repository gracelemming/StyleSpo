package com.example.stylespo.viewmodel;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import android.text.TextUtils;
import android.util.Patterns;


import com.example.stylespo.model.User;
import com.example.stylespo.repository.AuthRepository;
import com.google.firebase.auth.FirebaseUser;

public class SignUpViewModel extends AndroidViewModel{

    private AuthRepository repo;
    private MutableLiveData<FirebaseUser> userData;

    private MutableLiveData<Boolean> loginStatus;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        repo = new AuthRepository(application);
        userData = repo.getFirebaseUserMutableLiveData();

    }

    public void register(String email, String pass){
        repo.register(email, pass);

    }

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public void login(String email, String pass){
        repo.login(email, pass);
    }
}
