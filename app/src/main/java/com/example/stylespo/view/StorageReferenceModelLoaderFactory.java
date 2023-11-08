package com.example.stylespo.view;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class StorageReferenceModelLoaderFactory implements ModelLoaderFactory<StorageReference, InputStream> {

    @Override
    public ModelLoader<StorageReference, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new StorageReferenceModelLoader();
    }

    @Override
    public void teardown() {
        // No cleanup required.
    }
}

