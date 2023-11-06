package com.example.stylespo.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.stylespo.R;
import com.example.stylespo.model.User;
import com.example.stylespo.viewmodel.MainViewModel;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {

    private MainViewModel mMainViewModel;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;

    public HomepageFragment() {
        // Required empty public constructor
        //
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void onStop(){
        super.onStop();
    }


    public void onPause(){
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        bottomNavigationView = v.findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                Fragment fragment = null;
                int id = item.getItemId();
                if (id == R.id.AddFragment) {
                    fragment = new AddFragment();
                } else if (id == R.id.DiscoverFragment) {
                    fragment = new DiscoverFragment();
                } else if (id == R.id.ProfileFragment) {
                    fragment = new ProfileFragment();
                } else if (id == R.id.HomepageFragment) {
                    fragment = new HomepageFragment();
                }
             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, fragment).commit();



                return true;
            }
        });
        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}


