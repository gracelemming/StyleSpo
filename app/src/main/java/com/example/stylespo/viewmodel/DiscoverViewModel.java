package com.example.stylespo.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stylespo.view.UserImageField;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiscoverViewModel extends ViewModel {
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

    public void searchImagesByTag(String tag) {
        // Perform a query to fetch images based on the entered tag from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.whereArrayContains("tags.tag", tag)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<UserImageField> searchResults = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming you have a method to convert Firestore document to UserImageField
                            UserImageField userImageField = convertDocumentToUserImageField(document);
                            searchResults.add(userImageField);
                        }

                        // Update LiveData with the search results
                        imageListLiveData.setValue(searchResults);
                    } else {
                        // Handle errors
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    // Helper method to convert Firestore document to UserImageField
    private UserImageField convertDocumentToUserImageField(QueryDocumentSnapshot document) {
        // Implement the conversion logic based on your document structure
        // This is just a placeholder; replace it with your actual logic
        String todayImageRes = document.getString("todayImageRes");
        return new UserImageField(todayImageRes);
    }

    public void fetchImageList(String tag) {
        List<UserImageField> imageList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tagsCollection = db.collection("tags");

        // Convert the tag to lowercase for case-insensitive search
        String lowercaseTag = tag.toLowerCase();

        // Use whereGreaterThanOrEqualTo and whereLessThanOrEqualTo to simulate a substring search
        tagsCollection
                .whereGreaterThanOrEqualTo("tag", lowercaseTag)
                .whereLessThanOrEqualTo("tag", lowercaseTag + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String userId = document.getId();
                        UserImageField userImageField = new UserImageField(userId);
                        imageList.add(userImageField);
                    }

                    // Fetch user info for each user in the list
                    fetchUserInfoFromList(imageList);
                    // Update LiveData with the list of UserImageField
                    imageListLiveData.postValue(imageList);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore query failed: " + e.getMessage());
                });
    }


    private void fetchUserInfo(UserImageField userImageField) {
        CollectionReference userCollection = FirebaseFirestore.getInstance().collection("users");
        DocumentReference userDocRef = userCollection.document(userImageField.getUserID());
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String userName = documentSnapshot.getString("username");
                userImageField.setName(userName);
                // Update LiveData when user info is fetched
                imageListLiveData.postValue(imageListLiveData.getValue());
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Firestore query failed: " + e.getMessage());
        });
    }
}




