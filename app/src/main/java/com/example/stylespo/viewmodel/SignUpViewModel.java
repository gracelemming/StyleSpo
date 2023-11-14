package com.example.stylespo.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.stylespo.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpViewModel extends ViewModel {

    private MutableLiveData<User> userDetails = new MutableLiveData<>();

    public Map<String, Object> getUser(){
        User userModel = userDetails.getValue();

        Map<String, Object> user = new HashMap<>();
        user.put("DOB", userModel.getDOB());
        user.put("email", userModel.getEmail());
        user.put("first_name", userModel.getFirstName());
        user.put("last_name", userModel.getLastName());

        getUsernameAndUpdateBaseUsernameAndCount(userModel.getFirstName(), userModel.getLastName(), userModel);
        user.put("username", userModel.getUsername());

        return user;
    }

    public LiveData<User> getUserDetails() {
        return userDetails;
    }

    public void getUsernameAndUpdateBaseUsernameAndCount(String firstName, String lastName, User userModel) {
        // Combine first name and last name to generate the baseUsername
        String baseUsernameToCheck = (firstName + lastName).toLowerCase();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("base_username_and_count");
        // Reference to the document in Firestore containing baseUsernames and counts
        DocumentReference documentReference = collectionReference.document("temp");

        // Get the current count for the baseUsername
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document exists, get the current count
                    long currentCount = document.getLong(baseUsernameToCheck);

                    // Generate a unique username by appending the count to the baseUsername
                    String uniqueUsername = baseUsernameToCheck + currentCount;

                    // Update the count in Firestore
                    updateCountInFirestore(documentReference, baseUsernameToCheck, currentCount + 1);

                    // Set the unique username in the User model
                    userModel.setUsername(uniqueUsername);

                    // You can log or use the uniqueUsername as needed
                    Log.d("SignUpViewModel", "Unique Username: " + uniqueUsername);
                } else {
                    // Document does not exist, create a new document
                    createNewDocument(collectionReference, baseUsernameToCheck);

                    // Set the unique username in the User model
                    userModel.setUsername(baseUsernameToCheck + "1");

                    // You can log or use the uniqueUsername as needed
                    Log.d("SignUpViewModel", "Unique Username: " + userModel.getUsername());
                }
            } else {
                Log.e("SignUpViewModel", "Error getting document", task.getException());
            }
        });
    }

    private void createNewDocument(CollectionReference collectionReference, String baseUsernameToCheck) {
        // Create a new document with the baseUsername and set its count to 1
        Map<String, Object> data = new HashMap<>();
        data.put(baseUsernameToCheck, 1);

        collectionReference.document("temp")
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d("SignUpViewModel", "New document created successfully"))
                .addOnFailureListener(e -> Log.e("SignUpViewModel", "Error creating new document", e));
    }


    private void updateCountInFirestore(DocumentReference documentReference, String baseUsername, long newCount) {
        // Update the count for the given baseUsername in Firestore
        Map<String, Object> updateData = new HashMap<>();
        updateData.put(baseUsername, newCount);

        documentReference.update(updateData)
                .addOnSuccessListener(aVoid -> Log.d("SignUpViewModel", "Count updated successfully"))
                .addOnFailureListener(e -> Log.e("SignUpViewModel", "Error updating count", e));
    }



    public void setUserDetails(User user) {
        userDetails.setValue(user);
    }




}
