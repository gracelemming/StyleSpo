//package com.example.stylespo.view;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import com.example.stylespo.R;
//import com.example.stylespo.viewmodel.MainViewModel;
//import com.google.android.material.navigation.NavigationBarView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomepageFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HomepageFragment extends Fragment {
//
//    private MainViewModel mMainViewModel;
//    private FirebaseFirestore db;
//    private FirebaseAuth mAuth;
//    BottomNavigationView bottomNavigationView;
//
//    public HomepageFragment() {
//        // Required empty public constructor
//        //
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//    public void onStop(){
//        super.onStop();
//    }
//
//
//    public void onPause(){
//        super.onPause();
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
//        bottomNavigationView = v.findViewById(R.id.bottomNavigationView);
//
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item){
//                Fragment fragment = null;
//                int id = item.getItemId();
//                if (id == R.id.AddFragment) {
//                    fragment = new CameraFragment();
//                } else if (id == R.id.DiscoverFragment) {
//                    fragment = new DiscoverFragment();
//                } else if (id == R.id.ProfileFragment) {
//                    fragment = new ProfileFragment();
//                } else if (id == R.id.HomepageFragment) {
//                    fragment = new HomepageFragment();
//                }
//             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, fragment).commit();
//
//                return true;
//            }
//        });
//        return v;
//    }
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//}
//
//

package com.example.stylespo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stylespo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;


    public BottomNavigationFragment() {
        // Required empty public constructor
        //
    }

    public void onStop(){
        super.onStop();
    }


    public void onPause(){
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, new HomepageFragment()).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        bottomNavigationView = v.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();
                if (id == R.id.AddFragment) {

                    fragment = new CameraFragment();
                } else if (id == R.id.DiscoverFragment) {

                    fragment = new DiscoverFragment();
                } else if (id == R.id.ProfileFragment) {

                    fragment = new ProfileFragment();
                    // Hide or clear the ImageView in the ProfileFragment
                    // You can also set the image to null or an empty image resource
                    // randomUserImageView.setImageDrawable(null);
                } else if (id == R.id.HomepageFragment2) {
                    fragment = new HomepageFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homepage_frag_container, fragment).commit();
                return true;
            }
        });
        return v;
    }

}

