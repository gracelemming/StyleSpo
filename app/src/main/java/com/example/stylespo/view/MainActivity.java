package com.example.stylespo.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.stylespo.HomePage;
import com.example.stylespo.R;
import com.example.stylespo.databinding.ActivityMainBinding;
import com.example.stylespo.viewmodel.MainViewModel;
import com.example.stylespo.model.User;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public Button button;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding activityMainBinding;

    private MainViewModel mVModel;
    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if(savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new SignUpFragment();
            fm.beginTransaction().add(R.id.signUp_frag_container, fragment).commit();
        }

//
//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}