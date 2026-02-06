package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.dao.TaskDao;
import com.thinkspark.model.Alert;
import com.thinkspark.model.Task;
import com.thinkspark.dao.DisasterDao; // NEW IMPORT

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VolunteerDashboard extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";
    private static final String STATUS_COMPLETED_COLOR = "#38A169"; 
    private static final String STATUS_ASSIGNED_COLOR = "#3182CE"; 
    private static final String SECONDARY_BLUE = "#007BFF";

    private Stage primaryStage;
    private Scene dashboardScene;
    private ListView<Task> tasksListView;
    private String userName;

    private AlertDao alertDao;
    private TaskDao taskDao;
    private DisasterDao disasterDao; 
    private ObservableList<Alert> liveAlertsList;
    private ObservableList<Task> assignedTasksData; 

    public VolunteerDashboard() {
        this.userName = "Volunteer";
    }

    public VolunteerDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "Volunteer" : userName;
    }


    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        alertDao = new AlertDao();
        taskDao = new TaskDao();
        disasterDao = new DisasterDao(); 
        liveAlertsList = FXCollections.observableArrayList();
        assignedTasksData = FXCollections.observableArrayList(); 

        alertDao.listenForAlerts(liveAlertsList);
        taskDao.listenForTasks(assignedTasksData, this.userName); 

        VBox rootLayout = new VBox(60); 
        rootLayout.setPadding(new Insets(40, 80, 80, 80));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node overviewSection = createOverviewSection();
        GridPane mainGrid = createMainGrid();
        rootLayout.getChildren().addAll(header, overviewSection, mainGrid);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Button floatingButton = new Button("🤖"); 
        floatingButton.setStyle(
            "-fx-background-color: " + SECONDARY_BLUE + "; " + 
            "-fx-background-radius: 50%; " + 
            "-fx-min-width: 90px; -fx-min-height: 90px; " + 
            "-fx-max-width: 90px; -fx-max-height: 90px; " + 
            "-fx-font-size: 36px; " + 
            "-fx-text-fill: " + FONT_COLOR_LIGHT + "; " + 
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 20, 0, 0, 10); " + 
            "-fx-cursor: hand;"
        );
        StackPane.setAlignment(floatingButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(floatingButton, new Insets(0, 60, 180, 0));

        StackPane overallRoot = new StackPane();
        overallRoot.getChildren().addAll(scrollPane, floatingButton);

        dashboardScene = new Scene(overallRoot,1920,1080);
        primaryStage.setTitle("Safe Serve");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setScene(dashboardScene);

        tasksListView.setItems(assignedTasksData); 

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();

        final Scene finalDashboardScene = dashboardScene; 
        floatingButton.setOnAction(e -> {
            new FloatingButtonPage(primaryStage, finalDashboardScene).start(primaryStage);
        });
    }

    private GridPane createMainGrid(){
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(50);
        mainGrid.setVgap(50); 
        mainGrid.setAlignment(Pos.CENTER);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createAssignedTasksModule(), 1, 0);
        mainGrid.add(createTeamStatsModule(primaryStage, dashboardScene), 0, 1); 
        mainGrid.add(createLiveAlertsModule(), 1, 1);

        return mainGrid;
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 0, 30, 0)); 

        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logoView.setFitHeight(60);
        logoView.setPreserveRatio(true);

        Label logoLabel = new Label("Safe Serve");
        logoLabel.setFont(getFont(42, FontWeight.BOLD)); 
        logoLabel.setTextFill(Color.web(PRIMARY_BLUE));

        HBox logoBox = new HBox(20, logoView, logoLabel); 
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(50);
        navLinks.setAlignment(Pos.CENTER);

        Label dashboardLink = new Label("Dashboard");
        dashboardLink.setFont(getFont(20, FontWeight.MEDIUM));
        dashboardLink.setOnMouseClicked(e -> primaryStage.setScene(dashboardScene));

        navLinks.getChildren().add(dashboardLink);

        MenuButton profileButton = new MenuButton("Profile");
        profileButton.setFont(getFont(20, FontWeight.MEDIUM));
        MenuItem profileItem = new MenuItem("Profile");
        MenuItem logoutItem = new MenuItem("Log Out");
        profileButton.getItems().addAll(profileItem, logoutItem);

        profileItem.setOnAction(e -> new ProfileView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        logoutItem.setOnAction(e -> {
            new DisasterHelperUI().start(primaryStage);
        });

        navLinks.getChildren().add(profileButton);

        header.getChildren().addAll(logoBox, spacer, navLinks);
        return header;
    }

    private Node createOverviewSection() {
        VBox overviewBox = new VBox(25);
        overviewBox.setAlignment(Pos.CENTER_LEFT);
        overviewBox.setPadding(new Insets(50));
        overviewBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 20; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;"); // Increased radius

        Label title = new Label("Welcome, " + this.userName + "!");
        title.setFont(getFont(48, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Thank you for your dedication. Here are your tasks and team progress.");
        subtitle.setFont(getFont(22, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        subtitle.setWrapText(true);

        overviewBox.getChildren().addAll(title, subtitle);
        return overviewBox;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Actions");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(30);
        buttonGrid.setVgap(30);
        buttonGrid.setPadding(new Insets(25, 0, 0, 0));

        Button reportButton = createQuickAccessButton("🚨 Report Incident", true);
        reportButton.setOnAction(e -> new ReportDisasterView(primaryStage, dashboardScene, this.userName).start(primaryStage));

        Button requestHelpButton = createQuickAccessButton("🤝 Request Help", false);
        requestHelpButton.setOnAction(e -> new AssistanceView(primaryStage, dashboardScene, this.userName).start(primaryStage));

        Button logHoursButton = createQuickAccessButton("📋 Log Hours", false);
        logHoursButton.setOnAction(e -> new LogHoursView(primaryStage, dashboardScene).start(primaryStage));

        Button viewMapButton = createQuickAccessButton("🗺️ View Map", false);
        viewMapButton.setOnAction(e -> new MapView(primaryStage, dashboardScene).start(primaryStage));

        buttonGrid.add(reportButton, 0, 0);
        buttonGrid.add(requestHelpButton, 1, 0);
        buttonGrid.add(viewMapButton, 0, 1);
        buttonGrid.add(logHoursButton, 1, 1);

        card.getChildren().add(buttonGrid);
        return card;
    }

    private Button createQuickAccessButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(getFont(20, FontWeight.BOLD));
        button.setPrefSize(300, 100);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);

        String style;
        if (isPrimary) {
            button.setTextFill(Color.web(FONT_COLOR_LIGHT));
            style = "-fx-background-color: " + PRIMARY_BLUE + ";";
        } else {
            button.setTextFill(Color.web(FONT_COLOR_DARK));
            style = "-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + ";";
        }

        button.setStyle(style + " -fx-background-radius: 15; -fx-border-radius: 15; -fx-cursor: hand;"); 
        return button;
    }

    private Node createAssignedTasksModule() {
        VBox card = createDashboardCard("Your Assigned Tasks");
        tasksListView = new ListView<>(); 
        tasksListView.setPlaceholder(new Label("You have no assigned tasks."));
        tasksListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    System.out.println("DEBUG VD: Displaying task: " + item.getDescription() + 
                                       " (Status: " + item.getStatus() + 
                                       ", AssignedTo: " + item.getAssignedTo() + 
                                       ", FirebaseID: " + item.getFirebaseDocId() + ")");

                    HBox content = new HBox(20); 
                    content.setAlignment(Pos.CENTER_LEFT);
                    
                    Label descriptionLabel = new Label(item.getDescription());
                    descriptionLabel.setFont(VolunteerDashboard.getFont(20, FontWeight.MEDIUM)); 
                    descriptionLabel.setTextFill(Color.web(FONT_COLOR_DARK));
                    descriptionLabel.setWrapText(true);
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button statusButton = new Button(item.getStatus());
                    statusButton.setFont(VolunteerDashboard.getFont(16, FontWeight.BOLD));
                    statusButton.setTextFill(Color.WHITE);
                    statusButton.setPadding(new Insets(8, 12, 8, 12)); 
                    String buttonColor;
                    if ("Completed".equalsIgnoreCase(item.getStatus())) {
                        buttonColor = STATUS_COMPLETED_COLOR;
                        statusButton.setDisable(true); 
                    } else {
                        buttonColor = STATUS_ASSIGNED_COLOR;
                        statusButton.setOnAction(e -> {
                            taskDao.updateTaskStatus(item.getFirebaseDocId(), "Completed");
                            disasterDao.updateDisasterStatus(item.getDisasterId(), "Completed");
                        });
                    }
                    statusButton.setStyle("-fx-background-color: " + buttonColor + "; -fx-background-radius: 10; -fx-cursor: hand;"); // Increased radius further

                    content.getChildren().addAll(descriptionLabel, spacer, statusButton);
                    setGraphic(content);
                }
            }
        });
        tasksListView.setPrefHeight(350);
        VBox.setVgrow(tasksListView, Priority.ALWAYS);
        card.getChildren().add(tasksListView);
        return card;
    }


    private Node createTeamStatsModule(Stage stage, Scene scene) {
        VBox card = createDashboardCard("Your Team's Contribution");
        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(60);
        statsBox.setPadding(new Insets(25, 0, 0, 0));
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        String[][] statsData = {
            { "48", "Hours Logged (You)" },
            { "212", "Hours Logged (Team)" },
            { "15", "Tasks Completed" },
            { "120", "People Assisted" }
        };

        for (String[] stat : statsData) {
            Node statItem = createStatItem(stat[0], stat[1]);
            HBox.setHgrow(statItem, Priority.ALWAYS);
            statsBox.getChildren().add(statItem);
        }

        card.getChildren().add(statsBox);

        Button viewAnalyticsButton = new Button("View Analytics");
        viewAnalyticsButton.setFont(getFont(20, FontWeight.BOLD));
        viewAnalyticsButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        viewAnalyticsButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        viewAnalyticsButton.setPrefHeight(60);
        viewAnalyticsButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(viewAnalyticsButton, new Insets(30, 0, 0, 0));

        viewAnalyticsButton.setOnAction(e -> new AnalyticsView(stage, scene).start(stage));

        card.getChildren().add(viewAnalyticsButton);

        return card;
    }

    private Node createStatItem(String number, String label) {
        VBox item = new VBox(-8);
        item.setAlignment(Pos.CENTER);

        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(60, FontWeight.BOLD));
        numberLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Label textLabel = new Label(label);
        textLabel.setFont(getFont(20, FontWeight.NORMAL));
        textLabel.setTextFill(Color.web(FONT_COLOR_MEDIUM));

        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private Node createLiveAlertsModule() {
        VBox card = createDashboardCard("Live Government Alerts");
        ListView<Alert> alertsList = new ListView<>(liveAlertsList);
        alertsList.setPlaceholder(new Label("No active government alerts."));

        alertsList.setCellFactory(param -> new ListCell<Alert>() {
            @Override
            protected void updateItem(Alert item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text title = new Text(item.getTitle());
                    title.setFont(VolunteerDashboard.getFont(20, FontWeight.BOLD));

                    Text message = new Text(item.getMessage());
                    message.setFont(VolunteerDashboard.getFont(20, FontWeight.NORMAL));
                    message.setWrappingWidth(param.getWidth() - 60);

                    if ("High".equalsIgnoreCase(item.getPriority())) {
                        title.setFill(Color.web(ALERT_COLOR));
                    } else {
                        title.setFill(Color.web(FONT_COLOR_DARK));
                    }
                    
                    VBox alertContent = new VBox(12, title, message);
                    alertContent.setPadding(new Insets(12));
                    setGraphic(alertContent);
                }
            }
        });
        alertsList.setPrefHeight(350);
        VBox.setVgrow(alertsList, Priority.ALWAYS);
        card.getChildren().add(alertsList);
        return card;
    }

    private VBox createDashboardCard(String title) {
        VBox card = new VBox(25); 
        card.setPadding(new Insets(35)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " + 
                      "-fx-border-color: " + BORDER_COLOR + "; " +
                      "-fx-border-width: 1;");
        GridPane.setHgrow(card, Priority.ALWAYS);
        GridPane.setVgrow(card, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));

        card.getChildren().add(titleLabel);
        return card;
    }
}