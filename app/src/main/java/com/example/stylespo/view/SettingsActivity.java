package com.example.stylespo.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.stylespo.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onSendPasswordResetEmail(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password reset email sent. Check your email.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User not signed in, handle accordingly
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdateFirstNameClick(View view) {
        // Replace with the logic to open a dialog or activity to let the user enter a new first name
        // You can use an AlertDialog, EditText, or another UI component for input
        // After obtaining the new first name, update it as follows:

        EditText editFirstNameLayout = findViewById(R.id.newFirstNameInput);

        if (editFirstNameLayout.getVisibility() == View.VISIBLE) {
            editFirstNameLayout.setVisibility(View.GONE); // Hide the layout
        } else {
            editFirstNameLayout.setVisibility(View.VISIBLE); // Show the layout
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String newFirstName = editFirstNameLayout.getText().toString(); // Get the new first name from the EditText

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newFirstName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "First name updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update first name", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User not signed in, handle accordingly
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDeleteAccountClick(View view) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), "user_password"); // Replace with the user's actual password
            user.reauthenticate(credential)
                    .addOnCompleteListener(reauthTask -> {
                        if (reauthTask.isSuccessful()) {
                            // User has been reauthenticated, so you can now safely delete the account.
                            user.delete()
                                    .addOnCompleteListener(deleteTask -> {
                                        if (deleteTask.isSuccessful()) {
                                            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                            finish(); // Close the settings activity or navigate to the login screen
                                        } else {
                                            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Reauthentication failed. Handle the failure.
                            Toast.makeText(this, "Reauthentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User not signed in, handle accordingly
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null) {
//            user.delete()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            String errorMessage = task.getException().getMessage();
//                            Toast.makeText(this, "Failed to delete account: " + errorMessage, Toast.LENGTH_SHORT).show();
//                            Log.e("TAG", "Failed to delete account: " + errorMessage);
//                        }
//                    });
//        } else {
//            // User not signed in, handle accordingly
//            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
//        }
    }
}