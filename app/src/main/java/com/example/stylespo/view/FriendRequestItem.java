package com.example.stylespo.view;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendRequestItem {
    private String userId;
    private String profileImageRes;
    private String username; // Updated to store the username

    public interface InitializationCallback {
        void onInitialized(FriendRequestItem friendRequestItem);
    }

    public FriendRequestItem(String userId, InitializationCallback callback) {
        this.userId = userId;
        this.profileImageRes = userId + "/profile_image.jpg";



        // Fetch username from Firestore
        FirebaseFirestore.getInstance().collection("usernames").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Assuming "username" is the field in your Firestore document
                                username = document.getString("username");
                                // Callback to notify that initialization is complete
                                callback.onInitialized(FriendRequestItem.this);
                            }
                        }
                    }
                });
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageRes() {
        return profileImageRes;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
