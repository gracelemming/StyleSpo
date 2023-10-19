package com.example.stylespo.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderGetKt;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.example.stylespo.viewmodel.MainViewModel;


public class SignUpFragment extends Fragment {

    private ViewModel mMainViewModel;
    private Button mSignUpButton;
    private Button mLoginButton;
    private TextView mTextView;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //mMainViewModel =  new ViewModelProvider((ViewModelStoreOwner)activity).get(MainViewModel.class);
        Activity activity = requireActivity();
        mMainViewModel = new ViewModelProvider((ViewModelStoreOwner)activity).get(MainViewModel.class);
        mMainViewModel.getUserDetails().observe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mSignUpButton = v.findViewById(R.id.sign_up_button);
        return v;
    }

    public void onClick(View v) {
        final int viewId = v.getId();
        if(viewId == R.id.sign_up_button) {
            Toast.makeText(getActivity(), "Signup Successful", Toast.LENGTH_SHORT).show();
        }
    }
}