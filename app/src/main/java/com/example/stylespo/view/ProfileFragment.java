package com.example.stylespo.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment  implements View.OnClickListener {

    Button showPopupButton;
    ImageView imageView;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    String userID;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        showPopupButton = rootView.findViewById(R.id.drop_down_button);
        imageView = rootView.findViewById(R.id.imageView);
        showPopupButton.setOnClickListener(this);
        displayImage();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.drop_down_button) {
            PopupMenu popupMenu = new PopupMenu(getActivity(), v);
            popupMenu.getMenuInflater().inflate(R.menu.drop_down, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // Handle menu item click here
                    int itemId = item.getItemId();
                    if (itemId == R.id.settings_button) {
                        Toast.makeText(getActivity(), "Settings Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.logout_button) {
                        Toast.makeText(getActivity(), "Logout Selected", Toast.LENGTH_SHORT).show();
                        // Handle Logout option click
                        FirebaseAuth.getInstance().signOut();
                        // Redirect to the login screen or perform any additional cleanup
                        // For example, navigate to the login screen
                        // Replace MainActivity.class with your login activity class
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        // Finish the current activity
                        getActivity().finish();
                        return true;
                    } else {
                        // Add more cases for other menu items as needed
                        return false;
                    }
                }
            });
            popupMenu.show();
        }

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
    private void displayImage(){
        DocumentReference documentReference  = db.collection("images").document(userID);
        documentReference.get()
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
                                    .into(imageView);  // imageview object
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