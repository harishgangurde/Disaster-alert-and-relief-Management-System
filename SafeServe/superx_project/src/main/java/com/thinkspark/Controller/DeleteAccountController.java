package com.thinkspark.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.thinkspark.model.FirebaseConfig;

import java.io.IOException;

public class DeleteAccountController {

    public boolean deleteAccount(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Email cannot be null or empty.");
            return false;
        }

        try {
            FirebaseConfig.initialize();

            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            String uid = userRecord.getUid();

            FirebaseAuth.getInstance().deleteUser(uid);

            System.out.println("Successfully deleted user: " + email);
            return true;
        } catch (FirebaseAuthException e) {
            System.err.println("Firebase Auth error while deleting account: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while deleting the account.");
            e.printStackTrace();
            return false;
        }
    }
}