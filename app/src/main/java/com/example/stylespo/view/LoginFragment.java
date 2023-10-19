package com.example.stylespo.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stylespo.R;
import com.example.stylespo.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private ViewModel mMainViewModel;
    private Button log_in_button;
    private TextView mTextView;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();
        mMainViewModel = new ViewModelProvider((ViewModelStoreOwner)activity).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onClick(View view){

    }
}