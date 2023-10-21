package com.example.stylespo.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.example.stylespo.databinding.FragmentSignUpBinding;
import com.example.stylespo.model.UserAccount;
import com.example.stylespo.viewmodel.MainViewModel;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    private MainViewModel mMainViewModel;
    private Button mSignUpButton;
    private Button mLoginButton;
    private TextView mTextView;
    private UserAccount userAccountModel;
    private FragmentSignUpBinding binding;
    private TextView mUsername;
    private TextView mPassword;

    private NavController navController;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();
        mMainViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(MainViewModel.class);
        mMainViewModel.getUserDetails().observe(this, new Observer<UserAccount>() {
                    @Override
                    public void onChanged(UserAccount userAccountModel) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }

                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        navController = NavHostFragment.findNavController(this);
        mSignUpButton = v.findViewById(R.id.sign_up_button);
        mLoginButton = v.findViewById(R.id.log_on_button);
        mUsername = v.findViewById(R.id.username);
        mPassword = v.findViewById(R.id.password);
        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        return v;
    }

    /*@Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        navController = Navigation.findNavController(v);
        findViewById<Button>(R.id.sign_up_button).setOnClickListener(this);
    }
*/

    public void onClick(View v) {
        final int viewId = v.getId();
        System.out.println(v.getId());
        System.out.println(R.id.sign_up_button);
        if (viewId == R.id.sign_up_button) {
            if (mUsername.getText().toString().equals("admin") && mPassword.getText().toString().equals("admin")) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();

                user.put("username" ,mUsername.getText().toString());
                user.put("password", mPassword.getText().toString());

                // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity(), "Datastore SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Datastore FAILED", Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(getActivity(), "Signup Successful", Toast.LENGTH_SHORT).show();
           /* NavHostFragment.findNavController(this)
                    .navigate(R.id.action_signUpFragment_to_HomepageFragment);*/
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = new HomepageFragment();
                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.signUp_frag_container, fragment).commit();
            } else if (viewId == R.id.log_on_button) {
                //navcontroller here
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signUpFragment_to_loginFragment);

            }
        }
    }
}