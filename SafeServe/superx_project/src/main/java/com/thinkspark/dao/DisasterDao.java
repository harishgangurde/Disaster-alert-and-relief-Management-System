package com.thinkspark.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import com.google.cloud.firestore.DocumentChange;
import com.thinkspark.model.Disaster;
import com.thinkspark.model.FirebaseConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors; 

public class DisasterDao {

    private final Firestore db;

    public DisasterDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in DisasterDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    public void saveDisasterReport(Disaster disaster) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot save report.");
            return;
        }

        Map<String, Object> disasterData = new HashMap<>();
        disasterData.put("type", disaster.getType());
        disasterData.put("location", disaster.getLocation());
        disasterData.put("description", disaster.getDescription());
        disasterData.put("citizenName", disaster.getCitizenName());
        disasterData.put("imageUrl", disaster.getImageUrl());
        disasterData.put("status", disaster.getStatus());
        disasterData.put("timestamp", FieldValue.serverTimestamp());

        ApiFuture<DocumentReference> future = db.collection("disasters").add(disasterData);

        future.addListener(() -> {
            try {
                System.out.println("DAO Success: Disaster report saved to Firebase with new ID: " + future.get().getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("DAO Error: Failed to save disaster report to Firebase. " + e.getMessage());
                e.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor());
    }

    public void updateDisasterStatus(String disasterId, String newStatus) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot update disaster status.");
            return;
        }
        db.collection("disasters").document(disasterId).update("status", newStatus)
            .addListener(() -> {
                System.out.println("Disaster report " + disasterId + " status updated to " + newStatus);
            }, Executors.newSingleThreadExecutor()); 
    }

    public void listenForDisasterReports(ObservableList<Disaster> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for disaster reports.");
            return;
        }

        db.collection("disasters").orderBy("timestamp", Query.Direction.DESCENDING).limit(10)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for disaster reports failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Disaster disaster = dc.getDocument().toObject(Disaster.class);
                        disaster.setFirebaseDocId(dc.getDocument().getId()); 
                        disaster.setStatus(dc.getDocument().getString("status"));

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(disaster);
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getFirebaseDocId().equals(disaster.getFirebaseDocId())) {
                                        targetList.set(i, disaster);
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(d -> d.getFirebaseDocId().equals(disaster.getFirebaseDocId()));
                                break;
                        }
                    }
                });
            });
    }

    public void listenForDisastersByCitizen(ObservableList<Disaster> targetList, String citizenName) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for disaster reports.");
            return;
        }

        db.collection("disasters")
            .whereEqualTo("citizenName", citizenName)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for citizen's disaster reports failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    // Process only the changes
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Disaster disaster = dc.getDocument().toObject(Disaster.class);
                        disaster.setFirebaseDocId(dc.getDocument().getId());
                        disaster.setStatus(dc.getDocument().getString("status")); 

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(disaster);
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getFirebaseDocId().equals(disaster.getFirebaseDocId())) {
                                        targetList.set(i, disaster);
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(d -> d.getFirebaseDocId().equals(disaster.getFirebaseDocId()));
                                break;
                        }
                    }
                });
            });
    }
}