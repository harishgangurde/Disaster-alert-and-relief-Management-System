// File: superx_project/src/main/java/com/thinkspark/view/SignUpForm.java
package com.thinkspark.view;

import com.thinkspark.Controller.SignUpController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignUpForm extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(1200, 800);
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 20; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 30, 0, 0, 10);");
        mainLayout.getChildren().addAll(createBrandingPane(), createSignUpForm(primaryStage));

        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(60));

        Scene scene = new Scene(root,1920,1080);
        primaryStage.setTitle("Safe Serve");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();
    }

    private Node createBrandingPane() {
        VBox brandingPane = new VBox(30);
        brandingPane.setPrefWidth(600);
        brandingPane.setAlignment(Pos.CENTER_LEFT);
        brandingPane.setPadding(new Insets(60));
        brandingPane.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 20 0 0 20;");

        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logoView.setFitHeight(300);
        logoView.setPreserveRatio(true);

        Label logo = new Label("🚀 Join Us");
        logo.setFont(getFont(50, FontWeight.BOLD));
        logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Text welcomeText = new Text("Become a part of a community dedicated to providing and receiving help effectively during crises. Your first step to making a difference starts here.");
        welcomeText.setFont(getFont(20, FontWeight.NORMAL));
        welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
        welcomeText.setOpacity(0.9);
        welcomeText.setWrappingWidth(450);

        brandingPane.getChildren().addAll(logoView, logo, welcomeText);
        return brandingPane;
    }

    private Node createSignUpForm(Stage primaryStage) {
        VBox formPane = new VBox(30);
        formPane.setPrefWidth(600);
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(50));

        Label title = new Label("Create Your Account");
        title.setFont(getFont(34, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 15, 0));

        TextField nameField = createStyledTextField("Full Name");
        TextField emailField = createStyledTextField("Email Address");
        PasswordField passwordField = createStyledPasswordField("Password");
        ComboBox<String> userType = createStyledComboBox();

        Button signUpButton = new Button("Sign Up");
        signUpButton.setFont(getFont(20, FontWeight.BOLD));
        signUpButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        signUpButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        signUpButton.setPrefHeight(60);
        signUpButton.setMaxWidth(Double.MAX_VALUE);

        Hyperlink loginLink = new Hyperlink("Already have an account? Log In");
        loginLink.setFont(getFont(18, FontWeight.BOLD));
        loginLink.setTextFill(Color.web(PRIMARY_BLUE));
        loginLink.setStyle("-fx-underline: true; -fx-cursor: hand;");

        Button backToDashboardButton = new Button("⬅️ Back to Dashboard");
        backToDashboardButton.setFont(getFont(16, FontWeight.MEDIUM));
        backToDashboardButton.setTextFill(Color.web(PRIMARY_BLUE));
        backToDashboardButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12; -fx-border-width: 2; -fx-padding: 10 20;");
        backToDashboardButton.setOnAction(e -> new DisasterHelperUI().start(primaryStage));

        HBox topLinks = new HBox();
        topLinks.setAlignment(Pos.TOP_RIGHT);
        topLinks.getChildren().add(backToDashboardButton);

        HBox bottomLinkPane = new HBox(40);
        bottomLinkPane.setAlignment(Pos.CENTER);
        bottomLinkPane.getChildren().addAll(loginLink);
        VBox.setMargin(bottomLinkPane, new Insets(20, 0, 0, 0));

        Label nameLabel = createLabel("Full Name");
        Label emailLabel = createLabel("Email Address");
        Label passwordLabel = createLabel("Password");
        Label roleLabel = createLabel("I am a...");

        nameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                emailField.requestFocus();
            }
        });

        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                userType.requestFocus();
            }
        });

        userType.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUpButton.fire();
            }
        });

        signUpButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = userType.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            SignUpController signUpController = new SignUpController();
            SignUpController.Result result = signUpController.signUp(name, email, password, role);

            if (result.success) {
                showAlert("Success", "User registered successfully! Please log in.");
                 new LoginUI().start(primaryStage);
            } else {
                showAlert("Error", "Failed to register user. " + result.message);
            }
        });
        
        loginLink.setOnAction(e -> {
            new LoginUI().start(primaryStage);
        });

        formPane.getChildren().addAll(
            topLinks,
            title,
            nameLabel, nameField,
            emailLabel, emailField,
            passwordLabel, passwordField,
            roleLabel, userType,
            signUpButton,
            bottomLinkPane
        );

        return formPane;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(getFont(18, FontWeight.MEDIUM));
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(label, new Insets(10, 0, 0, 0));
        return label;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(18, FontWeight.NORMAL));
        textField.setPrefHeight(60);
        textField.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;");
        return textField;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setFont(getFont(18, FontWeight.NORMAL));
        passwordField.setPrefHeight(60);
        passwordField.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;");
        return passwordField;
    }
    
    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        comboBox.setPromptText("Select Role");
        comboBox.setPrefHeight(60);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 18px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;");
        return comboBox;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}