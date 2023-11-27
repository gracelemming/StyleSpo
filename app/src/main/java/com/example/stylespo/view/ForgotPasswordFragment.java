package com.example.stylespo.view;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.stylespo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    private EditText mEmail;
    private Button mResetButton;
    private FirebaseAuth mAuth;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        }

        mEmail = v.findViewById(R.id.email_forgot);
        mResetButton = v.findViewById(R.id.reset_password_button);
        mResetButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reset_password_button) {
            String email = mEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(getActivity(), "Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                // You can navigate to a login page or handle it as per your UI flow.
                            } else {
                                Toast.makeText(getActivity(), "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
