package com.thinkspark.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.json.JSONObject;

public class LoginController {

    public Result login(String email, String password, String selectedRole) {
        try {
            String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY"); // Your Web API Key
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + GEMINI_API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("email", email);
            jsonRequest.put("password", password);
            jsonRequest.put("returnSecureToken", true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonRequest.toString().getBytes("utf-8"));
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String idToken = jsonResponse.getString("idToken");

                String[] chunks = idToken.split("\\.");
                Base64.Decoder decoder = Base64.getUrlDecoder();
                String payload = new String(decoder.decode(chunks[1]));
                JSONObject payloadJson = new JSONObject(payload);

                if (payloadJson.has("role") && payloadJson.getString("role").equals(selectedRole)) {
                    return new Result(true, "Login Successful");
                } else {
                    return new Result(false, "Role mismatch or role not assigned. Please select the correct role.");
                }
            } else {
                return new Result(false, "Login Unsuccessful: Invalid credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "Login Unsuccessful: " + e.getMessage());
        }
    }
    public static class Result {
        public final boolean success;
        public final String message;
        public Result(boolean success, String message) { this.success = success; this.message = message; }
    }
}