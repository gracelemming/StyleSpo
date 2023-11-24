package com.example.stylespo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.bumptech.glide.Glide;
import com.example.stylespo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView friendRequestsRecyclerView;
    private List<FriendRequestItem> friendRequestsList;
    private FriendRequestAdapter friendRequestsAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        Glide.get(this).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());


        friendRequestsRecyclerView = findViewById(R.id.friend_requests_recycler_view);
        friendRequestsList = new ArrayList<>();
        friendRequestsAdapter = new FriendRequestAdapter(this, R.layout.item_layout_friend_request,friendRequestsList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRequestsRecyclerView.setLayoutManager(layoutManager);
        friendRequestsRecyclerView.setAdapter(friendRequestsAdapter);

        loadFriendRequests();

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationsActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // You can add more UI elements and functionality as needed
    }

    // Inside the loadFriendRequests method in NotificationsActivity
    // Inside the loadFriendRequests method in NotificationsActivity
    // Inside the loadFriendRequests method in NotificationsActivity
    private void loadFriendRequests() {
        db.collection("friends_list")
                .document(currentUserId)
                .collection("friends")
                .whereEqualTo("status", "pending")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        Toast.makeText(NotificationsActivity.this, "Error loading friend requests", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        DocumentSnapshot document = documentChange.getDocument();
                        String friendUserId = document.getId();

                        switch (documentChange.getType()) {
                            case ADDED:
                                // Check if the friend request already exists in the list
                                boolean exists = false;
                                for (FriendRequestItem item : friendRequestsList) {
                                    if (item.getUserId().equals(friendUserId)) {
                                        exists = true;
                                        break;
                                    }
                                }

                                // If it doesn't exist, add it to the list
                                if (!exists) {
                                    new FriendRequestItem(friendUserId, new FriendRequestItem.InitializationCallback() {
                                        @Override
                                        public void onInitialized(FriendRequestItem friendRequestItem) {
                                            friendRequestsList.add(friendRequestItem);
                                            friendRequestsAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                break;

                            case REMOVED:
                                // Check if the list is not empty before trying to remove
                                if (!friendRequestsList.isEmpty()) {
                                    // Remove the friend request from the list
                                    for (FriendRequestItem item : friendRequestsList) {
                                        if (item.getUserId().equals(friendUserId)) {
                                            friendRequestsList.remove(item);
                                            friendRequestsAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                }
                                break;
                        }
                    }
                });
    }








    // You can add more methods and functionality as needed
}
