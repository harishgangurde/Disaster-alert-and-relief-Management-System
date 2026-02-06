package com.thinkspark.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ViewDonationsView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private static class DonationItem {
        private final String donorName;
        private final String amount;
        private final String date;

        public DonationItem(String donorName, String amount, String date) {
            this.donorName = donorName;
            this.amount = amount;
            this.date = date;
        }
    }

    private static class DonationCell extends ListCell<DonationItem> {
        private final HBox content = new HBox();
        private final Label donorLabel = new Label();
        private final Label dateLabel = new Label();
        private final Label amountLabel = new Label();
        private final Region spacer = new Region();

        public DonationCell() {
            super();
            donorLabel.setFont(ViewDonationsView.getFont(20, FontWeight.BOLD));
            dateLabel.setFont(ViewDonationsView.getFont(18, FontWeight.NORMAL));
            dateLabel.setOpacity(0.8);
            amountLabel.setFont(ViewDonationsView.getFont(22, FontWeight.BOLD));
            amountLabel.setTextFill(Color.web(PRIMARY_BLUE));

            VBox details = new VBox(10, donorLabel, dateLabel);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            content.setAlignment(Pos.CENTER);
            content.setSpacing(25);
            content.getChildren().addAll(details, spacer, amountLabel);
            content.setPadding(new Insets(25));
            content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");
        }

        @Override
        protected void updateItem(DonationItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                donorLabel.setText(item.donorName);
                dateLabel.setText("On: " + item.date);
                amountLabel.setText(item.amount);
                setGraphic(content);
            }
        }
    }

    public ViewDonationsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40); 
        rootLayout.setPadding(new Insets(60, 80, 80, 80));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node donationsList = createDonationsList();

        rootLayout.getChildren().addAll(header, donationsList);

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

        Label title = new Label("Received Donations");
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
            primaryStage.setTitle("NGO Dashboard | Disaster Relief Operations");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createDonationsList() {
        VBox card = new VBox(15); 
        card.setPadding(new Insets(40)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); 

        ObservableList<DonationItem> donations = FXCollections.observableArrayList(
            new DonationItem("Anonymous", "Rs100.00", "2024-07-19"),
            new DonationItem("Atharva Guthe", "Rs5000.00", "2024-07-19"),
            new DonationItem("Rajesh Malhotra", "Rs250.00", "2024-07-18")
        );

        ListView<DonationItem> listView = new ListView<>(donations);
        listView.setPrefHeight(600); 
        listView.setCellFactory(param -> new DonationCell());
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

}