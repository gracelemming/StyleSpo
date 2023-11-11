package com.example.stylespo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
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
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageReferenceFolder;
    private FirebaseAuth mAuth;
    String userID;
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
    }

    private void loadUserData() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("first_name");
                        String lastName = documentSnapshot.getString("last_name");

                        // Set the username in the UI
                        if (firstName != null && lastName != null) {
                            userName.setText(firstName + " " + lastName);
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

    // You can add more methods for additional functionality as needed

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

    // You can add more methods for additional functionality as needed

    // For example, if you want to handle profile image uploads, you can add a method like this:



    // You can call this method with the appropriate Uri when you want to upload a new profile image.

    // You can similarly add methods to handle other functionalities like loading today's image, updating user details, etc.

}
