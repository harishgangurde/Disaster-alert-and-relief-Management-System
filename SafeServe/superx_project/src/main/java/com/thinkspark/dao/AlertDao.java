package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.thinkspark.model.Alert;
import com.thinkspark.model.FirebaseConfig;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import com.google.cloud.firestore.DocumentChange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class AlertDao {

    private final Firestore db;

   
    public AlertDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in AlertDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    public void saveAlert(Alert alert) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot save alert.");
            return;
        }

        Map<String, Object> alertData = new HashMap<>();
        alertData.put("title", alert.getTitle());
        alertData.put("message", alert.getMessage());
        alertData.put("priority", alert.getPriority());
        alertData.put("timestamp", FieldValue.serverTimestamp());

        ApiFuture<DocumentReference> future = db.collection("alerts").add(alertData);

        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Alert saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save alert to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Runnable::run);
    }

 
    public void listenForAlerts(ObservableList<Alert> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for alerts.");
            return;
        }

        db.collection("alerts").orderBy("timestamp", Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for alerts failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Alert alert = dc.getDocument().toObject(Alert.class);

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(alert);
                                break;
                            case MODIFIED:
                              
                                break;
                            case REMOVED:
                               
                                break;
                        }
                    }
                });
            });
    }
}