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
import javafx.stage.Stage;

import java.awt.Desktop; 
import java.io.IOException; 
import java.net.URI; 
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NearestSafeZoneView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_OPEN_COLOR = "#38A169";
    private static final String STATUS_FULL_COLOR = "#E53E3E";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public NearestSafeZoneView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(45);
        rootLayout.setPadding(new Insets(60)); 
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node safeZoneContent = createSafeZoneContent();

        rootLayout.getChildren().addAll(header, safeZoneContent);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox(20); 
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0)); 

        Label title = new Label("📍 Nearest Safe Zones");
        title.setFont(getFont(42, FontWeight.BOLD)); 
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(22, FontWeight.MEDIUM)); 
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10; -fx-border-width: 2;"); // Increased border radius and width
        backButton.setPadding(new Insets(12, 24, 12, 24)); 
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Safe Serve | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createSafeZoneContent() {
        VBox contentLayout = new VBox(35); 
        contentLayout.setAlignment(Pos.CENTER);

        contentLayout.getChildren().addAll(
            createSafeZoneCard("Meenatai Thackeray Stadium", "Panchavati, Nashik", "Capacity: 2000", "Open & Accepting", true),
            createSafeZoneCard("Kalidas Kalamandir", "Shalimar, Nashik", "Capacity: 1200", "Open & Accepting", true),
            createSafeZoneCard("City Centre Mall (Parking Area)", "Untwadi, Nashik", "Capacity: 3500", "At Capacity", false),
            createSafeZoneCard("Panchavati College Campus", "Panchavati, Nashik", "Capacity: 1500", "Open & Accepting", true)
        );

        return contentLayout;
    }

    private Node createSafeZoneCard(String name, String address, String capacity, String status, boolean isOpen) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(35)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 18; " + 
                      "-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 2; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 12, 0, 0, 5);"); 
        card.setMaxWidth(1000); 

        Label nameLabel = new Label(name);
        nameLabel.setFont(getFont(28, FontWeight.BOLD)); 
        nameLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Label addressLabel = new Label(address);
        addressLabel.setFont(getFont(20, FontWeight.NORMAL)); 
        addressLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        Label capacityLabel = new Label(capacity);
        capacityLabel.setFont(getFont(18, FontWeight.MEDIUM)); 
        capacityLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        HBox statusBox = new HBox(15); 
        statusBox.setAlignment(Pos.CENTER_LEFT);
        
        Label statusTitleLabel = new Label("Status:");
        statusTitleLabel.setFont(getFont(20, FontWeight.BOLD));
        
        Label statusValueLabel = new Label(status);
        statusValueLabel.setFont(getFont(20, FontWeight.BOLD)); 
        statusValueLabel.setTextFill(Color.web(isOpen ? STATUS_OPEN_COLOR : STATUS_FULL_COLOR));
        
        statusBox.getChildren().addAll(statusTitleLabel, statusValueLabel);
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button directionsButton = new Button("Get Directions");
        directionsButton.setFont(getFont(18, FontWeight.BOLD)); 
        directionsButton.setTextFill(Color.WHITE);
        directionsButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;"); // Increased radius
        directionsButton.setPadding(new Insets(12, 28, 12, 28));

        directionsButton.setOnAction(event -> {
            try {
                String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
                String mapUrl = "https://www.google.com/maps/dir/?api=1&destination=" + encodedAddress;

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(mapUrl));
                } else {
                    System.err.println("Desktop API not supported or BROWSE action not supported. Cannot open map.");
                }
            } catch (IOException | URISyntaxException e) {
                System.err.println("Error opening map: " + e.getMessage());
            }
        });
        
        HBox bottomRow = new HBox(statusBox, new Region(), directionsButton);
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(nameLabel, addressLabel, capacityLabel, spacer, bottomRow);
        return card;
    }
}