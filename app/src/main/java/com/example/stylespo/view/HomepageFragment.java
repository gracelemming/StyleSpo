//package com.example.stylespo.view;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import com.example.stylespo.R;
//import com.example.stylespo.viewmodel.MainViewModel;
//import com.google.android.material.navigation.NavigationBarView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomepageFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HomepageFragment extends Fragment {
//
//    private MainViewModel mMainViewModel;
//    private FirebaseFirestore db;
//    private FirebaseAuth mAuth;
//    BottomNavigationView bottomNavigationView;
//
//    public HomepageFragment() {
//        // Required empty public constructor
//        //
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//    public void onStop(){
//        super.onStop();
//    }
//
//
//    public void onPause(){
//        super.onPause();
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
//        bottomNavigationView = v.findViewById(R.id.bottomNavigationView);
//
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item){
//                Fragment fragment = null;
//                int id = item.getItemId();
//                if (id == R.id.AddFragment) {
//                    fragment = new CameraFragment();
//                } else if (id == R.id.DiscoverFragment) {
//                    fragment = new DiscoverFragment();
//                } else if (id == R.id.ProfileFragment) {
//                    fragment = new ProfileFragment();
//                } else if (id == R.id.HomepageFragment) {
//                    fragment = new HomepageFragment();
//                }
//             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, fragment).commit();
//
//                return true;
//            }
//        });
//        return v;
//    }
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//}
//
//

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

import androidx.fragment.app.Fragment;

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
    private BottomNavigationView bottomNavigationView;
    private ImageView randomUserProfileImage;
    private TextView randomUserName;
    private ImageView randomUserTodayImage;

    private ScrollView scrollView;

    String userID;

    StorageReference storageReference;

    FirebaseStorage storage;

    StorageReference storageReferenceFolder;

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
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        // Create a storage reference from our app
        storageReference = storage.getReference();
        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage2, container, false);
        randomUserName = v.findViewById(R.id.username);
        randomUserProfileImage = v.findViewById(R.id.profile_image);
        randomUserTodayImage = v.findViewById(R.id.today_image);
        fetchRandomUserImage();
        return v;
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


    private void fetchRandomUserImage() {

        // Step 1: Query the collection of user IDs
        CollectionReference userCollection = db.collection("images"); // Replace "your_document_id" with the actual document ID
        userCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                List<String> userIds = new ArrayList<>();

                // Step 2: Retrieve all user IDs
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String userId = document.getId();
                    userIds.add(userId);
                }
                if (!userIds.isEmpty()) {
                    // Step 3: Select a random user ID
                    Random random = new Random();
                    int randomIndex = random.nextInt(userIds.size());
                    String randomUserId = userIds.get(randomIndex);
                    displayUserImage(randomUserId);
                    displayUserNameAndProfilePicture(randomUserId);
                    // Now you have a random user ID saved in the 'userID' variable.
                } else {
                    // Handle the case where there are no user IDs in the collection.
                    Log.e("Firestore", "No user IDs found in the 'users' collection.");
                }
            } else {
                // Handle the case where no documents were found in the collection.
                Log.e("Firestore", "No documents found in the 'users' collection.");
            }
        }).addOnFailureListener(e -> {
            // Handle Firestore query failure
            Log.e("Firestore", "Firestore query failed: " + e.getMessage());
        });

    }

    private void displayUserNameAndProfilePicture(String randomUserId){
        DocumentReference userDocumentReference  = db.collection("users").document(randomUserId);
        userDocumentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            // Access fields in the data map
                            randomUserName.setText((String) data.get("first_name")+" "+(String) data.get("last_name"));
                            StorageReference imageRef = storageReference.child(randomUserId+"/profile_image.jpg");
                            Glide.with(requireContext())
                                    .load(imageRef) // image ref
                                    .listener(glideRequestListener) // Attach the listener here
                                    .skipMemoryCache(true) // Disable caching in memory
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                                    .into(randomUserProfileImage);  // imageview object
                        }
                    } else {
                        Log.d("TAG","Document Does not Exist");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("Firestore", "Error getting document", e);
                });
    }

    private void displayUserImage(String randomUserId){
        DocumentReference imageDocumentReference  = db.collection("images").document(randomUserId);
        imageDocumentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            // Access fields in the data map
                            String imageUrl = (String) data.get("imageUrl");
                            StorageReference imageRef = storageReference.child(imageUrl);
                            Glide.with(requireContext())
                                    .load(imageRef) // image ref
                                    .listener(glideRequestListener) // Attach the listener here
                                    .skipMemoryCache(true) // Disable caching in memory
                                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                                    .into(randomUserTodayImage);  // imageview object
                        }
                    } else {
                        Log.d("TAG","Document Does not Exist");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("Firestore", "Error getting document", e);
                });
    }



}

