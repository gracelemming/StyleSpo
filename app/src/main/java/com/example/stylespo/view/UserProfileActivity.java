package com.example.stylespo.view;

import static android.app.PendingIntent.getActivity;

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
import com.example.stylespo.model.FriendReq;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
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
    private StorageReference friendRequestRef, userRef;
    String userID;
    String currUser;
    String FRIEND_STATUS;
    Button friend_send;
    Button friend_decline;
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
        friendRequestRef = storageReference.child("FriendRequests");

        // Initialize views
        userName = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        todayImage = findViewById(R.id.today_image);
        currUser = mAuth.getCurrentUser().getUid();
        FRIEND_STATUS="not_friends";

        // Call methods to set up UI components
        loadUserData();
        loadProfileImage();
        loadTodayImage();
        friend_decline = (Button) findViewById(R.id.friend_request_button_decline);
        friend_send = (Button) findViewById(R.id.friend_request_button_send);
        friend_decline.setVisibility(View.INVISIBLE);
        friend_decline.setEnabled(false);

        if (!currUser.toString().equals(userID.toString())) {
            friend_send.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    friend_send.setEnabled(false);
                    if (FRIEND_STATUS.equals("not_friends")){
                        sendFriendRequest();
                    }
                }
            });
        }else{
            friend_decline.setVisibility(View.INVISIBLE);
            friend_send.setVisibility(View.INVISIBLE);

        }



        back_button = (ImageButton) findViewById(R.id.back_button);
        // Set a click listener for the back button
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class );
                startActivity(intent);
            }
        });





    }


        private void sendFriendRequest() {
            // Reference to the friend requests collection
            CollectionReference friendRequestsCollection = db.collection("friend_requests");

            // Create a friend request data object
            FriendReq friendReq = new FriendReq(currUser, userID, "pending");

            // Check if a friend request already exists
            friendRequestsCollection
                    .whereEqualTo("sender", currUser)
                    .whereEqualTo("receiver", userID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Check if there are no existing friend requests
                            if (task.getResult() == null || task.getResult().isEmpty()) {
                                // Add a friend request document to the collection
                                friendRequestsCollection.add(friendReq)
                                        .addOnSuccessListener(documentReference -> {
                                            // Friend request sent successfully
                                            Toast.makeText(UserProfileActivity.this, "Friend request sent", Toast.LENGTH_SHORT).show();
                                            FRIEND_STATUS = "friend_request_sent";
                                            friend_send.setEnabled(true);
                                            friend_send.setText("Cancel friend request");
                                            friend_decline.setVisibility(View.INVISIBLE);
                                            friend_decline.setEnabled(false);
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle the error
                                            Toast.makeText(UserProfileActivity.this, "Failed to send friend request", Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                // A friend request already exists
                                Toast.makeText(UserProfileActivity.this, "Friend request already sent", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle the error
                            Toast.makeText(UserProfileActivity.this, "Error checking friend requests", Toast.LENGTH_SHORT).show();
                        }
                    });
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
