package com.example.stylespo.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    private DiscoverViewModel viewModel;
    private RecyclerView recyclerView;
    private DiscoverAdapter adapter;
    private SearchView searchView;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_discover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        viewModel.fetchImageList();

        // Observe LiveData and update the adapter when data changes
        viewModel.getImageListLiveData().observe(getViewLifecycleOwner(), imageList -> {
            adapter = new DiscoverAdapter(imageList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        });

        searchView = v.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search based on the entered tag
                //viewModel.searchImagesByTag(query);
                viewModel.fetchImageList(query.toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes while typing

                //viewModel.searchImagesByTag(newText);
                viewModel.fetchImageList(newText.toLowerCase());
                return false;
            }
        });

        return v;
    }

    class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

        private final List<UserImageField> imageList;
        private FirebaseStorage storage = FirebaseStorage.getInstance();
        private StorageReference storageReference = storage.getReference();

        public DiscoverAdapter(List<UserImageField> imageList) {
            this.imageList = imageList;
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
                loadImage(imageView, userImageField.getTodayImageRes());
            }
        }

        private void loadImage(ImageView imageView, String imagePath) {
            if (imagePath != null) {
                StorageReference imageRef = storageReference.child(imagePath);
                Glide.with(requireContext())
                        .load(imageRef)
                        .listener(glideRequestListener)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
            }
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
