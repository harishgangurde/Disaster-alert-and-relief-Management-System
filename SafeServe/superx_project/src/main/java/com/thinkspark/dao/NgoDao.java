package com.thinkspark.dao;

import com.google.cloud.firestore.Firestore;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.view.ManageNgosView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import com.google.cloud.firestore.DocumentChange;


import java.io.IOException;

public class NgoDao {

    private final Firestore db;

    public NgoDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in NgoDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    public void listenForNgos(ObservableList<ManageNgosView.NgoItem> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for NGOs.");
            return;
        }

        db.collection("users").whereEqualTo("role", "NGO")
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for NGOs failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        ManageNgosView.NgoItem ngoItem = new ManageNgosView.NgoItem(
                            dc.getDocument().getString("name"),
                            dc.getDocument().getString("status"), 
                            dc.getDocument().getString("email")
                        );

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(ngoItem);
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getContact().equals(ngoItem.getContact())) { 
                                        targetList.set(i, ngoItem);
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(n -> n.getContact().equals(ngoItem.getContact())); 
                                break;
                        }
                    }
                });
            });
    }
}