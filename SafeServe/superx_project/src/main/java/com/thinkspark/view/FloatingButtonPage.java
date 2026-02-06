package com.thinkspark.view;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class FloatingButtonPage extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    private Scene previousScene = null;
    private Stage primaryStage;

    private VBox chatHistoryBox;
    private TextField messageInput;
    private ProgressIndicator loadingIndicator;
    private Label placeholderLabel;

    private static final String GEMINI_API_KEY = "AIzaSyC-WSo0_OCgUTU9zS_AOtJl98CCticuK_U"; 
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public FloatingButtonPage() {}

    public FloatingButtonPage(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

   @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // --- Header ---
        HBox header = new HBox();
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Label title = new Label("🤖 AI Chatbot");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Home");
        backButton.setFont(getFont(26, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10;");
        backButton.setPadding(new Insets(6, 12, 6, 12));
        backButton.setOnAction(e -> {
            if (previousScene != null) {
                primaryStage.setScene(previousScene);
                primaryStage.setTitle("Safe Serve");
            }
        });

        header.getChildren().addAll(title, spacer, backButton);
        root.setTop(header);

        chatHistoryBox = new VBox(20);
        chatHistoryBox.setPadding(new Insets(30));
        chatHistoryBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 12;");
        chatHistoryBox.setAlignment(Pos.TOP_CENTER);

        placeholderLabel = new Label("Ready when you are!");
        placeholderLabel.setFont(getFont(32, FontWeight.LIGHT));
        placeholderLabel.setTextFill(Color.web("#6C757D"));

        VBox placeholderContainer = new VBox(placeholderLabel);
        placeholderContainer.setAlignment(Pos.CENTER);
        placeholderContainer.setPrefHeight(800);
        chatHistoryBox.getChildren().add(placeholderContainer);

        ScrollPane chatScrollPane = new ScrollPane(chatHistoryBox);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(chatScrollPane);

        chatHistoryBox.heightProperty().addListener((obs, oldVal, newVal) -> chatScrollPane.setVvalue(1.0));

        messageInput = new TextField();
        messageInput.setPromptText("Type your message...");
        messageInput.setFont(getFont(22, FontWeight.NORMAL));
        messageInput.setPrefHeight(60);
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        Button sendButton = new Button("➤");
        sendButton.setFont(getFont(18, FontWeight.BOLD));
        sendButton.setTextFill(Color.WHITE);
        sendButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 24; -fx-cursor: hand;");
        sendButton.setPrefHeight(60);
        sendButton.setPrefWidth(60);

        sendButton.setOnMouseEntered(e -> sendButton.setStyle("-fx-background-color: #007BFF; -fx-background-radius: 24; -fx-cursor: hand;"));
        sendButton.setOnMouseExited(e -> sendButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 24; -fx-cursor: hand;"));

        sendButton.setOnAction(e -> sendMessage());
        messageInput.setOnAction(e -> sendMessage());

        HBox inputArea = new HBox(10, messageInput, sendButton);
        inputArea.setPadding(new Insets(15, 30, 115, 30));
        inputArea.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");
        inputArea.setAlignment(Pos.CENTER);
        root.setBottom(inputArea);

        Scene scene = new Scene(root, 1920, 1080);
        stage.setScene(scene);
        stage.setTitle("Safe Serve | AI Chatbot");
        stage.setResizable(true);
        stage.show();
    }


    private void sendMessage() {
        String userMessage = messageInput.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        if (placeholderLabel != null && chatHistoryBox.getChildren().contains(placeholderLabel.getParent())) {
            chatHistoryBox.getChildren().remove(placeholderLabel.getParent());
            placeholderLabel = null; 
            VBox.setVgrow(chatHistoryBox.getParent(), Priority.ALWAYS); 
        }
        displayMessage(userMessage, true);
        messageInput.clear();
        messageInput.setDisable(true);
        showLoading(true);

        new Thread(() -> {
            String aiResponse = "Error: Could not get response.";
            try {
                aiResponse = callGemini(userMessage);
            } catch (Exception e) {
                System.err.println("Gemini API Error: " + e.getMessage());
                e.printStackTrace();
                aiResponse = "Error: Failed to get response from AI. Please try again later. (Details: " + e.getMessage() + ")";
            }
            String finalAiResponse = aiResponse;
            Platform.runLater(() -> {
                showLoading(false);
                displayMessage(finalAiResponse, false);
                messageInput.setDisable(false);
                messageInput.requestFocus();
            });
        }).start();
    }

    private void displayMessage(String message, boolean isUser) {
        String timestamp = java.time.LocalTime.now().withNano(0).toString();

        Label messageLabel = new Label(message);
        messageLabel.setFont(getFont(24, FontWeight.NORMAL));
        messageLabel.setWrapText(true);
        messageLabel.setTextFill(isUser ? Color.WHITE : Color.web(FONT_COLOR_DARK));

        Label timeLabel = new Label(timestamp);
        timeLabel.setFont(Font.font("System", FontWeight.THIN, 10));
        timeLabel.setTextFill(Color.GRAY);

        VBox bubbleContent = new VBox(messageLabel, timeLabel);
        bubbleContent.setSpacing(4);
        bubbleContent.setPadding(new Insets(8, 12, 8, 12));
        bubbleContent.setMaxWidth(400);
        bubbleContent.setStyle(isUser
                ? "-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 12 12 0 12;"
                : "-fx-background-color: #e9ecef; -fx-background-radius: 12 12 12 0;");

        ImageView avatar = new ImageView(isUser
                ? new Image("https://img.icons8.com/ios-filled/50/005A9C/user-male-circle.png")
                : new Image("https://img.icons8.com/ios-filled/50/000000/robot.png"));
        avatar.setFitWidth(42);
        avatar.setFitHeight(42);

        HBox messageRow = new HBox(10);
        messageRow.setAlignment(Pos.TOP_LEFT);
        messageRow.setMaxWidth(700);

        if (isUser) {
            messageRow.setAlignment(Pos.TOP_RIGHT);
            messageRow.getChildren().addAll(bubbleContent, avatar);
        } else {
            messageRow.getChildren().addAll(avatar, bubbleContent);
        }

        messageRow.setOpacity(0);
        chatHistoryBox.getChildren().add(messageRow);

        FadeTransition ft = new FadeTransition(Duration.millis(400), messageRow);
        ft.setToValue(1.0);
        ft.play();
    }


    private void showLoading(boolean show) {
        if (show) {
            loadingIndicator = new ProgressIndicator();
            loadingIndicator.setPrefSize(30, 30);
            HBox loadingContainer = new HBox(loadingIndicator);
            loadingContainer.setAlignment(Pos.CENTER);
            loadingContainer.setPadding(new Insets(5, 0, 5, 0));
            chatHistoryBox.getChildren().add(loadingContainer);
        } else {
            if (loadingIndicator != null) {
                for (Node node : chatHistoryBox.getChildren()) {
                    if (node instanceof HBox && ((HBox) node).getChildren().contains(loadingIndicator)) {
                        chatHistoryBox.getChildren().remove(node);
                        break;
                    }
                }
            }
        }
    }

    private String callGemini(String userInput) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject content = new JSONObject();
        content.put("parts", new JSONArray().put(new JSONObject().put("text", userInput)));

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("contents", new JSONArray().put(content));

        RequestBody body = RequestBody.create(
                requestBodyJson.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(GEMINI_URL + GEMINI_API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body().string();
                System.err.println("Gemini API Error: " + err);
                throw new IOException("API call failed with code: " + response.code() + " and body: " + err);
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            return json
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}