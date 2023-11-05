package com.example.stylespo.view;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomepageFragment extends Fragment implements View.OnClickListener {

    FirebaseStorage storage;
    StorageReference storageReference;

    StorageReference storageReferenceFolder;

    // views for button
    private Button button_select, button_upload;

    // view for image view
    private ImageView imageView;

    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseAuth mAuth;
    String userID;

    public HomepageFragment() {
        // Required empty public constructor
        //
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageReferenceFolder = storageReference.child(userID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        button_select = v.findViewById(R.id.button_select);
        button_upload = v.findViewById(R.id.button_upload);;
        imageView = v.findViewById(R.id.image);
        button_upload.setOnClickListener(this);
        button_select.setOnClickListener(this);

        return v;
    }

    ActivityResultLauncher<String> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    // Handle the result here
                    // 'result' is the URI of the selected image
                    // Your logic to process the selected image
                    filePath = result;
                    imageView.setImageURI(filePath);

                }
            }
    );

    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.button_upload){
            uploadImage();
        } else if (viewId == R.id.button_select){
            someActivityResultLauncher.launch("image/*");
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (filePath != null) {
            StorageReference fileRef = storageReferenceFolder.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
            fileRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle successful upload
                        Toast.makeText(requireActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure
                        Toast.makeText(requireActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireActivity(), "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

}