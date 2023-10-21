package com.example.stylespo.extraActivitiesNotUsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import com.example.stylespo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile_og extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.ProfileFragment);

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
                startActivity(new Intent(getApplicationContext(), Discovery_og.class));
                finish();
                return true;
            } else if (itemId == R.id.ProfileFragment) {

                return true;
            }
            return false;
        });

    }
}