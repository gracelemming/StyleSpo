package com.example.stylespo.view;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

import java.io.InputStream;

public class StorageReferenceModelLoader implements ModelLoader<StorageReference, InputStream> {

    @NonNull
    @Override
    public LoadData<InputStream> buildLoadData(
            @NonNull StorageReference model, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(model), new StorageReferenceFetcher(model));
    }

    @Override
    public boolean handles(@NonNull StorageReference model) {
        return true;
    }
}
