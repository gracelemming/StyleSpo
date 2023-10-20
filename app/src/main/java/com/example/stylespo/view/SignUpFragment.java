package com.example.stylespo.view;

import android.app.Activity;
import android.os.Bundle;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylespo.R;
import com.example.stylespo.databinding.FragmentSignUpBinding;
import com.example.stylespo.defaultNotUsing.FirstFragment;
import com.example.stylespo.model.User;
import com.example.stylespo.viewmodel.MainViewModel;


public class SignUpFragment extends Fragment implements View.OnClickListener{

    private MainViewModel mMainViewModel;
    private Button mSignUpButton;
    private Button mLoginButton;
    private TextView mTextView;
    private User userModel;
    private FragmentSignUpBinding binding;

    private NavController navController;

    public SignUpFragment() {
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

        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        navController = NavHostFragment.findNavController(this);
        mSignUpButton = v.findViewById(R.id.sign_up_button);
        mLoginButton = v.findViewById(R.id.log_on_button);
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
        if(viewId == R.id.sign_up_button) {
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