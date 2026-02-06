package com.thinkspark.model;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseConfig {

    private static Firestore db = null;

    public static Firestore initialize() throws IOException {
        if (db == null) {
            String serviceAccountKeyPath = "src/main/resources/safe-serve-private.json";

            FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase is initialized");
            db = FirestoreClient.getFirestore();
        }
        return db;
    }

    public static Firestore getDb() {
        return db;
    }
}
