package com.thinkspark.view;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.ListenerRegistration;
import com.thinkspark.Controller.DeleteAccountController;
import com.thinkspark.Controller.ForgotPasswordController;
import com.thinkspark.dao.FileStorageDao;
import com.thinkspark.model.FirebaseConfig;
import com.thinkspark.model.User;
import com.thinkspark.model.UserService;

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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

public class ProfileView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_ACCENT_COLOR = "#48BB78";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final String userEmail; 

    private final ForgotPasswordController forgotPasswordController = new ForgotPasswordController();
    private final DeleteAccountController deleteAccountController = new DeleteAccountController();
    private final FileStorageDao fileStorageDao = new FileStorageDao();
    private final UserService userService = new UserService();

    private ImageView profileImageView;
    private Label nameLabel;
    private Label emailLabel;
    private ListenerRegistration firestoreListener; 

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public ProfileView(Stage primaryStage, Scene previousScene, String userEmail) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.userEmail = userEmail;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40);
        rootLayout.setPadding(new Insets(60, 80, 80, 80)); 
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node profileContent = createProfileContent();

        rootLayout.getChildren().addAll(header, profileContent);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Profile | Safe Serve");

        startProfileListener();
    }

    @Override
    public void stop() {
        if (firestoreListener != null) {
            firestoreListener.remove();
        }
    }

    private void startProfileListener() {
        if (FirebaseConfig.getDb() == null) {
            System.err.println("Firestore not initialized. Cannot listen for profile updates.");
            return;
        }

        DocumentReference userDocRef = FirebaseConfig.getDb().collection("users").document(userEmail);

        firestoreListener = userDocRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                System.err.println("Listen failed: " + e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                User user = snapshot.toObject(User.class);
                Platform.runLater(() -> {
                    if (user != null) {
                        nameLabel.setText(user.getName() != null ? user.getName() : "N/A");
                        emailLabel.setText(user.getEmail() != null ? user.getEmail() : "N/A");
                        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                            try {
                                profileImageView.setImage(new Image(user.getImageUrl()));
                            } catch (Exception imgE) {
                                System.err.println("Error loading profile image from URL: " + user.getImageUrl() + ". " + imgE.getMessage());
                                setPlaceholderImage();
                            }
                        } else {
                            setPlaceholderImage();
                        }
                    }
                });
            } else {
                System.out.println("Current data: null");
                Platform.runLater(this::setPlaceholderImage);
            }
        });
    }

    private void setPlaceholderImage() {
        InputStream defaultLogoStream = getClass().getResourceAsStream("/logo.png");
        if (defaultLogoStream != null) {
            profileImageView.setImage(new Image(defaultLogoStream));
        } else {
            System.err.println("Default profile placeholder image not found!");
            profileImageView.setImage(null);
        }
    }


    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); 

        Label title = new Label("👤 My Profile");
        title.setFont(getFont(42, FontWeight.BOLD)); 
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12;"); // Increased radius
        backButton.setPadding(new Insets(15, 28, 15, 28));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createProfileContent() {
        VBox card = new VBox(35); 
        card.setPadding(new Insets(50)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); 
        card.setMaxWidth(1000); 
        card.setAlignment(Pos.TOP_CENTER); 

        profileImageView = new ImageView();
        profileImageView.setFitWidth(200); 
        profileImageView.setFitHeight(200); 
        profileImageView.setPreserveRatio(true);
        profileImageView.setClip(new Circle(100, 100, 100)); 

        profileImageView.setStyle("-fx-cursor: hand;");
        profileImageView.setOnMouseClicked(e -> handleChangeProfilePhoto());

        Label changePhotoHint = new Label("Click to change photo");
        changePhotoHint.setFont(getFont(14, FontWeight.NORMAL));
        changePhotoHint.setTextFill(Color.web(FONT_COLOR_DARK));
        changePhotoHint.setOpacity(0.7);

        VBox photoSection = new VBox(10, profileImageView, changePhotoHint);
        photoSection.setAlignment(Pos.CENTER);
        VBox.setMargin(photoSection, new Insets(0, 0, 20, 0));

        nameLabel = new Label("Name: Loading...");
        nameLabel.setFont(getFont(24, FontWeight.BOLD)); 
        nameLabel.setTextFill(Color.web(PRIMARY_BLUE));

        emailLabel = new Label("Email: Loading...");
        emailLabel.setFont(getFont(20, FontWeight.NORMAL)); 
        emailLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        VBox userInfoSection = new VBox(10, nameLabel, emailLabel);
        userInfoSection.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(userInfoSection, new Insets(0, 0, 30, 0)); 

        Label accountLabel = new Label("Account Settings");
        accountLabel.setFont(getFont(28, FontWeight.BOLD));
        accountLabel.setTextFill(Color.web(PRIMARY_BLUE));
        accountLabel.setPadding(new Insets(20, 0, 10, 0));

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setFont(getFont(18, FontWeight.MEDIUM));
        changePasswordButton.setPadding(new Insets(12, 25, 12, 25));
        changePasswordButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: " + SECONDARY_ACCENT_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;");
        changePasswordButton.setOnAction(e -> handleChangePassword());

        Button deleteAccountButton = new Button("Delete Account");
        deleteAccountButton.setFont(getFont(18, FontWeight.MEDIUM));
        deleteAccountButton.setTextFill(Color.web(ALERT_COLOR));
        deleteAccountButton.setPadding(new Insets(12, 25, 12, 25));
        deleteAccountButton.setStyle("-fx-background-color: transparent; -fx-border-color: " + ALERT_COLOR + "; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");
        deleteAccountButton.setOnAction(e -> handleDeleteAccount());

        HBox accountButtons = new HBox(25, changePasswordButton, deleteAccountButton);
        accountButtons.setAlignment(Pos.CENTER);
        
        card.getChildren().addAll(photoSection, userInfoSection, accountLabel, accountButtons);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
    
    private void handleChangeProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            profileImageView.setImage(new Image(selectedFile.toURI().toString()));

            String imageUrl = fileStorageDao.uploadFile(selectedFile);
            if (imageUrl != null) {
                boolean success = userService.updateUserProfileImage(userEmail, imageUrl);
                if (!success) {
                    showAlert("Error", "Failed to save new profile image URL to database.");
                }
            } else {
                showAlert("Error", "Failed to upload image to storage.");
            }
        }
    }

    private void handleChangePassword() {
        boolean success = forgotPasswordController.sendPasswordResetEmail(this.userEmail);
        if (success) {
            showAlert("Success", "A password reset link has been sent to " + this.userEmail + ". Please check your inbox.");
        } else {
            showAlert("Error", "Could not send password reset email. Please try again later.");
        }
    }

    private void handleDeleteAccount() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Account");
        confirmationDialog.setHeaderText("Are you sure you want to permanently delete your account?");
        confirmationDialog.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = deleteAccountController.deleteAccount(this.userEmail);
            if (success) {
                showAlert("Account Deleted", "Your account has been successfully deleted.");
                primaryStage.close();
                new LoginUI().start(new Stage());
            } else {
                showAlert("Error", "Failed to delete account. Please try again later.");
            }
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.toLowerCase().contains("error")) {
            alert.setAlertType(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}