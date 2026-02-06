package com.thinkspark.dao;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.Volunteer;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import com.google.cloud.firestore.DocumentChange;

import java.io.IOException;

public class VolunteerDao {

    private final Firestore db;

    public VolunteerDao() {
        Firestore initializedDb = null;
        try {
            initializedDb = FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("FATAL: Firestore could not be initialized in VolunteerDao. " + e.getMessage());
            e.printStackTrace();
        }
        this.db = initializedDb;
    }

    public void listenForVolunteers(ObservableList<Volunteer> targetList) {
        if (db == null) {
            System.err.println("Firestore is not initialized. Cannot listen for volunteers.");
            return;
        }

        db.collection("users").whereEqualTo("role", "Volunteer")
            .addSnapshotListener((snapshots, e) -> {
                if (e != null) {
                    System.err.println("Listen for volunteers failed: " + e);
                    return;
                }

                Platform.runLater(() -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Volunteer volunteer = dc.getDocument().toObject(Volunteer.class);

                        switch (dc.getType()) {
                            case ADDED:
                                targetList.add(volunteer);
                                break;
                            case MODIFIED:
                                for (int i = 0; i < targetList.size(); i++) {
                                    if (targetList.get(i).getEmail().equals(volunteer.getEmail())) { // Assuming email is unique identifier
                                        targetList.set(i, volunteer);
                                        break;
                                    }
                                }
                                break;
                            case REMOVED:
                                targetList.removeIf(v -> v.getEmail().equals(volunteer.getEmail())); // Assuming email is unique identifier
                                break;
                        }
                    }
                });
            });
    }
}