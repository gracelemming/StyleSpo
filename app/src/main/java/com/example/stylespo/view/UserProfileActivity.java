package com.example.stylespo.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.stylespo.R;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        String userID = getIntent().getStringExtra("userId");

        // Check if userId is not null
        if (userID != null) {
            // Display a Toast message with userId
            Toast.makeText(this, "User ID: " + userID, Toast.LENGTH_SHORT).show();

            // Pass the user ID to the fragment
            Bundle bundle = new Bundle();
            bundle.putString("userId", userID);

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = new UserProfileFragment();
            fragment.setArguments(bundle);

            fm.beginTransaction().add(R.id.activity_other_user_profile, fragment).commit();
        } else {
            // Display a default message if userId is null
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }
    }
}
