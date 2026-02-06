package com.thinkspark.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.User;
import com.thinkspark.model.UserService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class SignUpController {

    public Result signUp(String name, String email, String password, String role) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty() || role == null || role.isEmpty()) {
            return new Result(false, "All fields are required and cannot be empty");
        }
        try {
            FirebaseConfig.initialize();

            String apiKey = "AIzaSyAZ0tYGpqtLb3OvVNj0RROf4eDMx5Uk330";
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
            jsonRequest.put("returnSecureToken", true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequest.toString().getBytes());
            }

            if (connection.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                String uid = jsonResponse.getString("localId");

                Map<String, Object> claims = new HashMap<>();
                claims.put("role", role);
                FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);

                UserService userService = new UserService();
                User newUser = new User(name, email, role);
                boolean isRegistered = userService.registerUser(newUser);

                if (isRegistered) {
                    return new Result(true, "SignUp Successful");
                } else {
                    return new Result(false, "SignUp successful, but failed to save user data.");
                }
            } else {
                return new Result(false, "SignUp Unsuccessful: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "SignUp Unsuccessful: " + e.getMessage());
        }
    }

    public static class Result {
        public Boolean success;
        public String message;

        public Result(Boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}