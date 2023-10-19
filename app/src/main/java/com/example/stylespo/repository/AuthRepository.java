package com.example.stylespo.repository;
import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    //for when they are logged out set userLoggedMutableLiveData.postValue(true
    private MutableLiveData<Boolean> UserLoggedMutableLiveData;
    private FirebaseAuth auth;

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return UserLoggedMutableLiveData;
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public AuthRepository(Application application) {
        this.application= application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        auth= FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null ){
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());

        }
    }

    public void register(String email, String pass){
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }else{
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());

                }else{
                Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
