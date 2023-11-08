package com.example.stylespo.view;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.IOException;
import java.io.InputStream;

public class StorageReferenceFetcher implements DataFetcher<InputStream> {

    private final StorageReference storageReference;

    public StorageReferenceFetcher(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    @Override
    public void loadData(
            @NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        try {
            // Load the data from the StorageReference and provide it to the callback.
            // Get an InputStream from a StorageReference
            storageReference.getStream()
                    .addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(StreamDownloadTask.TaskSnapshot taskSnapshot) {
                            // Get the InputStream from the taskSnapshot
                            InputStream inputStream = taskSnapshot.getStream();

                            // Handle the InputStream here
                            callback.onDataReady(inputStream);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occur during the operation
                            Log.e("StorageReferenceFetcher", "Error loading data from StorageReference", e);
                        }
                    });

        } catch (Exception e) {
            // Handle any loading errors here.
            Log.e("StorageReferenceFetcher", "Error loading data from StorageReference", e);
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        // Cleanup any resources if needed.
    }

    @Override
    public void cancel() {
        // Cancel the loading operation if needed.
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}

