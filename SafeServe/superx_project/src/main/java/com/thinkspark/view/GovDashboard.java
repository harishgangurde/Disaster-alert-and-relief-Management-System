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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GovDashboard extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_COLOR = "#E53E3E";
    private static final String SECONDARY_BLUE = "#007BFF";

    private Stage primaryStage;
    private Scene dashboardScene;
    private DisasterDao disasterDao;
    private ObservableList<Disaster> liveDisasterReportsList;
    private String userName;

    public GovDashboard() {
        this.userName = "Admin"; 
    }

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public GovDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "Admin" : userName;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        disasterDao = new DisasterDao();
        liveDisasterReportsList = FXCollections.observableArrayList();
        disasterDao.listenForDisasterReports(liveDisasterReportsList);

        VBox rootLayout = new VBox(60);
        rootLayout.setPadding(new Insets(40, 80, 80, 80)); 
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createOverviewSection(), createMainGrid());

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

        mainGrid.add(createKeyActionsModule(), 0, 0);
        mainGrid.add(createDisasterReportsModule(), 1, 0);
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
        
        MenuButton profileButton = new MenuButton("Admin");
        profileButton.setFont(getFont(20, FontWeight.MEDIUM)); 
        MenuItem profileItem = new MenuItem("Profile");
        MenuItem logoutItem = new MenuItem("Log Out");
        profileButton.getItems().addAll(profileItem, logoutItem);
        
        
        profileItem.setOnAction(e -> new ProfileView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        logoutItem.setOnAction(e -> {
            new DisasterHelperUI().start(primaryStage);
        });

        header.getChildren().addAll(logoBox, spacer, profileButton);
        return header;
    }

    private Node createOverviewSection() {
        VBox overviewBox = new VBox(25); 
        overviewBox.setPadding(new Insets(50)); 
        overviewBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 20;"); 
        Label title = new Label("Command Center Overview");
        title.setFont(getFont(48, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));
        Label subtitle = new Label("Monitor real-time reports, manage partner NGOs, and broadcast critical alerts.");
        subtitle.setFont(getFont(22, FontWeight.NORMAL)); 
        subtitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
        overviewBox.getChildren().addAll(title, subtitle);
        return overviewBox;
    }

    private Node createKeyActionsModule() {
    VBox card = createDashboardCard("Key Actions");

    GridPane buttonGrid = new GridPane();
    buttonGrid.setHgap(30);
    buttonGrid.setVgap(30); 
    buttonGrid.setPadding(new Insets(25, 0, 0, 0)); 

    Button broadcastButton = createQuickAccessButton("Broadcast Alert", true);
    broadcastButton.setOnAction(e -> new BroadcastAlertsView(primaryStage, dashboardScene).start(primaryStage));

    Button manageNgosButton = createQuickAccessButton("Manage NGOs", false);
    manageNgosButton.setOnAction(e -> new ManageNgosView(primaryStage, dashboardScene).start(primaryStage));

    buttonGrid.add(broadcastButton, 0, 0);
    buttonGrid.add(manageNgosButton, 1, 0);

    // Motivational Quote
    Label quote = new Label("“Preparedness is the calm before the storm.”");
    quote.setFont(getFont(18, FontWeight.NORMAL));
    quote.setTextFill(Color.web("#A0AEC0"));
    quote.setWrapText(true);
    quote.setAlignment(Pos.CENTER);
    quote.setMaxWidth(Double.MAX_VALUE);

    // Notes Section
    Label notesTitle = new Label("Admin Notes");
    notesTitle.setFont(getFont(20, FontWeight.BOLD));
    notesTitle.setTextFill(Color.web(FONT_COLOR_MEDIUM));
    TextArea notesArea = new TextArea();
    notesArea.setPromptText("Write reminders or notes here...");
    notesArea.setPrefRowCount(4);
    notesArea.setWrapText(true);
    notesArea.setStyle("-fx-background-radius: 10; -fx-border-color: #CBD5E0; -fx-border-radius: 10;");

    VBox.setMargin(quote, new Insets(40, 0, 10, 0));
    VBox.setMargin(notesTitle, new Insets(30, 0, 5, 0));
    VBox.setMargin(notesArea, new Insets(0, 0, 0, 0));

    card.getChildren().addAll(buttonGrid, quote, notesTitle, notesArea);
    return card;
}


    private Button createQuickAccessButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(getFont(20, FontWeight.BOLD));
        button.setPrefSize(300, 100);
        GridPane.setHgrow(button, Priority.ALWAYS);
        if (isPrimary) {
            button.setTextFill(Color.web(FONT_COLOR_LIGHT));
            button.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 15; -fx-cursor: hand;"); // Increased radius
        } else {
            button.setTextFill(Color.web(FONT_COLOR_DARK));
            button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;"); // Increased radius
        }
        return button;
    }
    
    private Node createDisasterReportsModule() {
        VBox card = createDashboardCard("Incoming Disaster Reports");
        ListView<Disaster> reportsList = new ListView<>(liveDisasterReportsList);
        reportsList.setPlaceholder(new Label("No new disaster reports from citizens."));
        reportsList.setPrefHeight(600); 
        reportsList.setCellFactory(param -> new ListCell<Disaster>() {
            @Override
            protected void updateItem(Disaster item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text type = new Text(item.getType() + " at " + item.getLocation());
                    type.setFont(GovDashboard.getFont(18, FontWeight.BOLD)); 
                    type.setFill(Color.web(ALERT_COLOR));
                    
                    Text description = new Text(item.getDescription());
                    description.setWrappingWidth(param.getWidth() - 60); 
                    description.setFont(GovDashboard.getFont(16, FontWeight.NORMAL));
                    
                    VBox content = new VBox(10, type, description); 
                    content.setPadding(new Insets(10)); 
                    setGraphic(content);
                }
            }
        });
        VBox.setVgrow(reportsList, Priority.ALWAYS);
        card.getChildren().add(reportsList);
        return card;
    }

    private Node createStatisticsModule(Stage stage, Scene scene) { 
        VBox card = createDashboardCard("Key Performance Indicators");
        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setSpacing(60); 
        statsBox.setPadding(new Insets(25, 0, 0, 0)); 
        HBox.setHgrow(statsBox, Priority.ALWAYS);

        statsBox.getChildren().addAll(
            createStatItem("12K", "People Rescued"),
            createStatItem("56K", "Requests Handled"),
            createStatItem("3", "Verified NGOs"),
            createStatItem("343", "Total Volunteers")
        );

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