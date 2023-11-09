package com.example.stylespo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderGetKt;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.example.stylespo.databinding.FragmentSignUpBinding;
import com.example.stylespo.model.User;
import com.example.stylespo.viewmodel.MainViewModel;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    private MainViewModel mMainViewModel;
    private Button mSignUpButton;
    private Button mLoginButton;
    private User userModel;
    private FragmentSignUpBinding binding;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mDOB;

    private  TextView mEmail;
    private TextView mPassword;
    private TextView mConfirmPassword;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userID;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();
        mMainViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainViewModel.class);
        mMainViewModel.getUserDetails().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User userModel) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }

                }
        );
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mSignUpButton = v.findViewById(R.id.sign_up_button);
        mLoginButton = v.findViewById(R.id.log_on_button);
        mDOB = v.findViewById(R.id.dob);
        mPassword = v.findViewById(R.id.password);
        mEmail = v.findViewById(R.id.email);
        mFirstName = v.findViewById(R.id.first_name);
        mLastName = v.findViewById(R.id.last_name);
        mConfirmPassword = v.findViewById(R.id.confirm_password);
        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        return v;
    }

    public void onStop(){
        super.onStop();

    }

    public void onPause(){
        super.onPause();
    }


    public void onClick(View v) {
        final int viewId = v.getId();
        System.out.println(v.getId());
        System.out.println(R.id.sign_up_button);
        if (viewId == R.id.sign_up_button) {
            if(TextUtils.isEmpty(String.valueOf(mEmail.getText()))){
                Toast.makeText(getActivity(),"Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(String.valueOf(mPassword.getText()))){
                Toast.makeText(getActivity(),"Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!mPassword.getText().toString().trim().equals(mConfirmPassword.getText().toString().trim())){
                Toast.makeText(getActivity(),"Password is not same as confirm password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(),mPassword.getText().toString())
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Authentication passes.",
                                        Toast.LENGTH_SHORT).show();
                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = db.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("DOB", mDOB.getText().toString());
                                user.put("email", mEmail.getText().toString());
                                user.put("first_name", mFirstName.getText().toString());
                                user.put("last_name", mLastName.getText().toString());
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG","On Success created for "+userID);
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG","On Failed created for "+userID);
                                            }
                                        });
//                                FragmentManager fm = getActivity().getSupportFragmentManager();
//                                FragmentTransaction transaction = fm.beginTransaction();
//                                Fragment fragment = new HomepageFragment();
//                                transaction.setReorderingAllowed(true);
//                                transaction.replace(R.id.signUp_frag_container, fragment).commit();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if (viewId == R.id.log_on_button) {
                Toast.makeText(getActivity(), "Navigating to login page", Toast.LENGTH_SHORT).show();
                Fragment newFragment = new LoginFragment(); // Instantiate the new fragment
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.signUp_frag_container, newFragment);
                transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
                transaction.commit();

            }
    }
}