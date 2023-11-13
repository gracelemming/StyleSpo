package com.example.stylespo.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stylespo.view.UserImageField;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomepageViewModel extends ViewModel {
    private MutableLiveData<List<UserImageField>> imageListLiveData = new MutableLiveData<>();

    public LiveData<List<UserImageField>> getImageListLiveData() {
        return imageListLiveData;
    }

    public void fetchImageList() {
        List<UserImageField> imageList = new ArrayList<>();

        CollectionReference userCollection = FirebaseFirestore.getInstance().collection("tags");
        userCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    String userId = document.getId();
                    UserImageField userImageField = new UserImageField(userId);
                    // Add userImageField to the list before fetching user info
                    imageList.add(userImageField);
                }
                // Fetch user info for each user in the list
                fetchUserInfoFromList(imageList);
                // Update LiveData with the list of UserImageField
                imageListLiveData.postValue(imageList);
            } else {
                Log.e("Firestore", "No documents found in the 'images' collection.");
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Firestore query failed: " + e.getMessage());
        });
    }

    private void fetchUserInfoFromList(List<UserImageField> imageList) {
        for (UserImageField userImageField : imageList) {
            fetchUserInfo(userImageField);
        }
    }

    private void fetchUserInfo(UserImageField userImageField) {
        CollectionReference userCollection = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userDocRef = userCollection.document(userImageField.getUserID());
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String userName = documentSnapshot.getString("first_name") + " " + documentSnapshot.getString("last_name");
                userImageField.setName(userName);
                // Update LiveData when user info is fetched
                imageListLiveData.postValue(imageListLiveData.getValue());
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Firestore query failed: " + e.getMessage());
        });
    }
}




