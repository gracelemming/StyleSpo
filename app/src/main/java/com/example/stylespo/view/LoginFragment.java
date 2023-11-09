package com.example.stylespo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.example.stylespo.model.User;
import com.example.stylespo.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private MainViewModel mMainViewModel;
    private Button mSignUpButton;
    private Button mLoginButton;
    private Button mForgotPasswordButton;
    private TextView mPassword;
    private  TextView mEmail;
    private User userModel;
    private FirebaseAuth mAuth;
    public LoginFragment() {
        // Required empty public constructor
    }




    public void setEnabled(boolean hidden) {
        mSignUpButton.setEnabled(hidden);
        mLoginButton.setEnabled(hidden);
        mForgotPasswordButton.setEnabled(hidden);
        mPassword.setEnabled(hidden);
        mEmail.setEnabled(hidden);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            setEnabled(false);
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);

        }else {
            onHiddenChanged(true);
        }
    }

//    public void onStop(){
//        super.onStop();
//    }
//
//    public void onPause(){
//        super.onPause();
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();
        mMainViewModel = new ViewModelProvider((ViewModelStoreOwner)activity).get(MainViewModel.class);
        mMainViewModel.getUserDetails().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User userModel){
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }

                }
        );
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false);
        //view=binding.getRoot();
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mSignUpButton = v.findViewById(R.id.makeAccount);
        mLoginButton = v.findViewById(R.id.log_in_button);
        mForgotPasswordButton = v.findViewById(R.id.forgot_password);
        mPassword = v.findViewById(R.id.password);
        mEmail = v.findViewById(R.id.email);
        setEnabled(true);
        mForgotPasswordButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        final int viewId = v.getId();
        if(viewId == R.id.makeAccount) {
            Toast.makeText(getActivity(), "Navigating to signup page", Toast.LENGTH_SHORT).show();
            setEnabled(false);
            Fragment newFragment = new SignUpFragment(); // Instantiate the new fragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.login_frag_container, newFragment);
            transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
            transaction.commit();
        } else if (viewId == R.id.log_in_button) {
            if(TextUtils.isEmpty(String.valueOf(mEmail.getText()))){
                Toast.makeText(getActivity(),"Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(String.valueOf(mPassword.getText()))){
                Toast.makeText(getActivity(),"Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(mEmail.getText().toString(),mPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Login successfull.",
                                        Toast.LENGTH_SHORT).show();
                                setEnabled(false);
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "login failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } else if (viewId == R.id.forgot_password) {
            setEnabled(false);
            Toast.makeText(getActivity(), "Navigating to forgot password page", Toast.LENGTH_SHORT).show();
            Fragment newFragment = new ForgotPasswordFragment(); // Instantiate the new fragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.login_frag_container, newFragment);
            transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
            transaction.commit();
        }
    }
}

//for onDestroy just call super

//jetpack nav
//viewbinding more than we need