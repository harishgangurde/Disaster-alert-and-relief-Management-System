package com.thinkspark.view;

import com.thinkspark.dao.DisasterDao;
import com.thinkspark.dao.TaskDao;
import com.thinkspark.dao.VolunteerDao;
import com.thinkspark.model.Disaster;
import com.thinkspark.model.Task;
import com.thinkspark.model.Volunteer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllRequestsView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String STATUS_PENDING_COLOR = "#DD6B20";
    private static final String STATUS_ASSIGNED_COLOR = "#3182CE";
    private static final String STATUS_COMPLETED_COLOR = "#38A169";
    private static final String ACTION_BUTTON_COLOR = "#38A169";

    private final Scene previousScene;
    private final Stage primaryStage;
    private ListView<RequestItem> listView;
    private ObservableList<RequestItem> requestItems;
    private final DisasterDao disasterDao;
    private final VolunteerDao volunteerDao;
    private final TaskDao taskDao;
    private final ObservableList<Volunteer> availableVolunteers;
    private final ObservableList<Task> allTasks;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    private static class RequestItem {
        private final Disaster disaster;
        private String status;

        public RequestItem(Disaster disaster, String status) {
            this.disaster = disaster;
            this.status = status;
        }

        public Disaster getDisaster() { return disaster; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

     private class RequestCell extends ListCell<RequestItem> {
        @Override
        protected void updateItem(RequestItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                HBox content = new HBox(25); 
                content.setAlignment(Pos.CENTER);
                content.setPadding(new Insets(20)); 
                content.setStyle("-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 1;");

                ImageView icon = new ImageView();
                icon.setFitHeight(60);
                icon.setFitWidth(60); 
                if (item.getDisaster() != null) {
                    String imgUrl = item.getDisaster().getImageUrl();
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        try {
                            icon.setImage(new Image(imgUrl));
                        } catch (Exception e) {
                            System.err.println("Error loading image from URL: " + imgUrl + ". Falling back to default logo.");
                            e.printStackTrace();
                            InputStream defaultLogoStream = getClass().getResourceAsStream("/logo.png");
                            if (defaultLogoStream != null) {
                                icon.setImage(new Image(defaultLogoStream));
                            } else {
                                System.err.println("Default logo.png not found in resources!");
                            }
                        }
                    } else {
                        InputStream defaultLogoStream = getClass().getResourceAsStream("/logo.png");
                        if (defaultLogoStream != null) {
                            icon.setImage(new Image(defaultLogoStream));
                        } else {
                            System.err.println("Default logo.png not found in resources!");
                        }
                    }
                }

                Label typeLabel = new Label(item.getDisaster() != null ? item.getDisaster().getType() : "N/A");
                typeLabel.setFont(AllRequestsView.getFont(20, FontWeight.BOLD));
                Label locationLabel = new Label(item.getDisaster() != null ? item.getDisaster().getLocation() : "N/A");
                locationLabel.setFont(AllRequestsView.getFont(18, FontWeight.NORMAL));
                VBox details = new VBox(8, typeLabel, locationLabel); 

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label statusLabel = new Label(item.getStatus() != null ? item.getStatus().toUpperCase() : "N/A");
                statusLabel.setFont(AllRequestsView.getFont(14, FontWeight.BOLD));
                statusLabel.setTextFill(Color.WHITE);
                statusLabel.setPadding(new Insets(6, 12, 6, 12)); 

                String statusLower = item.getStatus() != null ? item.getStatus().toLowerCase() : "";
                String statusColor;
                switch (statusLower) {
                    case "pending":
                        statusColor = STATUS_PENDING_COLOR;
                        break;
                    case "assigned":
                        statusColor = STATUS_ASSIGNED_COLOR;
                        break;
                    case "completed":
                        statusColor = STATUS_COMPLETED_COLOR;
                        break;
                    default:
                        statusColor = FONT_COLOR_DARK;
                        break;
                }
                statusLabel.setStyle("-fx-background-color: " + statusColor + "; -fx-background-radius: 8;"); 
                content.getChildren().addAll(icon, details, spacer, statusLabel);

                if ("pending".equalsIgnoreCase(item.getStatus())) {
                    Button assignButton = new Button("Assign Task");
                    assignButton.setFont(AllRequestsView.getFont(16, FontWeight.BOLD)); 
                    assignButton.setTextFill(Color.WHITE);
                    assignButton.setStyle("-fx-background-color: " + ACTION_BUTTON_COLOR + "; -fx-background-radius: 8; -fx-cursor: hand;"); // Increased radius
                    assignButton.setPadding(new Insets(10, 20, 10, 20));
                    assignButton.setOnAction(e -> assignTaskToVolunteer(item));
                    content.getChildren().add(assignButton);
                }
                setGraphic(content);
            }
        }
    }

    private void assignTaskToVolunteer(RequestItem request) {
        ObservableList<Volunteer> currentlyAvailableVolunteers = availableVolunteers.stream()
            .filter(volunteer -> allTasks.stream()
                .noneMatch(task -> task.getAssignedTo().equals(volunteer.getName()) && !task.getStatus().equalsIgnoreCase("completed")))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));


        if (currentlyAvailableVolunteers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "There are no available volunteers to assign. All are currently assigned tasks or none registered.", ButtonType.OK).show();
            return;
        }

        ChoiceDialog<Volunteer> dialog = new ChoiceDialog<>(currentlyAvailableVolunteers.get(0), currentlyAvailableVolunteers);
        dialog.setTitle("Assign Task to Volunteer");
        dialog.setHeaderText("Select a volunteer for this task:");
        dialog.setContentText("Task: " + request.getDisaster().getType() + " at " + request.getDisaster().getLocation());

        Optional<Volunteer> result = dialog.showAndWait();
        result.ifPresent(volunteer -> {
            String taskDescription = request.getDisaster().getType() + ": " + request.getDisaster().getDescription();

            Task newTask = new Task(taskDescription, volunteer.getEmail(), request.getDisaster().getFirebaseDocId());
            taskDao.saveTask(newTask);

            disasterDao.updateDisasterStatus(request.getDisaster().getFirebaseDocId(), "Assigned");

            new Alert(Alert.AlertType.INFORMATION, "Task successfully assigned to " + volunteer.getName() + ".", ButtonType.OK).show();
        });
    }

    public AllRequestsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.disasterDao = new DisasterDao();
        this.volunteerDao = new VolunteerDao();
        this.taskDao = new TaskDao();
        this.availableVolunteers = FXCollections.observableArrayList();
        this.allTasks = FXCollections.observableArrayList();
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(40); 
        rootLayout.setPadding(new Insets(60, 80, 80, 80)); 
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createRequestsList());

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve");

        ObservableList<Disaster> disasterReports = FXCollections.observableArrayList();
        disasterDao.listenForDisasterReports(disasterReports);
        disasterReports.addListener((ListChangeListener<Disaster>) c -> {
            requestItems.setAll(
                disasterReports.stream()
                    .map(disaster -> new RequestItem(disaster, disaster.getStatus()))
                    .collect(Collectors.toList())
            );
        });
        
        volunteerDao.listenForVolunteers(availableVolunteers);

        taskDao.listenForTasks(allTasks);
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0));

        Label title = new Label("📋 All Disaster Reports");
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

    private Node createRequestsList() {
        VBox card = new VBox(15); 
        card.setPadding(new Insets(40)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 20, 0, 0, 8);"); 

        requestItems = FXCollections.observableArrayList();

        listView = new ListView<>(requestItems);
        listView.setPlaceholder(new Label("No disaster reports to display."));
        listView.setPrefHeight(600); 
        listView.setCellFactory(param -> new RequestCell());
        listView.setStyle("-fx-background-color: transparent;");

        card.getChildren().add(listView);
        
        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        
        return container;
    }
}