package com.thinkspark.view;

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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;

public class LogHoursView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SUCCESS_COLOR = "#38A169";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public LogHoursView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(50);
        rootLayout.setPadding(new Insets(60, 80, 80, 80));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node logHoursForm = createLogHoursForm();

        rootLayout.getChildren().addAll(header, logHoursForm);

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

        Label title = new Label("📋 Log Your Hours");
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
            primaryStage.setTitle("Volunteer Dashboard | Disaster Relief");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createLogHoursForm() {
        VBox card = new VBox(35); 
        card.setPadding(new Insets(50)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);");
        card.setMaxWidth(1000);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(30);
        formGrid.setVgap(25); 

        formGrid.add(createLabel("Date of Activity"), 0, 0);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        styleFormControl(datePicker);
        datePicker.setPrefHeight(60);
        formGrid.add(datePicker, 1, 0);

        formGrid.add(createLabel("Hours Worked"), 0, 1);
        Spinner<Double> hoursSpinner = new Spinner<>(0.5, 24.0, 1.0, 0.5);
        styleFormControl(hoursSpinner);
        hoursSpinner.setPrefHeight(60);
        formGrid.add(hoursSpinner, 1, 1);

        formGrid.add(createLabel("Activity Description"), 0, 2);
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Briefly describe your work (e.g., 'Distributed supplies at shelter')...");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(5);
        styleFormControl(descriptionArea);
        descriptionArea.setPrefHeight(120);
        formGrid.add(descriptionArea, 1, 2);

        Button submitButton = new Button("Submit Hours");
        submitButton.setFont(getFont(22, FontWeight.BOLD));
        submitButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        submitButton.setStyle("-fx-background-color: " + SUCCESS_COLOR + "; -fx-background-radius: 12; -fx-cursor: hand;"); // Increased radius
        submitButton.setPadding(new Insets(18, 35, 18, 35)); 
        HBox actionBox = new HBox(submitButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        submitButton.setOnAction(e -> {
            System.out.println("Hours Logged!");
            
            VBox confirmationVBox = new VBox(30);
            confirmationVBox.setAlignment(Pos.CENTER);
            confirmationVBox.setPadding(new Insets(60)); 
            
            Label confirmationTitle = new Label("✅ Hours Submitted!");
            confirmationTitle.setFont(getFont(32, FontWeight.BOLD));
            confirmationTitle.setTextFill(Color.web(FONT_COLOR_DARK));
            
            Label confirmationText = new Label("Thank you for logging your time. Your contribution is greatly appreciated.");
            confirmationText.setFont(getFont(20, FontWeight.NORMAL));
            confirmationText.setTextFill(Color.web(FONT_COLOR_DARK));
            confirmationText.setWrapText(true);
            confirmationText.setTextAlignment(TextAlignment.CENTER);
            
            confirmationVBox.getChildren().addAll(confirmationTitle, confirmationText);
            
            container.getChildren().setAll(confirmationVBox);
            
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> {
                Button backButton = (Button) ((HBox) ((VBox) container.getParent()).getChildren().get(0)).getChildren().get(2);
                backButton.fire();
            });
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

    private void styleFormControl(Control control) {
        control.setStyle("-fx-font-size: 16px; -fx-background-color: #FFFFFF; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 10; -fx-padding: 10; -fx-max-width: infinity;"); // Increased font size, padding, radius
    }
}