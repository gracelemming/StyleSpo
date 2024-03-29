package com.example.stylespo.view;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.stylespo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements View.OnClickListener {

    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference storageReferenceFolder;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    String userID;

    private Uri photoUri; // URI to store the captured photo

    Button postButton;
    Button reTakePhotoButton;
    ImageView image;
    EditText tag;

    public CameraFragment() {
        // Required empty public constructor
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
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            v = inflater.inflate(R.layout.fragment_add, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_add_land, container, false);
        }
        takeImage();
        postButton = v.findViewById(R.id.postButton);
        reTakePhotoButton = v.findViewById(R.id.reTakeImageButton);
        image = v.findViewById(R.id.image);
        tag = v.findViewById(R.id.addTag);
        postButton.setOnClickListener(this);
        reTakePhotoButton.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
    if(v.getId() == R.id.postButton){
        uploadImage(photoUri);
    }else if (v.getId() == R.id.reTakeImageButton){
        takeImage();
    }
    }

    private void takeImage(){

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

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // The photo has been successfully captured and saved in the specified URI
                    // You can now use the photoUri to access the captured image and save it in the database

                    // WANTED TO TRY TO ADD A PAGE WHERE USER CAN REVIEW THE PHOTO THEY TOOK
                    // Intent reviewintent = new Intent(MediaStore.ACTION_REVIEW);
                   // postReview.launch(reviewIntent);
                    image.setImageURI(photoUri);
                    Toast.makeText(requireContext(), "Got Photo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );
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

                // Upload the compressed image to Firebase Storage
                StorageReference fileRef = storageReferenceFolder.child("today_image.jpg");
                fileRef.putFile(compressedUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Handle successful upload
                            Toast.makeText(requireActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = db.collection("tags").document(userID);
                            Map<String, Object> images = new HashMap<>();
                            images.put("tag", tag.getText().toString().toLowerCase());
                            documentReference.set(images)
                                    .addOnSuccessListener(unused -> Log.d("TAG", "On Success Images created for " + userID))
                                    .addOnFailureListener(e -> {
                                        // Handle upload failure
                                        Toast.makeText(requireActivity(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireActivity(), "Error compressing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireActivity(), "No File Selected", Toast.LENGTH_SHORT).show();
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


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }



}
