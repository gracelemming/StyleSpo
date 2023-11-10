package com.example.stylespo.view;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment  implements View.OnClickListener {

    Button showPopupButton;
    TextView userName;
    ImageView profileImage;
    ImageView todayImage;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageReferenceFolder;
    private FirebaseAuth mAuth;
    String userID;
    private Uri photoUri;
    public ProfileFragment() {
        // Required empty public constructor
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
        storageReferenceFolder = storageReference.child(userID);
        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        showPopupButton = rootView.findViewById(R.id.drop_down_button);
        profileImage = rootView.findViewById(R.id.profile_image);
        todayImage = rootView.findViewById(R.id.today_image);
        showPopupButton.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        userName = rootView.findViewById(R.id.username);
        setName();
        displayProfileImage();
        displayTodayImage();
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
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.logout_button) {
                        Toast.makeText(getActivity(), "Logout Selected", Toast.LENGTH_SHORT).show();
                        // Handle Logout option click
                        FirebaseAuth.getInstance().signOut();
                        // Redirect to the login screen or perform any additional cleanup
                        // For example, navigate to the login screen
                        // Replace MainActivity.class with your login activity class
                        Intent intent = new Intent(getActivity(), LoginAndSignUpActivity.class);
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
        }else if (viewId == R.id.profile_image){

            // Create a unique file name for the image
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timestamp + ".jpg";

            // Create a file to store the captured photo
            File photoFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            photoUri = FileProvider.getUriForFile(requireContext(), "com.example.stylespo.fileprovider", photoFile);

            // Create a camera intent to capture the image and save it to the specified URI
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(cameraIntent);
        }
    }
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // The photo has been successfully captured and saved in the specified URI
                    // You can now use the photoUri to access the captured image and save it in the database
                    uploadImage(photoUri);
                } else {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

        private void displayProfileImage() {
            StorageReference imageRef = storageReferenceFolder.child("profile_image.jpg");
            Glide.with(requireContext())
                    .load(imageRef) // image ref
                    .listener(glideRequestListener) // Attach the listener here
                    .skipMemoryCache(true) // Disable caching in memory
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                    .into(profileImage);  // imageview object
        }

    private void displayTodayImage() {
        StorageReference imageRef = storageReferenceFolder.child("today_image.jpg");
        Glide.with(requireContext())
                .load(imageRef) // image ref
                .listener(glideRequestListener) // Attach the listener here
                .skipMemoryCache(true) // Disable caching in memory
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                .into(todayImage);  // imageview object
    }
    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            StorageReference fileRef = storageReferenceFolder.child("profile_image." + getFileExtension(filePath));
            fileRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle successful upload
                        Toast.makeText(requireActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        displayProfileImage();
                    })
                    .addOnFailureListener(e -> {
                                    // Handle upload failure
                                    Toast.makeText(requireActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
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
            Log.d("GlideSucess", "Image Load Successful" );
            return false;
        }
    };

    private void setName() {

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            // Access fields in the data map
                            userName.setText((String) data.get("first_name") + " " + (String) data.get("last_name"));

                        }
                    } else {
                        Log.d("TAG", "Document Does not Exist");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("Firestore", "Error getting document", e);
                });
    }

}