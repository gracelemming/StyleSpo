package com.example.stylespo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.os.Bundle;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_add);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
                return true;
            } else if (itemId == R.id.bottom_add) {
                return true;
            } else if (itemId == R.id.bottom_discover) {
                startActivity(new Intent(getApplicationContext(), Discovery.class));
                finish();
                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), Profile.class));
                finish();
                return true;
            }
            return false;
        });

    }
}