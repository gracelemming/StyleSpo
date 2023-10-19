package com.example.stylespo.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.HomePage;
import com.example.stylespo.R;
import com.example.stylespo.viewmodel.SignUpViewModel;
import com.google.firebase.auth.FirebaseUser;


public class SignUpFragment extends Fragment {

    private EditText emailEdit, passEdit;
    private TextView loginText;
    private Button log_in_button;
    private SignUpViewModel viewModel;
    private NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(SignUpViewModel.class);
        viewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            public void onChanged(FirebaseUser firebaseUser) {
              //  if(firebaseUser != null) {
                //}
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEdit = view.findViewById(R.id.username);
        passEdit = view.findViewById(R.id.password);

        navController = Navigation.findNavController(view);
         log_in_button = view.findViewById(R.id.log_in_button);

        log_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String email = emailEdit.getText().toString();
               String password =  passEdit.getText().toString();

               if(!email.isEmpty() && !password.isEmpty()) {
                   viewModel.register(email, password);
               }

            }

        });
    }

}