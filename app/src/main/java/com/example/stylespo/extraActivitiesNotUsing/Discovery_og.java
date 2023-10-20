package com.example.stylespo.extraActivitiesNotUsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import com.example.stylespo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Discovery_og extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.DiscoverFragment);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.HomepageFragment) {
                startActivity(new Intent(getApplicationContext(), HomePage_og.class));
                finish();
                return true;
            } else if (itemId == R.id.AddFragment) {
                startActivity(new Intent(getApplicationContext(), Add_og.class));
                finish();
                return true;
            } else if (itemId == R.id.DiscoverFragment) {

                return true;
            } else if (itemId == R.id.ProfileFragment) {
                startActivity(new Intent(getApplicationContext(), Profile_og.class));
                finish();
                return true;
            }
            return false;
        });

    }
}