package com.thinkspark.view;

import com.thinkspark.dao.NgoDao;
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

public class ManageNgosView extends Application {
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_VERIFIED_COLOR = "#38A169";
    private static final String STATUS_PENDING_COLOR = "#00a6ffff";
    private static final String ACTION_BUTTON_COLOR = "#3182CE";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final NgoDao ngoDao;
    private ObservableList<NgoItem> ngosList;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public static class NgoItem {
        private String name;
        private String status;
        private String contact;

        public NgoItem(String name, String status, String contact) {
            this.name = name;
            this.status = status;
            this.contact = contact;
        }

        public String getName() { return name; }
        public String getStatus() { return status; }
        public String getContact() { return contact; }
        public void setStatus(String status) { this.status = status; }
    }

    private class NgoCell extends ListCell<NgoItem> {
        @Override
        protected void updateItem(NgoItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox content = new HBox(25);
                content.setAlignment(Pos.CENTER);
                content.setPadding(new Insets(25));
                content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");

                Label nameLabel = new Label(item.getName());
                nameLabel.setFont(ManageNgosView.getFont(20, FontWeight.BOLD));
                Label contactLabel = new Label("Contact: " + item.getContact());
                contactLabel.setFont(ManageNgosView.getFont(18, FontWeight.NORMAL));
                contactLabel.setOpacity(0.8);
                VBox details = new VBox(10, nameLabel, contactLabel);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                String status = item.getStatus() != null ? item.getStatus().toUpperCase() : "VERIFIED";
                Label statusLabel = new Label(status);
                statusLabel.setFont(ManageNgosView.getFont(16, FontWeight.BOLD));
                statusLabel.setTextFill(Color.WHITE);
                statusLabel.setPadding(new Insets(8, 12, 8, 12));
                String statusColor = "Verified".equalsIgnoreCase(item.getStatus()) ? STATUS_VERIFIED_COLOR : STATUS_VERIFIED_COLOR;
                statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 10;");

                content.getChildren().addAll(details, spacer, statusLabel);

                if ("Pending Verification".equalsIgnoreCase(item.getStatus())) {
                    Button verifyButton = new Button("Verify NGO");
                    verifyButton.setFont(ManageNgosView.getFont(16, FontWeight.BOLD));
                    verifyButton.setTextFill(Color.WHITE);
                    verifyButton.setStyle("-fx-background-color: " + ACTION_BUTTON_COLOR + "; -fx-background-radius: 10; -fx-cursor: hand;");
                    verifyButton.setPadding(new Insets(10, 20, 10, 20));
                    verifyButton.setOnAction(e -> {
                        item.setStatus("Verified");
                        getListView().refresh();
                    });
                    content.getChildren().add(verifyButton);
                }
                setGraphic(content);
            }
        }
    }

    public ManageNgosView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.ngoDao = new NgoDao();
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40);
        rootLayout.setPadding(new Insets(60, 80, 80, 80));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createNgosList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");

        ngoDao.listenForNgos(ngosList);
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0));

        Label title = new Label("Manage Partner NGOs");
        title.setFont(getFont(42, FontWeight.BOLD));

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

    private Node createNgosList() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);");

        ngosList = FXCollections.observableArrayList();

        ListView<NgoItem> listView = new ListView<>(ngosList);
        listView.setCellFactory(param -> new NgoCell());
        listView.setStyle("-fx-background-color: transparent;");
        listView.setPlaceholder(new Label("No NGOs to display."));
        listView.setPrefHeight(600);

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}