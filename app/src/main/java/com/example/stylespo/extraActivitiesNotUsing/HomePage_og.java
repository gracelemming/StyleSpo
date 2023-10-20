package com.example.stylespo.extraActivitiesNotUsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.stylespo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;

public class HomePage_og extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.HomepageFragment);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.HomepageFragment) {
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
                startActivity(new Intent(getApplicationContext(), Profile_og.class));
                finish();
                return true;
            }
            return false;
        });

    }
}