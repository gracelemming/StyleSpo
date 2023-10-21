package com.example.stylespo.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stylespo.model.UserAccount;

/*
THIS IS WHERE USER DATA IS EXTRACTED BASED ON THE com.example.stylespo.model.user file
 */
public class MainViewModel extends ViewModel {
    public LiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<UserAccount> userMutableLiveData;
    public MutableLiveData<UserAccount> getUserDetails(){
        if(userMutableLiveData == null) {
            //deal with later
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }
    public void onClick(View view){
        UserAccount userAccount = new UserAccount(email.getValue(), password.getValue());


    }

}
