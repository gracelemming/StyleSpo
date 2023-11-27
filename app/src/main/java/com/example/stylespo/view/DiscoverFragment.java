package com.example.stylespo.view;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.stylespo.R;
import com.example.stylespo.viewmodel.DiscoverViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class DiscoverFragment extends Fragment {


    private DiscoverViewModel viewModel;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    String userID;
    private RecyclerView recyclerView;
    private DiscoverAdapter adapter;
    private SearchView searchView;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        Glide.get(requireContext()).getRegistry().prepend(StorageReference.class, InputStream.class, new StorageReferenceModelLoaderFactory());
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            v = inflater.inflate(R.layout.fragment_discover, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_discover_land, container, false);
        }

        recyclerView = v.findViewById(R.id.recycler_view_discover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        viewModel.fetchImageList();


        // Observe LiveData and update the adapter when data changes
        viewModel.getImageListLiveData().observe(getViewLifecycleOwner(), imageList -> {
            adapter = new DiscoverAdapter(filterCurrentUserPosts(imageList, userID), userID);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        });


        searchView = v.findViewById(R.id.searchView);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search based on the entered tag
                //viewModel.searchImagesByTag(query);
                viewModel.fetchImageList(query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes while typing


                //viewModel.searchImagesByTag(newText);
                viewModel.fetchImageList(newText);
                return false;
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




    class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {


        private final List<UserImageField> imageList;
        private FirebaseStorage storage = FirebaseStorage.getInstance();
        private StorageReference storageReference = storage.getReference();
        String currentUserID;


        public DiscoverAdapter(List<UserImageField> imageList, String currentUserID) {
            this.imageList = imageList;
            this.currentUserID = currentUserID;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            List<ImageView> userImageViews;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userImageViews = new ArrayList<>();


                userImageViews.add(itemView.findViewById(R.id.image));
            }
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout_for_discover, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UserImageField userImageField = imageList.get(position);


            for (int i = 0; i < holder.userImageViews.size(); i++) {
                ImageView imageView = holder.userImageViews.get(i);
                loadImage(imageView, userImageField.getTodayImageRes(), userImageField.getUserID());
            }
        }






        private void loadImage(ImageView imageView, String imagePath, String userId) {
            if (imagePath != null) {
                StorageReference imageRef = storageReference.child(imagePath);
                Glide.with(requireContext())
                        .load(imageRef)
                        .listener(glideRequestListener)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDiscoverImageClick(view,userId );
                }
            });
        }


        private void onDiscoverImageClick(View view, String userId) {
            // ...


            Toast.makeText(getContext(), "User ID: " + currentUserID + "user id", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
            intent.putExtra("userId", userId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // ...
        }


        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }




    // Glide request listener for error handling
    private RequestListener<Drawable> glideRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            // Handle the load failure here
            // Log.e("GlideError", "Load failed: " + e.getMessage());
            return false; // Return false to allow Glide to handle the error, or true to indicate that you've handled it.
        }


        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            // Image loaded successfully
            return false;
        }
    };
}

