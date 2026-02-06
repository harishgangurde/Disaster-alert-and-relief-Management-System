package com.thinkspark.view;

import com.thinkspark.dao.DisasterDao;
import com.thinkspark.model.Disaster;

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

public class MyRequestsView extends Application {

    // --- Style Definitions ---
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    private final Scene previousScene;
    private final Stage primaryStage;
    private final String userName;
    private DisasterDao disasterDao;
    private ObservableList<Disaster> myRequestsList;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public MyRequestsView(Stage primaryStage, Scene previousScene, String userName) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.userName = userName;
    }

    @Override
    public void start(Stage stage) {
        disasterDao = new DisasterDao();
        myRequestsList = FXCollections.observableArrayList();
        disasterDao.listenForDisastersByCitizen(myRequestsList, this.userName);

        VBox rootLayout = new VBox(40); 
        rootLayout.setPadding(new Insets(60)); 
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createMyRequestsList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0)); 

        Label title = new Label("📋 My Submitted Requests");
        title.setFont(getFont(40, FontWeight.BOLD)); 
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(20, FontWeight.MEDIUM)); 
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10;"); // Increased radius
        backButton.setPadding(new Insets(10, 20, 10, 20));
        backButton.setOnAction(e -> primaryStage.setScene(previousScene));

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createMyRequestsList() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(40)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); 

        ListView<Disaster> listView = new ListView<>(myRequestsList);
        listView.setPlaceholder(new Label("You have not submitted any requests."));
        listView.setPrefHeight(600); 
        listView.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox content = new HBox(30); 
                    content.setAlignment(Pos.CENTER_LEFT);
                    content.setPadding(new Insets(25));
                    
                    VBox details = new VBox(10); 
                    Label typeLabel = new Label(item.getType());
                    typeLabel.setFont(MyRequestsView.getFont(22, FontWeight.BOLD)); 
                    Label locationLabel = new Label(item.getLocation());
                    locationLabel.setFont(MyRequestsView.getFont(20, FontWeight.NORMAL)); 
                    details.getChildren().addAll(typeLabel, locationLabel);

                    
                    content.getChildren().add(details);
                    setGraphic(content);
                }
            }
        });
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}