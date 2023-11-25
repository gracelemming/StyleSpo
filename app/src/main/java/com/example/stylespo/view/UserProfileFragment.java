package com.example.stylespo.view;

import static android.content.Intent.getIntent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.example.stylespo.model.User;
import com.example.stylespo.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UserProfileFragment extends Fragment  {

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

    Button friendSendOrRemoveOrCancelRequest, declineRequest;
    private Uri photoUri;
    CollectionReference friendListCollectionReference;
    DocumentReference currUserDocumentReference, userDocumentReference;
    CollectionReference currUserFriendCollectionReference, userFriendCollectionReference;

    TextView friendCount;
    private boolean isButtonClickable = true;

    public UserProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getArguments().getString("userId");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getUid();
        storageReference = storage.getReference();
        storageReferenceFolder = storageReference.child(userID);
        friendListCollectionReference = db.collection("friends_list");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other_user_profile, container, false);

        userName = v.findViewById(R.id.username);
        profileImage = v.findViewById(R.id.profile_image);
        todayImage = v.findViewById(R.id.today_image);
        friendCount = v.findViewById(R.id.friend_count);

        updateFriendCount();
        loadUserData();
        loadProfileImage();
        loadTodayImage();


        friendSendOrRemoveOrCancelRequest = v.findViewById(R.id.friend_request_button_send);
        declineRequest = v.findViewById(R.id.friend_request_button_decline);

        declineRequest.setEnabled(false);
        declineRequest.setVisibility(View.INVISIBLE);

        updateTextForFriendSendAndDecline();
        friendSendOrRemoveOrCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButtonClickable) {
                    isButtonClickable = false;
                    currUserDocumentReference = friendListCollectionReference.document(currUser);
                    userDocumentReference = friendListCollectionReference.document(userID);
                    currUserFriendCollectionReference = currUserDocumentReference.collection("friends");
                    userFriendCollectionReference = userDocumentReference.collection("friends");
                    clickOnFriendSend();
                }
            }
        });
        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currUserDocumentReference = friendListCollectionReference.document(currUser);
                userDocumentReference = friendListCollectionReference.document(userID);
                currUserFriendCollectionReference = currUserDocumentReference.collection("friends");
                userFriendCollectionReference = userDocumentReference.collection("friends");
                cancelRequest();
                updateTextForFriendSendAndDecline();
            }
        });
        back_button = v.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }


    private void updateFriendCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "friends" subcollection of the current user
        CollectionReference friendsCollection = db.collection("friends_list")
                .document(userID)
                .collection("friends");

        friendsCollection.whereEqualTo("status", "accepted")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    // Get the count of friends with "status" as "accepted" and update the UI
                    int count = queryDocumentSnapshots.size();
                    friendCount.setText(String.valueOf(count));
                });
    }

    private void updateTextForFriendSendAndDecline() {
        db.document("friends_list/"+currUser+"/friends/"+userID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String friendStatus = documentSnapshot.getString("status");
                        if (friendStatus != null) {
                            updateUIForFriendStatus(friendStatus);
                        }
                    } else {
                        updateUIForNoFriendStatus();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error checking friend status", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUIForFriendStatus(String friendStatus) {
        declineRequest.setEnabled(false);
        declineRequest.setVisibility(View.INVISIBLE);
        if (friendStatus.equals("accepted")) {
            friendSendOrRemoveOrCancelRequest.setText("Remove Friend");
        } else if (friendStatus.equals("sent")) {
            friendSendOrRemoveOrCancelRequest.setText("Cancel Request");
        } else if (friendStatus.equals("pending")) {
            friendSendOrRemoveOrCancelRequest.setText("Accept");
            declineRequest.setEnabled(true);
            declineRequest.setVisibility(View.VISIBLE);
        }
    }

    private void updateUIForNoFriendStatus() {
        declineRequest.setEnabled(false);
        declineRequest.setVisibility(View.INVISIBLE);
        friendSendOrRemoveOrCancelRequest.setText("Add Friend");
    }

    private void clickOnFriendSend() {
        currUserFriendCollectionReference.document(userID).get().addOnCompleteListener(task -> {
            isButtonClickable = true;
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String friendStatus = documentSnapshot.getString("status");
                    handleFriendStatus(friendStatus);
                    updateTextForFriendSendAndDecline();
                } else {
                    handleFriendStatus(null);
                    updateTextForFriendSendAndDecline();
                }
            } else {
                Toast.makeText(getActivity(), "Error checking friend status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFriendStatus(String friendStatus) {
        if (friendStatus != null) {
            if (friendStatus.equals("accepted")) {
                removeFriend();
            } else if (friendStatus.equals("sent")) {
                cancelRequest();
            } else if (friendStatus.equals("pending")) {
                acceptRequest();
            }
        } else {
            addFriend();
        }
    }

    private void removeFriend() {
        db.runTransaction(transaction -> {
            transaction.delete(currUserFriendCollectionReference.document(userID));
            transaction.delete(userFriendCollectionReference.document(currUser));
            return null;
        }).addOnSuccessListener(aVoid -> {
            // Handle success
        }).addOnFailureListener(e -> {
            // Handle failure
        });
        updateFriendCount();
    }

    private void cancelRequest() {
        currUserFriendCollectionReference.document(userID).delete();
        userFriendCollectionReference.document(currUser).delete();
    }

    private void acceptRequest() {
        currUserFriendCollectionReference.document(userID).update("status", "accepted");
        userFriendCollectionReference.document(currUser).update("status", "accepted");
    }

    private void addFriend() {
        Map<String, Object> currUserMap = new HashMap<>();
        Map<String, Object> userMap = new HashMap<>();

        currUserMap.put("status", "sent");
        userMap.put("status", "pending");

        currUserFriendCollectionReference.document(userID).set(currUserMap);
        userFriendCollectionReference.document(currUser).set(userMap);
    }

    private void loadUserData() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        if (username != null) {
                            userName.setText(username);
                        }
                    } else {
                        Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error loading user data", Toast.LENGTH_SHORT).show();
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

    private RequestListener<Drawable> glideRequestListener = new RequestListener<android.graphics.drawable.Drawable>() {
        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Toast.makeText(getActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    };
}