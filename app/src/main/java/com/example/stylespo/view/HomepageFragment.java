package com.example.stylespo.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomepageFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String userID;

    private RecyclerView recyclerView;
    private YourAdapter adapter;

    public HomepageFragment() {
        // Required empty public constructor
        //
    }

    public void onStop(){
        super.onStop();
    }


    public void onPause(){
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // Use a callback to get the data and update the adapter
        getYourImageList(imageList -> {
            adapter = new YourAdapter(imageList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        });
        return v;
    }

    private void getYourImageList(OnImageListReadyCallback callback) {
        List<UserImageField> imageList = new ArrayList<>();

        // Step 1: Query the collection of user IDs
        CollectionReference userCollection = db.collection("images");
        userCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                // Step 2: Retrieve all user IDs
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String userId = document.getId();
                    UserImageField userImageField = new UserImageField(userId);
                    imageList.add(userImageField);
                }

                // Notify the callback with the data
                callback.onImageListReady(imageList);
            } else {
                // Handle the case where no documents were found in the collection.
                Log.e("Firestore", "No documents found in the 'users' collection.");
            }
        }).addOnFailureListener(e -> {
            // Handle Firestore query failure
            Log.e("Firestore", "Firestore query failed: " + e.getMessage());
        });
    }

    // Callback interface
    public interface OnImageListReadyCallback {
        void onImageListReady(List<UserImageField> imageList);
    }

    static class UserImageField {
        private String userID;
        private String name;
        private String profileImageRes;
        private String todayImageRes;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        public UserImageField(String userID) {
            this.userID = userID;
            this.profileImageRes = userID + "/profile_image.jpg";
            this.todayImageRes = userID + "/today_image.jpg";
        }


        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getProfileImageRes() {
            return profileImageRes;
        }

        public String getTodayImageRes() {
            return todayImageRes;
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
    }class YourAdapter extends RecyclerView.Adapter<YourAdapter.ViewHolder> {

        private final List<UserImageField> imageList;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        public YourAdapter(List<UserImageField> imageList) {
            this.imageList = imageList;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profileImage;
            TextView username;
            ImageView todayImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                profileImage = itemView.findViewById(R.id.profile_image);
                username = itemView.findViewById(R.id.username);
                todayImage = itemView.findViewById(R.id.today_image);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout_for_homepage, parent, false);
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
            UserImageField userImageField = imageList.get(position);

            // Set image resources and other data to your views in ViewHolder
            StorageReference profileImageRef = storageReference.child(userImageField.getProfileImageRes());
            Glide.with(requireContext())
                    .load(profileImageRef) // image ref
                    .listener(glideRequestListener) // Attach the listener here
                    .skipMemoryCache(true) // Disable caching in memory
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                    .into(holder.profileImage);  // imageview object
            holder.username.setText(userImageField.getName());
            StorageReference todayImageRef = storageReference.child(userImageField.getTodayImageRes());
            Glide.with(requireContext())
                    .load(todayImageRef) // image ref
                    .listener(glideRequestListener) // Attach the listener here
                    .skipMemoryCache(true) // Disable caching in memory
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                    .into(holder.todayImage);  // imageview object
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }



}
