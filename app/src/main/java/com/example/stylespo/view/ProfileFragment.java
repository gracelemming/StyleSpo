package com.example.stylespo.view;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageButton;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment  implements View.OnClickListener {

    ImageButton showPopupButton;
    TextView userName;

    TextView firstName;
    ImageButton deletePostButton;
    ImageView profileImage;
    ImageView todayImage;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageReferenceFolder;
    private FirebaseAuth mAuth;

    TextView friendCount;
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

        if (userID != null && !userID.isEmpty()) {
            storageReferenceFolder = storageReference.child(userID);
        } else {
            // Handle the case where userID is null or empty
            Log.e("ProfileFragment", "userID is null or empty");
        }

        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_profile_land, container, false);
        }
        showPopupButton = (ImageButton) rootView.findViewById(R.id.drop_down_button);
        deletePostButton = (ImageButton) rootView.findViewById(R.id.delete_post_button);
        profileImage = rootView.findViewById(R.id.profile_image);
        todayImage = rootView.findViewById(R.id.today_image);
        showPopupButton.setOnClickListener(this);
        deletePostButton.setOnClickListener(this);

        profileImage.setOnClickListener(this);
        userName = rootView.findViewById(R.id.username);
        firstName = rootView.findViewById(R.id.first_name);
        friendCount = rootView.findViewById(R.id.friend_count);
        friendCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on friendCount, navigate to FriendsListActivity
                Intent intent = new Intent(getActivity(), FriendsListActivity.class);
                startActivity(intent);
            }
        }); setName();
        setUserName();
        displayProfileImage();
        displayTodayImage();
        updateFriendCount();
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

    private void updateFriendCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getUid();

        // Reference to the "friends" subcollection of the current user
        CollectionReference friendsCollection = db.collection("friends_list")
                .document(currentUserId)
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

//        // Reference to the "friends" subcollection of the current user
//        CollectionReference friendsCollection = db.collection("friends_list")
//                .document(currentUserId)
//                .collection("friends");
//
//        // Query to get only friends with "status" as "accepted"
//        friendsCollection.whereEqualTo("status", "accepted")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    // Get the count of friends with "status" as "accepted" and update the UI
//                    int count = queryDocumentSnapshots.size();
//                    friendCount.setText(String.valueOf(count));
//                })
//                .addOnFailureListener(e -> {
//                    // Handle error
//                    Toast.makeText(requireContext(), "Error fetching friend count", Toast.LENGTH_SHORT).show();
//                });
    }


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

    private void deleteTodayImage(StorageReference imageRef) {
        imageRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Image deleted successfully
                    Toast.makeText(requireContext(), "image deleted successfully", Toast.LENGTH_SHORT).show();
                    // You may want to update the UI or take other actions after deleting the image
                })
                .addOnFailureListener(e -> {
                    // Error deleting image
                    Toast.makeText(requireContext(), "Failed to delete profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayTodayImage() {
        StorageReference imageRef = storageReferenceFolder.child("today_image.jpg");
        Glide.with(requireContext())
                .load(imageRef) // image ref
                .listener(glideRequestListener) // Attach the listener here
                .skipMemoryCache(true) // Disable caching in memory
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                .into(todayImage);  // imageview object
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // TodayImage exists, set up deletePostButton click listener
            deletePostButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete your profile image?");

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // User clicked Yes, proceed with image deletion
                    deleteTodayImage(imageRef);
                    deletePostButton.setVisibility(v.INVISIBLE);
                    deletePostButton.setEnabled(false);
                });

                builder.setNegativeButton("No", (dialog, which) -> {
                    // User clicked No, cancel the deletion action
                    dialog.dismiss();
                });

                // Show the dialog
                builder.show();
            });

        }).addOnFailureListener(e -> {
            // TodayImage does not exist, hide deletePostButton
            deletePostButton.setVisibility(View.INVISIBLE);
            deletePostButton.setEnabled(false);
        });
    }
    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);

                // Create a temporary file to store the compressed image
                File compressedFile = createImageFile();

                // Write the resized bitmap to the temporary file
                FileOutputStream fos = new FileOutputStream(compressedFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
                fos.close();

                // Get the URI of the compressed image
                Uri compressedUri = Uri.fromFile(compressedFile);

                StorageReference fileRef = storageReferenceFolder.child("profile_image." + getFileExtension(filePath));
                fileRef.putFile(compressedUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Handle successful upload
                            Toast.makeText(requireActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            displayProfileImage();
                        })
                        .addOnFailureListener(e -> {
                            // Handle upload failure
                            Toast.makeText(requireActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireActivity(), "Error compressing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    private File createImageFile() throws IOException {
        // Create a unique file name for the compressed image
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_COMPRESSED_" + timestamp + ".jpg";

        // Create a file to store the compressed image
        File compressedFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

        return compressedFile;
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

    private void setUserName() {

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            // Access fields in the data map
                            userName.setText((String) data.get("username"));
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

    private void setName() {

        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            // Access fields in the data map
                            firstName.setText((String) data.get("first_name"));
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