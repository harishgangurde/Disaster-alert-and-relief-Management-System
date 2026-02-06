package com.thinkspark.view;

import com.thinkspark.dao.DisasterDao;
import com.thinkspark.model.Disaster;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode; 
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AssistanceView extends Application {

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

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public AssistanceView(Stage primaryStage, Scene previousScene, String userName) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.userName = userName;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(50); 
        rootLayout.setPadding(new Insets(60));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createAssistanceForm());

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
        header.setPadding(new Insets(0, 0, 30, 0));

        Label title = new Label("🤝 Request Assistance");
        title.setFont(getFont(40, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); 
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10;");
        backButton.setPadding(new Insets(10, 20, 10, 20)); 
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createAssistanceForm() {
        VBox card = new VBox(35); 
        card.setPadding(new Insets(50)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 15; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 6);"); 
        card.setMaxWidth(1000);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(30);
        formGrid.setVgap(25); 

        ComboBox<String> assistanceType = new ComboBox<>();
        assistanceType.getItems().addAll("Medical", "Food & Water", "Shelter", "Rescue", "Other");
        assistanceType.setPromptText("Select a type of help needed");
        styleFormControl(assistanceType);

        Spinner<Integer> peopleSpinner = new Spinner<>(1, 50, 1);
        styleFormControl(peopleSpinner);

        TextField locationField = createTextField("e.g., Near City Park, 456 Oak Ave, Nashik");
        TextArea detailsArea = new TextArea();
        detailsArea.setPromptText("Describe your situation, e.g., 'Trapped on rooftop', 'Need baby formula'...");
        detailsArea.setWrapText(true);
        detailsArea.setPrefRowCount(5);
        styleFormControl(detailsArea);

        TextField contactField = createTextField("A number where you can be reached");

        formGrid.add(createLabel("Type of Assistance"), 0, 0);
        formGrid.add(assistanceType, 1, 0);
        formGrid.add(createLabel("Number of People"), 0, 1);
        formGrid.add(peopleSpinner, 1, 1);
        formGrid.add(createLabel("Current Location"), 0, 2);
        formGrid.add(locationField, 1, 2);
        formGrid.add(createLabel("Additional Details"), 0, 3);
        formGrid.add(detailsArea, 1, 3);
        formGrid.add(createLabel("Contact Number"), 0, 4);
        formGrid.add(contactField, 1, 4);

        Button submitButton = new Button("Submit Request");
        submitButton.setFont(getFont(22, FontWeight.BOLD));
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 10; -fx-cursor: hand;");
        submitButton.setPadding(new Insets(15, 30, 15, 30));
        HBox actionBox = new HBox(submitButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        assistanceType.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                peopleSpinner.requestFocus();
            }
        });
        peopleSpinner.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                locationField.requestFocus();
            }
        });
        locationField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                detailsArea.requestFocus();
            }
        });
        detailsArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                contactField.requestFocus();
            }
        });
        contactField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitButton.fire();
            }
        });

        submitButton.setOnAction(e -> {
            String type = assistanceType.getValue();
            String location = locationField.getText();
            String details = detailsArea.getText();
            Integer peopleCount = peopleSpinner.getValue();

            if (type == null || location.isEmpty() || details.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").showAndWait();
                return;
            }

            String fullDescription = "Assistance for " + peopleCount + " person(s). Details: " + details;
            
            Disaster newDisaster = new Disaster(type, location, fullDescription, this.userName);
            
            DisasterDao disasterDao = new DisasterDao();
            disasterDao.saveDisasterReport(newDisaster);

            VBox confirmationVBox = new VBox(30);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(50)); 
            
            Label confirmationTitle = new Label("✅ Request Sent!");
            confirmationTitle.setFont(getFont(32, FontWeight.BOLD));
            
            Label confirmationText = new Label("Your request for assistance has been received. Help is on the way.");
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
        label.setFont(getFont(20, FontWeight.MEDIUM));
        label.setTextFill(Color.web(FONT_COLOR_DARK));
        return label;
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        styleFormControl(textField);
        textField.setPrefHeight(60); 
        return textField;
    }

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 18px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-padding: 10;");
        if (control instanceof TextArea) {
            ((TextArea) control).setPrefHeight(120);
        } else if (control instanceof ComboBox) {
            ((ComboBox<?>) control).setPrefHeight(60);
        } else if (control instanceof Spinner) {
            ((Spinner<?>) control).setPrefHeight(60);
        }
    }
}