package com.thinkspark.model;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserService {

    public UserService() {
        try {
            FirebaseConfig.initialize();
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }
    }

    public boolean registerUser(User user) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return false;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("role", user.getRole());
        if (user.getImageUrl() != null) {
            userData.put("imageUrl", user.getImageUrl());
        }

        // *** FIX: Add default status for new NGOs ***
        if ("NGO".equals(user.getRole())) {
            userData.put("status", "Pending Verification");
        }

        ApiFuture<WriteResult> future = FirebaseConfig.getDb().collection("users").document(user.getEmail()).set(userData);

        try {
            future.get();
            System.out.println("User data successfully saved to Firestore for: " + user.getEmail());
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error writing user data to Firestore: " + e.getMessage());
            return false;
        }
    }

    public User getUserByEmail(String email) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return null;
        }
        try {
            DocumentSnapshot document = FirebaseConfig.getDb().collection("users").document(email).get().get();
            if (document.exists()) {
                return document.toObject(User.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error fetching user data: " + e.getMessage());
        }
        return null;
    }

    public boolean updateUserProfileImage(String email, String imageUrl) {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore database is not initialized.");
            return false;
        }
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("imageUrl", imageUrl);
            FirebaseConfig.getDb().collection("users").document(email).update(updates).get();
            System.out.println("User profile image updated for: " + email);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error updating user profile image: " + e.getMessage());
            return false;
        }
    }
}