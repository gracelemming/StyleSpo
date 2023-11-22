package com.example.stylespo.view;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

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
    String currUser;
    Button friendSendOrRemoveOrCancelRequest;
    private Uri photoUri;
    CollectionReference friendListCollectionReference;
    DocumentReference currUserDocumentReference, userDocumentReference;
    CollectionReference currUserFriendCollectionReference, userFriendCollectionReference;
    private boolean isButtonClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

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
        currUser = mAuth.getUid();

                // Create a storage reference from our app
        storageReference = storage.getReference();
        storageReferenceFolder = storageReference.child(userID);
        friendListCollectionReference = db.collection("friends_list");
        currUserDocumentReference = friendListCollectionReference.document(currUser);
        userDocumentReference = friendListCollectionReference.document(userID);
        currUserFriendCollectionReference = currUserDocumentReference.collection("friends");
        userFriendCollectionReference = userDocumentReference.collection("friends");

        // Initialize views
        userName = findViewById(R.id.username);
        profileImage = findViewById(R.id.profile_image);
        todayImage = findViewById(R.id.today_image);

        // Call methods to set up UI components
        loadUserData();
        loadProfileImage();
        loadTodayImage();


        updateTextForFriendSendAndDecline();

        friendSendOrRemoveOrCancelRequest = (Button) findViewById(R.id.friend_request_button_send);
            friendSendOrRemoveOrCancelRequest.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (isButtonClickable) {
                        isButtonClickable = false;
                        clickOnFriendSend();
                        updateTextForFriendSendAndDecline();
                    }
                }
            });

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

    private void updateTextForFriendSendAndDecline() {

        currUserFriendCollectionReference.document(userID).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                // Handle the error
                Toast.makeText(UserProfileActivity.this, "Error checking friend status", Toast.LENGTH_SHORT).show();
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Retrieve friend status from the snapshot
                String friendStatus = documentSnapshot.getString("status");

                if (friendStatus != null) {
                    // declineRequest set invisible and disable
                    if (friendStatus.equals("accepted")) {
                        friendSendOrRemoveOrCancelRequest.setText("Remove Friend");
                    } else if (friendStatus.equals("sent")) {
                        friendSendOrRemoveOrCancelRequest.setText("Cancel");
                    } else if (friendStatus.equals("pending")){
                        friendSendOrRemoveOrCancelRequest.setText("Accept");
                        // declineRequest set visible and enable
                    }
                }
            } else {
                // Handle the case where the friend document doesn't exist
                // declineRequest set invisible and disable
                friendSendOrRemoveOrCancelRequest.setText("Add Friend");
            }
        });
    }

    private void clickOnFriendSend() {

        currUserFriendCollectionReference.document(userID).addSnapshotListener((documentSnapshot, error) -> {

            isButtonClickable = true;
            if (error != null) {
                // Handle the error
                Toast.makeText(UserProfileActivity.this, "Error checking friend status", Toast.LENGTH_SHORT).show();
                return;
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Retrieve friend status from the snapshot
                String friendStatus = documentSnapshot.getString("status");

                if (friendStatus != null) {
                    if (friendStatus.equals("accepted")) {
                        //friendSendOrRemoveOrCancelRequest.setText("Add Friend");
                        currUserFriendCollectionReference.document(userID).delete();
                        userFriendCollectionReference.document(currUser).delete();
                    } else if (friendStatus.equals("sent")) {
                        //friendSendOrRemoveOrCancelRequest.setText("Add Friend");
                        currUserFriendCollectionReference.document(userID).delete();
                        userFriendCollectionReference.document(currUser).delete();
                    } else if (friendStatus.equals("pending")){
                        //friendSendOrRemoveOrCancelRequest.setText("Remove Friend");
                        currUserFriendCollectionReference.document(userID).update("status","accepted");
                        userFriendCollectionReference.document(currUser).update("status","accepted");
                    }
                }
            } else {
                // Handle the case where the friend document doesn't exist
                //friendSendOrRemoveOrCancelRequest.setText("Cancel Request");
                Map<String,Object> currUserMap = new HashMap<>();
                Map<String,Object> userMap = new HashMap<>();

                currUserMap.put("status","sent");
                userMap.put("status","pending");

                currUserFriendCollectionReference.document(userID).set(currUserMap);
                userFriendCollectionReference.document(currUser).set(userMap);

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
