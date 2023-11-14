package com.example.stylespo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfileActivity extends AppCompatActivity {

    TextView userName;
    ImageView profileImage;
    ImageView todayImage;
    FirebaseFirestore db;

    ImageButton back_button;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageReferenceFolder;
    private FirebaseAuth mAuth;
    String userID;
    Button friend_request;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_other_user_profile);

        // Retrieve userId from the intent
        userID = getIntent().getStringExtra("userId");

        // Check if userId is not null
        if (userID != null) {
            // Display a Toast message with userId
            Toast.makeText(this, "User ID: " + userID, Toast.LENGTH_SHORT).show();
        } else {
            // Display a default message if userId is null
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Create a storage reference from our app
        storageReference = storage.getReference();
        storageReferenceFolder = storageReference.child(userID);

        // Initialize views
        userName = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        todayImage = findViewById(R.id.today_image);

        // Call methods to set up UI components
        loadUserData();
        loadProfileImage();
        loadTodayImage();

        back_button = (ImageButton) findViewById(R.id.back_button);



        // Set a click listener for the back button
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class );
                startActivity(intent);
                // Handle the back button click
/*

 bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                Fragment fragment = null;
                int id = item.getItemId();
                if (id == R.id.AddFragment) {
                    fragment = new CameraFragment();
                } else if (id == R.id.DiscoverFragment) {
                    fragment = new DiscoverFragment();
                } else if (id == R.id.ProfileFragment) {
                    fragment = new ProfileFragment();
                } else if (id == R.id.HomepageFragment) {
                    fragment = new HomepageFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, fragment).commit();

   BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        MenuItem profileMenuItem = bottomNavigationView.getMenu().findItem(R.id.HomepageFragment);
        profileMenuItem.setChecked(true);


                return true;
            }
        });
 Fragment newFragment = new HomepageFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment, and add the transaction to the back stack
                transaction.replace(R.id.ProfileFragment, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();

                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment = new BottomNavigationFragment();
                fm.beginTransaction().setReorderingAllowed(true).replace(R.id.homepage_frag_container, fragment).commit();
 */

                //getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, new HomepageFragment()).commit();
            }
        });


    }

    private void loadUserData() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");

                        // Set the username in the UI
                        if (username != null) {
                            userName.setText(username);
                        }
                    } else {
                        // Handle the case where the user data doesn't exist
                        Toast.makeText(UserProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(UserProfileActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadProfileImage() {
        StorageReference profileImageRef = storageReferenceFolder.child("/profile_image.jpg");
        Glide.with(this)
                .load(profileImageRef)
                .listener(glideRequestListener)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(profileImage);
    }

    private void loadTodayImage() {
        StorageReference todayImageRef = storageReferenceFolder.child("/today_image.jpg");
        Glide.with(this)
                .load(todayImageRef)
                .listener(glideRequestListener)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(todayImage);
    }


    private RequestListener<android.graphics.drawable.Drawable> glideRequestListener = new RequestListener<android.graphics.drawable.Drawable>() {
        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
            // Handle the load failure here
            Toast.makeText(UserProfileActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, DataSource dataSource, boolean isFirstResource) {
            // Image loaded successfully
            return false;
        }
    };
}
