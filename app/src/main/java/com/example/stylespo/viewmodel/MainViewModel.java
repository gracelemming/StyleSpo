package com.example.stylespo.viewmodel;

import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stylespo.model.User;

/*
THIS IS WHERE USER DATA IS EXTRACTED BASED ON THE com.example.stylespo.model.user file
 */
public class MainViewModel extends ViewModel {
    public LiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;
    public MutableLiveData<User> getUserDetails(){
        if(userMutableLiveData == null) {
            //deal with later
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }
//    public void onClick(View view){
//        User user = new User(email.getValue(), password.getValue());
//
//
//    }

}
