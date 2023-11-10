package com.example.stylespo.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.stylespo.viewmodel.DiscoverViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
  factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String userID;
    private DiscoverViewModel viewModel;
    private RecyclerView recyclerView;
    private YourDiscoverAdapter adapter;
    public DiscoverFragment() {
        // Required empty public constructor
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
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_discover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        viewModel.fetchImageList();

        // Observe LiveData and update the adapter when data changes
        viewModel.getImageListLiveData().observe(getViewLifecycleOwner(), new Observer<List<UserImageField>>() {
            @Override
            public void onChanged(List<UserImageField> imageList) {
                adapter = new YourDiscoverAdapter(imageList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
        return v;
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

    class YourDiscoverAdapter extends RecyclerView.Adapter<DiscoverFragment.YourDiscoverAdapter.ViewHolder> {

        private final List<UserImageField> imageList;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        public YourDiscoverAdapter(List<UserImageField> imageList) {
            this.imageList = imageList;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            List<ImageView> userImageViews;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userImageViews = new ArrayList<>();

                userImageViews.add(itemView.findViewById(R.id.image_1));
                userImageViews.add(itemView.findViewById(R.id.image_2));
                userImageViews.add(itemView.findViewById(R.id.image_3));
                userImageViews.add(itemView.findViewById(R.id.image_4));
                userImageViews.add(itemView.findViewById(R.id.image_5));
                userImageViews.add(itemView.findViewById(R.id.image_6));



                /*user1 = itemView.findViewById(R.id.image_1);
                user2 = itemView.findViewById(R.id.image_2);
                user3 = itemView.findViewById(R.id.image_3);
                user4 = itemView.findViewById(R.id.image_4);
                user5 = itemView.findViewById(R.id.image_5);
                user6 = itemView.findViewById(R.id.image_6);*/

            }
        }

        @NonNull
        @Override
        public DiscoverFragment.YourDiscoverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout_for_discover, parent, false);
            return new DiscoverFragment.YourDiscoverAdapter.ViewHolder(view);
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
        public void onBindViewHolder(@NonNull DiscoverFragment.YourDiscoverAdapter.ViewHolder holder, int position) {
            UserImageField userImageField = imageList.get(position);

            // Set image resources and other data to your views in ViewHolder
            for (int i = 0; i < holder.userImageViews.size(); i++) {
                StorageReference todayImageRef = storageReference.child(userImageField.getTodayImageRes());
                Glide.with(requireContext())
                        .load(todayImageRef) // image ref
                        .listener(glideRequestListener) // Attach the listener here
                        .skipMemoryCache(true) // Disable caching in memory
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable caching on disk
                        .into(holder.userImageViews.get(i));  // imageview object
            }
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }



}
