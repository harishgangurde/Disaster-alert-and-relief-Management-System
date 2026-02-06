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

public class ReliefResourcesView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ACCENT_COLOR_GREEN = "#38A169";
    private static final String ACCENT_COLOR_ORANGE = "#DD6B20";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public ReliefResourcesView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40);
        rootLayout.setPadding(new Insets(60));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node resourcesContent = createResourcesContent();

        rootLayout.getChildren().addAll(header, resourcesContent);

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

        Label title = new Label("📦 Relief Resources");
        title.setFont(getFont(48, FontWeight.BOLD)); // Increased font size
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(24, FontWeight.MEDIUM)); // Increased font size
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10; -fx-border-width: 2;");
        backButton.setPadding(new Insets(12, 24, 12, 24)); // Increased padding
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Safe Serve | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createResourcesContent() {
        VBox card = new VBox(40);
        card.setPadding(new Insets(60)); // Increased padding
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 18; " + // Matched AboutUsView
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 15, 0, 0, 6);");
        card.setMaxWidth(1400); // Increased max width

        GridPane resourcesGrid = new GridPane();
        resourcesGrid.setHgap(45); // Increased gap
        resourcesGrid.setVgap(45); // Increased gap

        resourcesGrid.add(createResourceCategory(
            "💧 Food & Water",
            "Distribution centers for essential supplies, including potable water and non-perishable food items.",
            new String[]{
                "Collector Office, Nashik - Water Bottles (High Stock) - Available 24/7",
                "Rajiv Gandhi Bhavan, Nashik - Food Packets (Medium Stock) - Open 8 AM - 8 PM",
                "Panchavati Karanja - Community Kitchen (Operating 24/7) - Hot meals provided",
                "CIDCO Community Hall - Bottled Water & Dry Rations (High Stock) - Daily distribution"
            }
        ), 0, 0);

        resourcesGrid.add(createResourceCategory(
            "⚕️ Medical Supplies",
            "Access to first-aid, essential medicines, and hygiene kits. Support for basic medical needs during emergencies.",
            new String[]{
                "Nashik Civil Hospital - First-Aid Kits (High Stock) - Emergency medical aid",
                "Wockhardt Hospital - General Medicines (Limited Stock) - Prescription required for some meds",
                "Red Cross Society, Shalimar - Hygiene Kits (Medium Stock) - Personal hygiene essentials",
                "Saraswati Hospital, Adgaon - Emergency Medical Support (High Availability) - 24-hour service"
            }
        ), 1, 0);

        resourcesGrid.add(createResourceCategory(
            "⛺ Temporary Shelters",
            "Safe locations for temporary accommodation and basic amenities. Please check for capacity before proceeding.",
            new String[]{
                "Meenatai Thackeray Stadium, Panchavati - Large Capacity (Open)",
                "Kalidas Kalamandir, Shalimar - Medium Capacity (Open)",
                "Various schools in the affected zones (check local announcements) - Limited facilities",
                "Mumbai-Agra Highway Relief Camp (Near Toll Plaza) - Tents and basic facilities"
            }
        ), 0, 1);

        resourcesGrid.add(createResourceCategory(
            "👕 Clothing & Blankets",
            "Warm clothing and blankets for all ages. Donations are also accepted at these points.",
            new String[]{
                "Godavari Ghat Collection Point, Panchavati - All sizes (High Stock)",
                "City Centre Mall (Basement Collection) - Limited children's clothing",
                "Local NGO distribution vans (mobile) - Check nearest van location for distribution times",
                "Mahatma Nagar Community Center - Blankets and winter wear (Medium Stock)"
            }
        ), 1, 1);

        card.getChildren().add(resourcesGrid);

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);

        return container;
    }

    private Node createResourceCategory(String title, String description, String[] items) {
        VBox categoryBox = new VBox(25); // Increased spacing
        categoryBox.setPadding(new Insets(35)); // Increased padding
        categoryBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 12;");
        categoryBox.setPrefHeight(400); // Increased height

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD)); // Increased font size
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Text descText = new Text(description);
        descText.setFont(getFont(22, FontWeight.NORMAL)); // Increased font size
        descText.setFill(Color.web(FONT_COLOR_DARK));
        descText.setWrappingWidth(450); // Increased wrapping width

        VBox itemsList = new VBox(15); // Increased spacing
        for (String item : items) {
            itemsList.getChildren().add(createResourceItem(item));
        }

        categoryBox.getChildren().addAll(titleLabel, descText, itemsList);
        return categoryBox;
    }

    private Node createResourceItem(String itemText) {
        HBox itemBox = new HBox(10); // Increased spacing
        itemBox.setAlignment(Pos.CENTER_LEFT);

        String statusText = "";
        String color = FONT_COLOR_DARK;

        if (itemText.contains("(High Stock)")) {
            statusText = "High Stock";
            color = ACCENT_COLOR_GREEN;
        } else if (itemText.contains("(Medium Stock)")) {
            statusText = "Medium Stock";
            color = ACCENT_COLOR_ORANGE;
        } else if (itemText.contains("(Limited Stock)")) {
            statusText = "Limited Stock";
            color = Color.RED.toString();
        } else if (itemText.contains("(Operating 24/7)") || itemText.contains("(High Availability)")) {
            statusText = "Open 24/7";
            color = ACCENT_COLOR_GREEN;
        } else if (itemText.contains("(Open)")) {
            statusText = "Open";
            color = ACCENT_COLOR_GREEN;
        } else if (itemText.contains("(Check nearest van location)")) {
            statusText = "Mobile";
            color = PRIMARY_BLUE;
        } else if (itemText.contains("(Prescription required)")) {
            statusText = "Restricted";
            color = ACCENT_COLOR_ORANGE;
        }


        Label mainLabel = new Label(itemText.split("\\(")[0].trim());
        mainLabel.setFont(getFont(18, FontWeight.NORMAL)); // Increased font size
        mainLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        Label statusLabel = new Label(statusText);
        statusLabel.setFont(getFont(16, FontWeight.BOLD)); // Increased font size
        statusLabel.setTextFill(Color.web(color));
        statusLabel.setPadding(new Insets(4, 10, 4, 10)); // Increased padding
        statusLabel.setStyle("-fx-background-color: " + Color.web(color).deriveColor(0, 1, 1, 0.1).toString().replace("0x", "#") + "; -fx-background-radius: 6;");

        if (!statusText.isEmpty()) {
            itemBox.getChildren().addAll(mainLabel, statusLabel);
        } else {
            itemBox.getChildren().add(mainLabel);
        }

        return itemBox;
    }
}