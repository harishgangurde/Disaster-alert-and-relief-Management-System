package com.thinkspark.view;

import com.thinkspark.dao.DisasterDao;
import com.thinkspark.dao.FileStorageDao;
import com.thinkspark.model.Disaster;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class ReportDisasterView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SUCCESS_COLOR = "#48BB78";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final String userName;
    private File selectedFile;
    private final FileStorageDao fileStorageDao;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public ReportDisasterView(Stage primaryStage, Scene previousScene, String userName) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.userName = userName;
        this.fileStorageDao = new FileStorageDao();
    }


    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40); 
        rootLayout.setPadding(new Insets(60, 80, 80, 80)); 
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createDisasterReportForm());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); 

        Label title = new Label("🚨 Report a New Disaster");
        title.setFont(getFont(42, FontWeight.BOLD)); 
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); 
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12;"); // Increased radius
        backButton.setPadding(new Insets(15, 28, 15, 28)); 
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createDisasterReportForm() {
        VBox card = new VBox(35); 
        card.setPadding(new Insets(50)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); 
        card.setMaxWidth(1000); 

        GridPane formGrid = new GridPane();
        formGrid.setHgap(30); 
        formGrid.setVgap(25); 

        ComboBox<String> disasterType = new ComboBox<>();
        disasterType.getItems().addAll("Flood", "Earthquake", "Wildfire", "Hurricane", "Power Outage", "Medical Emergency", "Other");
        disasterType.setPromptText("Select a type");
        styleFormControl(disasterType);
        disasterType.setPrefHeight(60); 

        TextField locationField = createTextField("e.g., City Hall, 123 Main St, Nashik");
        locationField.setPrefHeight(60); 

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Provide a brief description of the situation...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(5); 
        styleFormControl(descriptionArea);
        descriptionArea.setPrefHeight(120); 

        TextField contactField = createTextField("Your phone number (Optional)");
        contactField.setPrefHeight(60);

        Label fileNameLabel = new Label("No file selected.");
        fileNameLabel.setFont(getFont(16, FontWeight.NORMAL)); 
        
        Button chooseFileButton = new Button("Choose File...");
        chooseFileButton.setFont(getFont(16, FontWeight.MEDIUM)); 
        chooseFileButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: " + FONT_COLOR_LIGHT + "; -fx-background-radius: 8; -fx-padding: 10 20; -fx-cursor: hand;"); // Styled button
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a Photo");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            fileNameLabel.setText(selectedFile != null ? selectedFile.getName() : "No file selected.");
        });
        HBox fileUploadBox = new HBox(15, chooseFileButton, fileNameLabel);
        fileUploadBox.setAlignment(Pos.CENTER_LEFT);

        formGrid.add(createLabel("Type of Disaster"), 0, 0);
        formGrid.add(disasterType, 1, 0);
        formGrid.add(createLabel("Location / Address"), 0, 1);
        formGrid.add(locationField, 1, 1);
        formGrid.add(createLabel("Description"), 0, 2);
        formGrid.add(descriptionArea, 1, 2);
        formGrid.add(createLabel("Contact Number"), 0, 3);
        formGrid.add(contactField, 1, 3);
        formGrid.add(createLabel("Upload Photo"), 0, 4);
        formGrid.add(fileUploadBox, 1, 4);

        Button submitButton = new Button("Submit Report");
        submitButton.setFont(getFont(22, FontWeight.BOLD));
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 12; -fx-cursor: hand;"); // Increased radius
        submitButton.setPadding(new Insets(18, 35, 18, 35));

        HBox actionBox = new HBox(submitButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(30, 0, 0, 0));

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> {
            String type = disasterType.getValue();
            String location = locationField.getText();
            String description = descriptionArea.getText();

            if (type == null || location.isEmpty() || description.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all required fields: Type, Location, and Description.").showAndWait();
                return;
            }

            String imageUrl = null;
            if (selectedFile != null) {
                imageUrl = fileStorageDao.uploadFile(selectedFile);
            }

            Disaster newDisaster = new Disaster(type, location, description, this.userName);
            newDisaster.setImageUrl(imageUrl);

            DisasterDao disasterDao = new DisasterDao();
            disasterDao.saveDisasterReport(newDisaster);

            VBox confirmationVBox = new VBox(30);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(60));

            Label confirmationTitle = new Label("✅ Submission Received!");
            confirmationTitle.setFont(getFont(32, FontWeight.BOLD));

            Label confirmationText = new Label("Thank you! Your report has been sent to response teams.");
            confirmationText.setFont(getFont(20, FontWeight.NORMAL));
            confirmationText.setWrapText(true);
            confirmationText.setTextAlignment(TextAlignment.CENTER);

            confirmationVBox.getChildren().addAll(confirmationTitle, confirmationText);

            container.getChildren().setAll(confirmationVBox);

            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> primaryStage.setScene(previousScene));
            delay.play();
        });

        card.getChildren().addAll(formGrid, actionBox);

        return container;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(getFont(18, FontWeight.MEDIUM));
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        styleFormControl(textField);
        return textField;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-padding: 10;"); // Increased font size, padding, radius
        GridPane.setHgrow(control, Priority.ALWAYS);
    }
}