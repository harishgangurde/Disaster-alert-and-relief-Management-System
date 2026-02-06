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

public class MyTasksView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String PRIORITY_HIGH_COLOR = "#DD6B20"; 
    private static final String PRIORITY_MEDIUM_COLOR = "#3182CE"; 
    private static final String PRIORITY_LOW_COLOR = "#4A5568";
    private static final String STATUS_COMPLETED_COLOR = "#38A169"; 

    private final Scene previousScene;
    private final Stage primaryStage;


    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private static class TaskItem {
        private final String description;
        private final String priority;
        private final String status;

        public TaskItem(String description, String priority, String status) {
            this.description = description;
            this.priority = priority;
            this.status = status;
        }

        public String getDescription() { return description; }
        public String getPriority() { return priority; }
        public String getStatus() { return status; }
    }

    private static class TaskCell extends ListCell<TaskItem> {
        private final HBox content = new HBox();
        private final VBox details = new VBox(5);
        private final Label descriptionLabel = new Label();
        private final Label priorityLabel = new Label();
        private final Button statusButton = new Button();
        private final Region spacer = new Region();

        public TaskCell() {
            super();
            descriptionLabel.setFont(MyTasksView.getFont(16, FontWeight.BOLD));
            descriptionLabel.setTextFill(Color.web(FONT_COLOR_DARK));
            
            priorityLabel.setFont(MyTasksView.getFont(12, FontWeight.BOLD));
            
            statusButton.setFont(MyTasksView.getFont(14, FontWeight.BOLD));
            statusButton.setMinWidth(120);

            details.getChildren().addAll(descriptionLabel, priorityLabel);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            content.setAlignment(Pos.CENTER);
            content.setSpacing(20);
            content.getChildren().addAll(details, spacer, statusButton);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");
        }

        @Override
        protected void updateItem(TaskItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                descriptionLabel.setText(item.getDescription());
                priorityLabel.setText(item.getPriority());
                statusButton.setText(item.getStatus());

                String priorityColor;
                switch (item.getPriority().toLowerCase()) {
                    case "high priority":
                        priorityColor = PRIORITY_HIGH_COLOR;
                        break;
                    case "medium priority":
                        priorityColor = PRIORITY_MEDIUM_COLOR;
                        break;
                    default: // Low Priority
                        priorityColor = PRIORITY_LOW_COLOR;
                        break;
                }
                priorityLabel.setTextFill(Color.web(priorityColor));

                // Style status button
                if ("completed".equalsIgnoreCase(item.getStatus())) {
                    statusButton.setStyle("-fx-background-color: " + STATUS_COMPLETED_COLOR + "; -fx-text-fill: white; -fx-background-radius: 6;");
                } else {
                    statusButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-background-radius: 6;");
                }

                setGraphic(content);
            }
        }
    }

    public MyTasksView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node tasksList = createTasksList();

        rootLayout.getChildren().addAll(header, tasksList);

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
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📝 My Tasks");
        title.setFont(getFont(32, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅️ Back to Dashboard");
        backButton.setFont(getFont(16, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 8;");
        backButton.setPadding(new Insets(8, 16, 8, 16));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Volunteer Dashboard | Disaster Relief");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createTasksList() {
        VBox card = new VBox(0);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 12; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        // Mock data for demonstration
        ObservableList<TaskItem> tasks = FXCollections.observableArrayList(
            new TaskItem("Distribute water bottles at Sector 7 shelter", "High Priority", "Mark as Complete"),
            new TaskItem("Assist with sandbagging at Riverbank", "Medium Priority", "Mark as Complete"),
            new TaskItem("Check-in with elderly residents on Oak St.", "Low Priority", "Mark as Complete"),
            new TaskItem("Transport medical supplies to North Clinic", "High Priority", "Completed"),
            new TaskItem("Organize donations at the main warehouse", "Medium Priority", "Completed")
        );

        ListView<TaskItem> listView = new ListView<>(tasks);
        listView.setCellFactory(param -> new TaskCell());
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }

}
