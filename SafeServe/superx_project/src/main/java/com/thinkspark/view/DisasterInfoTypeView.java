package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DisasterInfoTypeView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR_RED = "#E53E3E";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public DisasterInfoTypeView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(60));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node disasterInfoContent = createDisasterInfoContent();

        rootLayout.getChildren().addAll(header, disasterInfoContent);

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

        Label title = new Label("Disaster Information");
        title.setFont(getFont(42, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 12; -fx-border-width: 2;");
        backButton.setPadding(new Insets(15, 28, 15, 28));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Safe Serve | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createDisasterInfoContent() {
        VBox card = new VBox(40);
        card.setPadding(new Insets(50));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 18; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 15, 0, 0, 6);");
        card.setMaxWidth(1200);

        // --- Disaster Type Sections ---
        Node floodSection = createDisasterSection(
            "Floods",
            "ACTIVE ALERT: Heavy rainfall has led to flooding in low-lying areas near the Godavari river.",
            "Safety Protocols:\n" +
            "• Evacuate immediately if you are in a flood-prone area. Do not wait for official orders.\n" +
            "• Move to higher ground. Avoid walking or driving through floodwaters.\n" +
            "• Turn off utilities at the main switches or valves if instructed to do so.\n" +
            "• Keep emergency supplies, including a battery-powered radio, on hand.",
            "Affected Areas: Ramkund, Panchavati, Gangapur Road low-lying areas."
        );
        
        Node earthquakeSection = createDisasterSection(
            "Earthquakes",
            "No active alerts at this time. Nashik is in a moderate seismic zone.",
            "Safety Protocols:\n" +
            "• If indoors: Drop, Cover, and Hold On. Get under a sturdy table and hold on until shaking stops.\n" +
            "• If outdoors: Find a clear spot away from buildings, trees, and power lines.\n" +
            "• After the shaking: Be prepared for aftershocks. Check for injuries and hazards.",
            "Most Vulnerable Areas: Older structures in the core city area."
        );

        card.getChildren().addAll(floodSection, earthquakeSection);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

    private Node createDisasterSection(String title, String status, String protocols, String affectedAreas) {
        VBox section = new VBox(20);
        section.setPadding(new Insets(30, 0, 30, 0));
        section.setStyle("-fx-border-color: " + BORDER_COLOR + " transparent transparent transparent; -fx-border-width: 1;");

        // Remove border from the first item
        section.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null && ((VBox)section.getParent()).getChildren().get(0) == section) {
                section.setStyle("-fx-border-width: 0; -fx-padding-top: 0;");
            }
        });

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(28, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Label statusLabel = new Label(status);
        statusLabel.setFont(getFont(18, FontWeight.BOLD));
        statusLabel.setWrapText(true);
        if (status.startsWith("ACTIVE ALERT")) {
            statusLabel.setTextFill(Color.web(ALERT_COLOR_RED));
        } else {
            statusLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        }

        VBox protocolsBox = createInfoSubSection("Safety Protocols", protocols);
        VBox affectedAreasBox = createInfoSubSection("Affected Areas", affectedAreas);

        section.getChildren().addAll(titleLabel, statusLabel, protocolsBox, affectedAreasBox);
        return section;
    }

    private VBox createInfoSubSection(String title, String content) {
        VBox subSection = new VBox(8);
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(20, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        Text contentText = new Text(content);
        contentText.setFont(getFont(18, FontWeight.NORMAL));
        contentText.setFill(Color.web(FONT_COLOR_DARK));
        contentText.setWrappingWidth(1100);
        contentText.setLineSpacing(6);

        subSection.getChildren().addAll(titleLabel, contentText);
        return subSection;
    }
}