package com.example.stylespo.viewmodel;

import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stylespo.model.User;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    private MutableLiveData<User> userMutableLiveData;
    public MutableLiveData<User> getUserDetails(){
        if(userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }
    public void onClick(View view){
        User user = new User(email.getValue(), password.getValue());


    }

}
