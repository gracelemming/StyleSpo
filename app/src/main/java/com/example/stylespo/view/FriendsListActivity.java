package com.example.stylespo.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stylespo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    private RecyclerView friendRequestsRecyclerView;
    private List<FriendRequestItem> friendRequestsList;
    private FriendRequestAdapter friendRequestsAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        friendRequestsRecyclerView = findViewById(R.id.recycler_view_friends);
        friendRequestsList = new ArrayList<>();
        friendRequestsAdapter = new FriendRequestAdapter(this, R.layout.item_layout_friend_request, friendRequestsList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRequestsRecyclerView.setLayoutManager(layoutManager);
        friendRequestsRecyclerView.setAdapter(friendRequestsAdapter);

        loadFriends();
    }

    private void loadFriends() {
        // Assuming you have a "friends_list" collection in Firestore
        CollectionReference friendListCollectionReference = db.collection("friends_list");

        friendListCollectionReference.document(currentUserId)
                .collection("friends")
                .whereEqualTo("status", "accepted")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        FriendRequestItem friend = documentSnapshot.toObject(FriendRequestItem.class);
                        if (friend != null) {
                            friendRequestsList.add(friend);
                        }
                    }
                    friendRequestsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FriendsListActivity", "Error fetching friends", e);
                    Toast.makeText(FriendsListActivity.this, "Error fetching friends", Toast.LENGTH_SHORT).show();
                });
    }
}
