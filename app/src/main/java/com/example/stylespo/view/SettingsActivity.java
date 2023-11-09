package com.example.stylespo.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.stylespo.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onSendPasswordResetEmail(View view) {
        // Your existing code for sending password reset email
    }

    public void onUpdateFirstNameClick(View view) {
        LinearLayout updateFirstNameLayout = findViewById(R.id.updateFirstNameLayout);
        updateFirstNameLayout.setVisibility(View.VISIBLE);

        Button updateFirstNameButton = findViewById(R.id.updateFirstNameButton);
        updateFirstNameButton.setVisibility(View.GONE);
    }

    public void onSaveFirstNameClick(View view) {
        EditText editFirstNameLayout = findViewById(R.id.newFirstNameInput);
        String newFirstName = editFirstNameLayout.getText().toString();

        if (!newFirstName.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newFirstName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                                updateFirstNameInFirestore(user.getUid(), newFirstName);
                            } else {
                                Toast.makeText(this, "Failed to update first name", Toast.LENGTH_SHORT).show();
                            }
                        });

                LinearLayout updateFirstNameLayout = findViewById(R.id.updateFirstNameLayout);
                updateFirstNameLayout.setVisibility(View.GONE);

                Button updateFirstNameButton = findViewById(R.id.updateFirstNameButton);
                updateFirstNameButton.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter a valid first name", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFirstNameInFirestore(String userId, String newFirstName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.update("first_name", newFirstName)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "First name updated in Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update first name in Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    public void onDeleteAccountClick(View view) {
        LinearLayout deleteAccountLayout = findViewById(R.id.deleteAccountLayout);
        deleteAccountLayout.setVisibility(View.VISIBLE);

        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);
        deleteAccountButton.setVisibility(View.GONE);
    }

    public void onConfirmDeleteAccountClick(View view) {
        EditText enterPassword = findViewById(R.id.userPassword);
        String userPassword = enterPassword.getText().toString();

        if (!userPassword.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), userPassword);

                user.reauthenticate(credential)
                        .addOnCompleteListener(reauthTask -> {
                            if (reauthTask.isSuccessful()) {
                                deleteUserDocumentFromFirestore(user.getUid());
                                user.delete()
                                        .addOnCompleteListener(deleteTask -> {
                                            if (deleteTask.isSuccessful()) {
                                                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(this, "Reauthentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                            }
                        });

                LinearLayout deleteAccountLayout = findViewById(R.id.deleteAccountLayout);
                deleteAccountLayout.setVisibility(View.GONE);

                Button deleteAccountButton = findViewById(R.id.deleteAccountButton);
                deleteAccountButton.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUserDocumentFromFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User document deleted from Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete user document from Firestore", Toast.LENGTH_SHORT).show();
                });
    }
}
