package com.example.stylespo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.DataSource;
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

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private Context context;
    private int resource;
    private List<FriendRequestItem> friendRequestsList;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currUser;
    private CollectionReference friendListCollectionReference;

    public FriendRequestAdapter(Context context, int resource, List<FriendRequestItem> objects) {
        this.context = context;
        this.resource = resource;
        this.friendRequestsList = objects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, parent, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getUid();
        friendListCollectionReference = db.collection("friends_list");

        return new ViewHolder(view);
    }

    private RequestListener<Drawable> glideRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            // Handle the load failure here
            Log.e("GlideError", "Load failed: " + e.getMessage());
            return false; // Return false to allow Glide to handle the error, or true to indicate that you've handled it.
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            // Image loaded successfully
            return false;
        }
    };

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Check if the list is empty or if the position is valid
        if (position >= 0 && position < friendRequestsList.size()) {
            FriendRequestItem friendRequestItem = friendRequestsList.get(position);

            String profileImageRes = friendRequestItem.getProfileImageRes();

            if (friendRequestItem != null) {
                holder.usernameTextView.setText(friendRequestItem.getUsername());

                if (profileImageRes != null) {
                    StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child(profileImageRes);
                    Glide.with(this.context)
                            .load(profileImageRef)
                            .listener(glideRequestListener)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.profileImage);
                } else {
                    // Handle the case where image paths are null
                    Log.e("YourAdapter", "Image paths are null for position: " + position);
                }

                // Add click listeners or any other operations as needed
                holder.acceptButton.setOnClickListener(v -> {
                    // Double-check the list size before removal
                    if (position >= 0 && position < friendRequestsList.size()) {
                        String userID = friendRequestItem.getUserId();
                        DocumentReference currUserDocumentReference = friendListCollectionReference.document(currUser);
                        DocumentReference userDocumentReference = friendListCollectionReference.document(userID);
                        CollectionReference currUserFriendCollectionReference = currUserDocumentReference.collection("friends");
                        CollectionReference userFriendCollectionReference = userDocumentReference.collection("friends");

                        userFriendCollectionReference.document(currUser).update("status", "accepted");
                        currUserFriendCollectionReference.document(userID).update("status", "accepted")
                                .addOnSuccessListener(aVoid -> {
                                    // Update successful, remove the item from the list and notify the adapter
                                    //friendRequestsList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to update the friend request
                                    Toast.makeText(context, "Failed to accept friend request", Toast.LENGTH_SHORT).show();
                                });
                    }
                });

                holder.declineButton.setOnClickListener(v -> {
                    // Double-check the list size before removal
                    if (position >= 0 && position < friendRequestsList.size()) {
                        String userID = friendRequestItem.getUserId();
                        DocumentReference currUserDocumentReference = friendListCollectionReference.document(currUser);
                        DocumentReference userDocumentReference = friendListCollectionReference.document(userID);
                        CollectionReference currUserFriendCollectionReference = currUserDocumentReference.collection("friends");
                        CollectionReference userFriendCollectionReference = userDocumentReference.collection("friends");

                        userFriendCollectionReference.document(currUser).delete();
                        currUserFriendCollectionReference.document(userID).delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Deletion successful, remove the item from the list and notify the adapter
                                    //friendRequestsList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to delete the friend request
                                    Toast.makeText(context, "Failed to decline friend request", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return friendRequestsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button acceptButton;
        Button declineButton;
        ImageView profileImage;

        ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            usernameTextView = itemView.findViewById(R.id.username);
            acceptButton = itemView.findViewById(R.id.accept_button);
            declineButton = itemView.findViewById(R.id.decline_button);
        }
    }
}
