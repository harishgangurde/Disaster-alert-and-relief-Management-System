// File: superx_project/src/main/java/com/thinkspark/view/LoginUI.java
package com.thinkspark.view;

import com.thinkspark.Controller.LoginController;
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

public class LoginUI extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private final LoginController loginController = new LoginController();
    private TextField emailField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;

    @Override
    public void start(Stage stage) {
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setMaxSize(1200, 800);
        mainLayout.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                           "-fx-background-radius: 20; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 30, 0, 0, 10);");
        mainLayout.getChildren().addAll(createBrandingPane(), createLoginForm(stage));

        StackPane root = new StackPane(mainLayout);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(60));

        Scene scene = new Scene(root,1980,1080);
        stage.setTitle("Safe Serve");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        stage.show();
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

    Label logo = new Label("Safe Serve");
    logo.setFont(getFont(50, FontWeight.BOLD));
    logo.setTextFill(Color.web(FONT_COLOR_LIGHT));

    Text welcomeText = new Text("A unified platform for citizens, volunteers, NGOs, and government agencies to collaborate during crises.");
    welcomeText.setFont(getFont(20, FontWeight.NORMAL));
    welcomeText.setFill(Color.web(FONT_COLOR_LIGHT));
    welcomeText.setOpacity(0.9);
    welcomeText.setWrappingWidth(450);

    brandingPane.getChildren().addAll(logoView, logo, welcomeText);
    return brandingPane;
    }

    private Node createLoginForm(Stage stage) {
        VBox formPane = new VBox(30);
        formPane.setPrefWidth(600);
        formPane.setAlignment(Pos.CENTER_LEFT);
        formPane.setPadding(new Insets(50));

        Label title = new Label("Log In to Your Account");
        title.setFont(getFont(34, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        VBox.setMargin(title, new Insets(0, 0, 15, 0));

        emailField = createStyledTextField("Enter your email");
        passwordField = createStyledPasswordField("Enter your password");
        roleComboBox = createStyledComboBox();

        Label emailLabel = new Label("Email");
        emailLabel.setFont(getFont(18, FontWeight.MEDIUM));
        emailLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(getFont(18, FontWeight.MEDIUM));
        passwordLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        Label roleLabel = new Label("Log in as");
        roleLabel.setFont(getFont(18, FontWeight.MEDIUM));
        roleLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        Button backToDashboardButton = new Button("⬅️ Back to Dashboard");
        backToDashboardButton.setFont(getFont(16, FontWeight.MEDIUM));
        backToDashboardButton.setTextFill(Color.web(PRIMARY_BLUE));
        backToDashboardButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12; -fx-border-width: 2; -fx-padding: 10 20;");
        backToDashboardButton.setOnAction(e -> new DisasterHelperUI().start(stage));

        HBox topLinks = new HBox();
        topLinks.setAlignment(Pos.TOP_RIGHT);
        topLinks.getChildren().add(backToDashboardButton);

        Button loginBtn = new Button("Log In");
        loginBtn.setFont(getFont(20, FontWeight.BOLD));
        loginBtn.setTextFill(Color.web(FONT_COLOR_LIGHT));
        loginBtn.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        loginBtn.setPrefHeight(60);
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setFont(getFont(16, FontWeight.BOLD));
        forgotPasswordLink.setTextFill(Color.web(PRIMARY_BLUE));
        forgotPasswordLink.setStyle("-fx-underline: true; -fx-cursor: hand;");
        forgotPasswordLink.setOnAction(e -> handleForgotPassword(stage));

        Hyperlink signUpLink = new Hyperlink("Don't have an account? Sign Up");
        signUpLink.setFont(getFont(16, FontWeight.BOLD));
        signUpLink.setTextFill(Color.web(PRIMARY_BLUE));
        signUpLink.setStyle("-fx-underline: true; -fx-cursor: hand;");

        HBox bottomLinks = new HBox(40);
        HBox.setHgrow(forgotPasswordLink, Priority.ALWAYS);
        bottomLinks.setAlignment(Pos.CENTER);
        bottomLinks.getChildren().addAll(forgotPasswordLink, signUpLink);

        emailField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                roleComboBox.requestFocus();
            }
        });
        
        roleComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginBtn.fire();
            }
        });

        loginBtn.setOnAction(e -> handleLogin(stage, emailField.getText(), passwordField.getText(), roleComboBox.getValue()));
        signUpLink.setOnAction(e -> handleSignUp(stage));

        formPane.getChildren().addAll(
            topLinks,
            title,
            emailLabel, emailField,
            passwordLabel, passwordField,
            roleLabel, roleComboBox,
            loginBtn,
            bottomLinks
        );

        return formPane;
    }

    private TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(getFont(18, FontWeight.NORMAL));
        textField.setPrefHeight(60);
        textField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;");
        return textField;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setFont(getFont(18, FontWeight.NORMAL));
        passwordField.setPrefHeight(60);
        passwordField.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;");
        return passwordField;
    }

    private ComboBox<String> createStyledComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Citizen", "Volunteer", "NGO", "Government");
        comboBox.setPromptText("Select your role");
        comboBox.setPrefHeight(60);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setStyle("-fx-font-size: 18px; -fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-border-width: 2;");
        return comboBox;
    }

    private void handleLogin(Stage currentStage, String email, String password, String selectedRole) {
        if (selectedRole == null || selectedRole.isEmpty()) {
            showAlert("Login Error", "Please select a role to continue.");
            return;
        }
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            showAlert("Login Error", "Email and password fields cannot be empty.");
            return;
        }

        LoginController.Result result = loginController.login(email, password, selectedRole);

        if (result.success) {
            Application dashboard = null;

            switch (selectedRole) {
                case "Citizen":
                    dashboard = new CitizenDashboard(email);
                    break;
                case "Volunteer":
                    dashboard = new VolunteerDashboard(email);
                    break;
                case "NGO":
                    dashboard = new NgoDashboard(email);
                    break;
                case "Government":
                    dashboard = new GovDashboard(email);
                    break;
            }

            if (dashboard != null) {
                try {
                    dashboard.start(currentStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Application Error", "Could not open the dashboard.");
                }
            } else {
                showAlert("Login Successful", "Welcome! The dashboard for '" + selectedRole + "' is not available.");
            }
        } else {
            showAlert("Login Failed", result.message);
        }
    }

    private void handleSignUp(Stage currentStage) {
        new SignUpForm().start(currentStage);
    }
    
    private void handleForgotPassword(Stage currentStage) {
        new ForgotPasswordUI().start(currentStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.toLowerCase().contains("error") || title.toLowerCase().contains("failed")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}