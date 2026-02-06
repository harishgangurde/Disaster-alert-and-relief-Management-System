package com.thinkspark.view;

import com.thinkspark.dao.AlertDao;
import com.thinkspark.dao.NewsDao;
import com.thinkspark.model.Alert;
import com.thinkspark.model.NewsArticle;

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


public class CitizenDashboard extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_ACCENT_COLOR = "#48BB78";
    private static final String ALERT_COLOR = "#E53E3E";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String SECONDARY_BLUE = "#007BFF"; 

    private Stage primaryStage;
    private Scene dashboardScene;
    private String userName;

    private AlertDao alertDao;
    private ObservableList<Alert> liveAlertsList;

    public CitizenDashboard() {
        this.userName = "Citizen";
    }

    public CitizenDashboard(String userName) {
        this.userName = (userName == null || userName.isEmpty()) ? "Citizen" : userName;
    }

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        alertDao = new AlertDao();
        liveAlertsList = FXCollections.observableArrayList();
        alertDao.listenForAlerts(liveAlertsList);

        VBox rootLayout = new VBox(70);
        rootLayout.setPadding(new Insets(50, 100, 100, 100));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createHeroSection(), createMainGrid());

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
        mainGrid.setHgap(60);
        mainGrid.setVgap(60);
        mainGrid.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        mainGrid.add(createQuickAccessModule(), 0, 0);
        mainGrid.add(createLiveAlertsModule(), 1, 0);
        mainGrid.add(createStatisticsModule(), 0, 1);
        mainGrid.add(createNewsFeedModule(), 1, 1);
        
        return mainGrid;
    }

    private Node createHeader() {
    HBox header = new HBox();
    header.setAlignment(Pos.CENTER_LEFT);
    header.setPadding(new Insets(30, 0, 40, 0));

    ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
    logoView.setFitHeight(70);
    logoView.setPreserveRatio(true);

    Label logoLabel = new Label("Safe Serve");
    logoLabel.setFont(getFont(48, FontWeight.BOLD));
    logoLabel.setTextFill(Color.web(PRIMARY_BLUE));

    HBox logoBox = new HBox(25, logoView, logoLabel);
    logoBox.setAlignment(Pos.CENTER_LEFT);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox navLinks = new HBox(60);
    navLinks.setAlignment(Pos.CENTER);
    String[] navItems = { "Home", "Assistance", "Resources", "About Us" };
    for (String item : navItems) {
        Label link = new Label(item);
        link.setFont(getFont(22, FontWeight.MEDIUM));
        link.setTextFill(Color.web(FONT_COLOR_DARK));
        link.setStyle("-fx-cursor: hand;");
        link.setOnMouseEntered(e -> link.setUnderline(true));
        link.setOnMouseExited(e -> link.setUnderline(false));
        
        if ("Home".equals(item)) {
             link.setOnMouseClicked(e -> new CitizenDashboard(this.userName).start(primaryStage));
        } else if ("Assistance".equals(item)) {
            link.setOnMouseClicked(e -> new AssistanceView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        } else if ("About Us".equals(item)) {
            link.setOnMouseClicked(e -> new AboutUsView(primaryStage, dashboardScene).start(primaryStage));
        } else if ("Resources".equals(item)) {
            link.setOnMouseClicked(e -> new ResourcesView(primaryStage, dashboardScene).start(primaryStage));
        }
        navLinks.getChildren().add(link);
    }

    MenuButton profileButton = new MenuButton(this.userName.substring(0, 1).toUpperCase());
    MenuItem profileItem = new MenuItem("Profile");
    MenuItem logoutItem = new MenuItem("Log Out");
    profileButton.getItems().addAll(profileItem, logoutItem);

    profileItem.setOnAction(e -> new ProfileView(primaryStage, dashboardScene, this.userName).start(primaryStage));
    logoutItem.setOnAction(e -> {
        new DisasterHelperUI().start(primaryStage);
    });

    profileButton.setFont(getFont(22, FontWeight.BOLD));
    profileButton.setStyle(
        "-fx-background-color: " + PRIMARY_BLUE + "; " +
        "-fx-text-fill: " + FONT_COLOR_LIGHT + "; " +
        "-fx-background-radius: 50em; -fx-padding: 0;"
    );
    profileButton.setMinSize(60, 60);
    profileButton.setMaxSize(60, 60);
    profileButton.setAlignment(Pos.CENTER);

    HBox.setMargin(navLinks, new Insets(0, 50, 0, 0));

    header.getChildren().addAll(logoBox, spacer, navLinks, profileButton);
    return header;
    }

    private Node createHeroSection() {
        StackPane heroPane = new StackPane();
        heroPane.setPadding(new Insets(70));
        heroPane.setStyle("-fx-background-color: linear-gradient(to right, " + PRIMARY_BLUE + ", #007ACC); -fx-background-radius: 20;");

        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER_LEFT);

        Label welcome = new Label("Welcome back, " + this.userName + "!");
        welcome.setFont(getFont(40, FontWeight.BOLD));
        welcome.setTextFill(Color.web(FONT_COLOR_LIGHT));

        Label subtitle = new Label("Your central hub for disaster assistance, resources, and real-time alerts.");
        subtitle.setFont(getFont(22, FontWeight.NORMAL));
        subtitle.setTextFill(Color.web(FONT_COLOR_LIGHT));
        subtitle.setOpacity(0.9);
        subtitle.setWrapText(true);

        Button reportButton = new Button("🚨 Report a Disaster");
        reportButton.setFont(getFont(24, FontWeight.BOLD));
        reportButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        reportButton.setStyle("-fx-background-color: " + ALERT_COLOR + "; -fx-background-radius: 15; -fx-cursor: hand;");
        reportButton.setPadding(new Insets(20, 35, 20, 35));
        VBox.setMargin(reportButton, new Insets(25, 0, 0, 0));

        reportButton.setOnAction(e -> new ReportDisasterView(primaryStage, dashboardScene, this.userName).start(primaryStage));

        content.getChildren().addAll(welcome, subtitle, reportButton);
        heroPane.getChildren().add(content);
        return heroPane;
    }

    private Node createQuickAccessModule() {
        VBox card = createDashboardCard("Quick Access");
        HBox buttonBox = new HBox(25);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Node mapButton = createQuickAccessButton("🗺️ Map", "View disaster map");
        mapButton.setOnMouseClicked(e -> new MapView(primaryStage, dashboardScene).start(primaryStage));
        Node donateButton = createQuickAccessButton("💖 Donate", "Support the cause");
        Node myRequestsButton = createQuickAccessButton("📋 My Requests", "See your requests");

        myRequestsButton.setOnMouseClicked(e -> new MyRequestsView(primaryStage, dashboardScene, this.userName).start(primaryStage));
        donateButton.setOnMouseClicked(e -> new DonationsView(primaryStage, dashboardScene).start(primaryStage));

        buttonBox.getChildren().addAll(mapButton, donateButton, myRequestsButton);
        card.getChildren().add(buttonBox);
        return card;
    }

    private Node createQuickAccessButton(String text, String subtext) {
        VBox buttonVBox = new VBox(12);
        buttonVBox.setAlignment(Pos.CENTER);
        buttonVBox.setPadding(new Insets(35, 25, 35, 25));
        buttonVBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 15; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;");
        HBox.setHgrow(buttonVBox, Priority.ALWAYS);

        buttonVBox.setOnMouseEntered(e -> buttonVBox.setStyle("-fx-background-color: #E2E8F0; -fx-background-radius: 15; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;"));
        buttonVBox.setOnMouseExited(e -> buttonVBox.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 15; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 15; -fx-cursor: hand;"));
        
        Label textLabel = new Label(text);
        textLabel.setFont(getFont(22, FontWeight.BOLD));
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        Label subtextLabel = new Label(subtext);
        subtextLabel.setFont(getFont(18, FontWeight.NORMAL));
        subtextLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        subtextLabel.setOpacity(0.7);

        buttonVBox.getChildren().addAll(textLabel, subtextLabel);
        return buttonVBox;
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
                    title.setFont(CitizenDashboard.getFont(20, FontWeight.BOLD));

                    Text message = new Text(item.getMessage());
                    message.setFont(CitizenDashboard.getFont(20, FontWeight.NORMAL));
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
        alertsList.setPrefHeight(600);
        VBox.setVgrow(alertsList, Priority.ALWAYS);
        card.getChildren().add(alertsList);
        return card;
    }

    private Node createStatisticsModule() {
        VBox card = createDashboardCard("Key Statistics");
        GridPane grid = new GridPane();
        grid.setHgap(35); 
        grid.setVgap(35); 
        grid.setPadding(new Insets(30, 0, 0, 0)); 
        
        grid.add(createStatItem("12K", "People Rescued", PRIMARY_BLUE), 0, 0); 
        grid.add(createStatItem("56K", "Requests Handled", PRIMARY_BLUE), 1, 0); 
        grid.add(createStatItem("343", "Active Volunteers", SECONDARY_ACCENT_COLOR), 0, 1); 
        grid.add(createStatItem("39K", "Resources Delivered", SECONDARY_ACCENT_COLOR), 1, 1); 

        card.getChildren().add(grid);

        Button viewAnalyticsButton = new Button("View Analytics");
        viewAnalyticsButton.setFont(getFont(20, FontWeight.BOLD));
        viewAnalyticsButton.setTextFill(Color.web(FONT_COLOR_LIGHT));
        viewAnalyticsButton.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-background-radius: 10; -fx-cursor: hand;");
        viewAnalyticsButton.setPrefHeight(60);
        viewAnalyticsButton.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(viewAnalyticsButton, new Insets(30, 0, 0, 0)); 

        viewAnalyticsButton.setOnAction(e -> new AnalyticsView(primaryStage, dashboardScene).start(primaryStage));

        card.getChildren().add(viewAnalyticsButton); 

        return card;
    }

    private Node createStatItem(String number, String label, String color) {
        VBox item = new VBox(-7);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(48, FontWeight.BOLD));
        numberLabel.setTextFill(Color.web(color)); 
        
        Label textLabel = new Label(label);
        textLabel.setFont(getFont(20, FontWeight.NORMAL));
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private Node createNewsFeedModule() {
        VBox card = createDashboardCard("Latest News on Disasters");
        NewsDao newsDao = new NewsDao();
        ObservableList<NewsArticle> newsList = newsDao.getNews();

        ListView<NewsArticle> listView = new ListView<>(newsList);
        listView.setCellFactory(param -> new ListCell<NewsArticle>() {
            @Override
            protected void updateItem(NewsArticle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(15);
                    vbox.setPadding(new Insets(15));
                    vbox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1; -fx-padding: 15;");

                    Label title = new Label(item.getTitle());
                    title.setWrapText(true);
                    title.setFont(CitizenDashboard.getFont(24, FontWeight.BOLD));
                    title.setTextFill(Color.web(PRIMARY_BLUE));

                    Label description = new Label(item.getDescription());
                    description.setWrapText(true);
                    description.setFont(CitizenDashboard.getFont(20, FontWeight.NORMAL));
                    description.setTextFill(Color.web(FONT_COLOR_DARK));

                    vbox.getChildren().addAll(title, description);
                    setGraphic(vbox);
                }
            }
        });

        listView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        listView.setFixedCellSize(220);
        listView.setPrefHeight(660);

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(660);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        card.getChildren().add(scrollPane);
        return card;
    }
    
    private VBox createDashboardCard(String title) {
        VBox card = new VBox(25);
        card.setPadding(new Insets(35));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 20; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 20, 0, 0, 8);");
        VBox.setVgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        card.getChildren().add(titleLabel);
        return card;
    }
}