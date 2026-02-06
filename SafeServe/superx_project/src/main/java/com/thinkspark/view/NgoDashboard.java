package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.dao.DisasterDao;
import com.thinkspark.model.Alert;
import com.thinkspark.model.Disaster;

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

public class NgoDashboard extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";
    private static final String PRIMARY_ACCENT_ORANGE = "#FCA311";
    private static final String STATUS_COMPLETED_COLOR = "#38A169";
    private static final String SECONDARY_BLUE = "#007BFF";

    private Stage primaryStage;
    private Scene dashboardScene;

    private AlertDao alertDao;
    private DisasterDao disasterDao;
    private ObservableList<Alert> liveAlertsList;
    private ObservableList<Disaster> liveDisasterReportsList;
    private String userName;

    private NgoDashboard() {
        this.userName = "NGO";
    }


    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public NgoDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "NGO" : userName;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        alertDao = new AlertDao();
        disasterDao = new DisasterDao();
        liveAlertsList = FXCollections.observableArrayList();
        liveDisasterReportsList = FXCollections.observableArrayList();
        alertDao.listenForAlerts(liveAlertsList);
        disasterDao.listenForDisasterReports(liveDisasterReportsList);


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
        primaryStage.setScene(dashboardScene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();

        final Scene finalDashboardScene = dashboardScene;
        floatingButton.setOnAction(e -> {
            new FloatingButtonPage(primaryStage, finalDashboardScene).start(primaryStage);
        });
    }

    private GridPane createMainGrid() {
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(50);
        mainGrid.setVgap(50); 
        mainGrid.setAlignment(Pos.CENTER);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createLiveFeedsModule(), 1, 0); 
        mainGrid.add(createStatisticsModule(primaryStage, dashboardScene), 0, 1, 2, 1);
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
        String[] navItems = { "Dashboard", "Volunteers", "Reports" };
        for (String item : navItems) {
            Label link = new Label(item);
            link.setFont(getFont(20, FontWeight.MEDIUM));
            link.setTextFill(Color.web(FONT_COLOR_DARK));
            link.setStyle("-fx-cursor: hand;");
            link.setOnMouseEntered(e -> link.setUnderline(true));
            link.setOnMouseExited(e -> link.setUnderline(false));

            if ("Dashboard".equals(item)) {
                link.setOnMouseClicked(e -> primaryStage.setScene(dashboardScene));
            } else if ("Volunteers".equals(item)) {
                link.setOnMouseClicked(e -> new ManageVolunteersView(primaryStage, dashboardScene).start(primaryStage));
            } else if ("Reports".equals(item)) {
                link.setOnMouseClicked(e -> new AllRequestsView(primaryStage, dashboardScene).start(primaryStage));
            }

            navLinks.getChildren().add(link);
        }

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

        Label title = new Label("Operations Dashboard");
        title.setFont(getFont(48, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Coordinate your team, manage requests, and track your impact in real-time.");
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

    Button allRequestsButton = createQuickAccessButton("View & Assign Tasks", true);
    allRequestsButton.setOnAction(e -> new AllRequestsView(primaryStage, dashboardScene).start(primaryStage));

    Button manageVolunteersButton = createQuickAccessButton("Manage Volunteers", false);
    manageVolunteersButton.setOnAction(e -> new ManageVolunteersView(primaryStage, dashboardScene).start(primaryStage));

    Button viewMapButton = createQuickAccessButton("View Map", false);
    viewMapButton.setOnAction(e -> new MapView(primaryStage, dashboardScene).start(primaryStage));

    Button viewDonationsButton = createQuickAccessButton("View Donations", false);
    viewDonationsButton.setOnAction(e -> new ViewDonationsView(primaryStage, dashboardScene).start(primaryStage));

    buttonGrid.add(allRequestsButton, 0, 0);
    buttonGrid.add(manageVolunteersButton, 1, 0);
    buttonGrid.add(viewMapButton, 0, 1);
    buttonGrid.add(viewDonationsButton, 1, 1);

    card.getChildren().add(buttonGrid);

    // --- Add an image below the buttons to fill the space ---
    ImageView quickAccessImage = new ImageView(new Image(getClass().getResourceAsStream("/Assets/Images/ngoMap.jpg")));
    quickAccessImage.setPreserveRatio(true);
    quickAccessImage.setFitWidth(600); // Adjust as per design
    quickAccessImage.setSmooth(true);
    quickAccessImage.setCache(true);
    VBox.setMargin(quickAccessImage, new Insets(30, 0, 0, 0));

    card.getChildren().add(quickAccessImage); // Add image to the card
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

    private Node createLiveFeedsModule() {
        VBox card = createDashboardCard("Live Feeds");

        Label disasterReportsTitle = new Label("Citizen Disaster Reports");
        disasterReportsTitle.setFont(getFont(20, FontWeight.BOLD));

        ListView<Disaster> disasterReportsList = new ListView<>(liveDisasterReportsList);
        disasterReportsList.setPlaceholder(new Label("No new disaster reports."));
        disasterReportsList.setPrefHeight(350);
        disasterReportsList.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text typeAndLocation = new Text(item.getType() + " at " + item.getLocation());
                    typeAndLocation.setFont(NgoDashboard.getFont(18, FontWeight.BOLD));

                    String status = item.getStatus();
                    String statusColor = ALERT_COLOR;
                    if (status != null) { 
                        if (status.equalsIgnoreCase("assigned")) {
                            statusColor = PRIMARY_BLUE;
                        } else if (status.equalsIgnoreCase("completed")) {
                            statusColor = STATUS_COMPLETED_COLOR;
                        }
                    }
                    typeAndLocation.setFill(Color.web(statusColor));

                    Text description = new Text(item.getDescription());
                    description.setWrappingWidth(param.getWidth() - 50);
                    description.setFont(NgoDashboard.getFont(16, FontWeight.NORMAL));

                    Text statusText = new Text("Status: " + (status != null ? status : "Unknown"));
                    statusText.setFont(NgoDashboard.getFont(14, FontWeight.MEDIUM));
                    statusText.setFill(Color.web(statusColor));

                    VBox content = new VBox(10, typeAndLocation, description, statusText);
                    content.setPadding(new Insets(10));
                    setGraphic(content);
                }
            }
        });

        Label govAlertsTitle = new Label("Government Alerts");
        govAlertsTitle.setFont(getFont(20, FontWeight.BOLD));
        govAlertsTitle.setPadding(new Insets(20, 0, 0, 0));

        ListView<Alert> govAlertsList = new ListView<>(liveAlertsList);
        govAlertsList.setPlaceholder(new Label("No new government alerts."));
        govAlertsList.setPrefHeight(350);
        govAlertsList.setCellFactory(param -> new ListCell<Alert>() {
            @Override
            protected void updateItem(Alert item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text title = new Text(item.getTitle());
                    title.setFont(NgoDashboard.getFont(18, FontWeight.BOLD));

                    Text message = new Text(item.getMessage());
                    message.setWrappingWidth(param.getWidth() - 50);
                    message.setFont(NgoDashboard.getFont(16, FontWeight.NORMAL));

                    String priorityColor = "High".equalsIgnoreCase(item.getPriority()) ? PRIMARY_ACCENT_ORANGE : FONT_COLOR_DARK;
                    title.setFill(Color.web(priorityColor));

                    VBox content = new VBox(10, title, message);
                    content.setPadding(new Insets(10));
                    setGraphic(content);
                }
            }
        });

        VBox.setVgrow(disasterReportsList, Priority.ALWAYS);
        VBox.setVgrow(govAlertsList, Priority.ALWAYS);

        card.getChildren().addAll(disasterReportsTitle, disasterReportsList, govAlertsTitle, govAlertsList);
        return card;
    }

    private Node createStatisticsModule(Stage stage, Scene scene) {
        VBox card = createDashboardCard("Your Organization's Impact");
        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(60);
        statsBox.setPadding(new Insets(25, 0, 0, 0));
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        String[][] statsData = {
            { "1.2K", "People Aided" },
            { "4.8K", "Requests Fulfilled" },
            { "343", "Active Volunteers" },
            { "8.2K", "Items Delivered" }
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