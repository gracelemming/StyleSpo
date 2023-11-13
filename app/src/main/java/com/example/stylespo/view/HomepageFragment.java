package com.example.stylespo.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.example.stylespo.viewmodel.HomepageViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String userID;
    private HomepageViewModel viewModel;
    private RecyclerView recyclerView;
    private YourAdapter adapter;

    public HomepageFragment() {
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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
        viewModel = new ViewModelProvider(this).get(HomepageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        viewModel.fetchImageList();

        // Observe LiveData and update the adapter when data changes
        viewModel.getImageListLiveData().observe(getViewLifecycleOwner(), new Observer<List<UserImageField>>() {
            @Override
            public void onChanged(List<UserImageField> imageList) {
                List<UserImageField> filteredImageList = filterCurrentUserPosts(imageList, userID);
                adapter = new YourAdapter(filteredImageList, userID);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        return v;
    }

    private List<UserImageField> filterCurrentUserPosts(List<UserImageField> imageList, String currentUserID) {
        List<UserImageField> filteredList = new ArrayList<>();
        for (UserImageField userImageField : imageList) {
            if (!userImageField.getUserID().equals(currentUserID)) {
                filteredList.add(userImageField);
            }
        }
        return filteredList;
    }


    private RequestListener<Drawable> glideRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            // Handle the load failure here
            Log.e("GlideError", "Load failed: " + e.getMessage());
            return false; // Return false to allow Glide to handle the error, or true to indicate that you've handled it.
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            // Image loaded successfully
            return false;
        }
    };




    class YourAdapter extends RecyclerView.Adapter<YourAdapter.ViewHolder> {

        private final List<UserImageField> imageList;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        String currentUserID;
        public YourAdapter(List<UserImageField> imageList, String currentUserID) {
            this.imageList = imageList;
            this.currentUserID = currentUserID;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profileImage;
            TextView username;
            ImageView todayImage;

            String userID;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                profileImage = itemView.findViewById(R.id.profile_image);
                username = itemView.findViewById(R.id.username);
                todayImage = itemView.findViewById(R.id.today_image);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout_for_homepage, parent, false);
            return new ViewHolder(view);
        }
        private RequestListener<Drawable> glideRequestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                // Handle the load failure here
                Log.e("GlideError", "Load failed: " + e.getMessage());
                return false; // Return false to allow Glide to handle the error, or true to indicate that you've handled it.
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                // Image loaded successfully
                return false;
            }
        };

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UserImageField userImageField = imageList.get(position);
            holder.userID = userImageField.getUserID();

            // Set image resources and other data to your views in ViewHolder
            String profileImageRes = userImageField.getProfileImageRes();
            String todayImageRes = userImageField.getTodayImageRes();

            if (profileImageRes != null && todayImageRes != null) {
                StorageReference profileImageRef = storageReference.child(profileImageRes);
                Glide.with(requireContext())
                        .load(profileImageRef)
                        .listener(glideRequestListener)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.profileImage);

                holder.username.setText(userImageField.getName());

                StorageReference todayImageRef = storageReference.child(todayImageRes);
                Glide.with(requireContext())
                        .load(todayImageRef)
                        .listener(glideRequestListener)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.todayImage);

                holder.profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onProfileImageClick(view, holder.userID);
                    }
                });
            } else {
                // Handle the case where image paths are null
                Log.e("YourAdapter", "Image paths are null for position: " + position);
            }
        }

        // Inside HomepageFragment
        private void onProfileImageClick(View view, String userId) {
            // ...

            Toast.makeText(getContext(), "User ID: " + currentUserID + "user id", Toast.LENGTH_SHORT).show();

            if(userId.equals(currentUserID)){
                /*

                        Needs to switch to profile in navigation bar


//                BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
//                bottomNavigationView.getMenu().findItem(R.id.ProfileFragment).setChecked(true);

                Fragment newFragment = new ProfileFragment(); // Instantiate the new fragment
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homepage_frag_container, newFragment);
                transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack
                transaction.commit();*/
            }else{
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


            // ...
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }



}
