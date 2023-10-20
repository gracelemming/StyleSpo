package com.example.stylespo.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.example.stylespo.model.User;
import com.example.stylespo.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private MainViewModel mMainViewModel;
    private Button mSignUpButton;
    private Button mLoginButton;
    private TextView mTextView;
    private User userModel;

    public LoginFragment() {
        // Required empty public constructor
    }

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
        );}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false);
        //view=binding.getRoot();
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mSignUpButton = v.findViewById(R.id.makeAccount);
        mLoginButton = v.findViewById(R.id.log_in_button);
        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        final int viewId = v.getId();
        if(viewId == R.id.makeAccount) {
            Toast.makeText(getActivity(), "Navigating to signup page", Toast.LENGTH_SHORT).show();

        } else if (viewId == R.id.log_in_button) {
            //navcontroller here

        }
    }
}

//for onDestroy just call super

//jetpack nav
//viewbinding more than we need