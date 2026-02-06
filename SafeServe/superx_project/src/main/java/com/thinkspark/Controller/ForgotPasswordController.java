package com.thinkspark.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.thinkspark.model.FirebaseConfig;

import java.io.IOException;

public class ForgotPasswordController {

    public boolean sendPasswordResetEmail(String email) {
        try {
            FirebaseConfig.initialize();
            FirebaseAuth.getInstance().generatePasswordResetLink(email);
            return true;
        } catch (FirebaseAuthException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}