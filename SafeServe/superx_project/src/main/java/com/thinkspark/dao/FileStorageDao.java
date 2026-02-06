package com.thinkspark.dao;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.thinkspark.model.FirebaseConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FileStorageDao {

    private static final String BUCKET_NAME = "safe-serve-5065f.firebasestorage.app"; 

    private Storage storage;

    public FileStorageDao() {
        try {
            FirebaseConfig.initialize();
            FileInputStream serviceAccount = new FileInputStream("src\\main\\resources\\safe-serve-private.json");
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                    .getService();
        } catch (IOException e) {
            System.err.println("Error initializing Firebase Storage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String uploadFile(File file) {
        if (storage == null) {
            System.err.println("Firebase Storage is not initialized.");
            return null;
        }

        try {
            String blobName = "disaster_reports/" + UUID.randomUUID().toString() + "_" + file.getName();
            BlobId blobId = BlobId.of(BUCKET_NAME, blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            storage.create(blobInfo, Files.readAllBytes(file.toPath()));

            return storage.get(blobId).signUrl(365, TimeUnit.DAYS).toString();
        } catch (IOException e) {
            System.err.println("Error uploading file to Firebase Storage: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}