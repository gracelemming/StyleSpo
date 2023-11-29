package com.example.stylespo.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.stylespo.R;
import com.example.stylespo.viewmodel.MainViewModel;
import com.example.stylespo.model.User;

import android.view.Menu;
import android.widget.Button;


public class LoginAndSignUpActivity extends AppCompatActivity {
    public Button button;
    private AppBarConfiguration appBarConfiguration;

    private MainViewModel mVModel;
    private User userModel;

    public NavController navController;

    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(R.layout.activity_home);

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.activity_home, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}



