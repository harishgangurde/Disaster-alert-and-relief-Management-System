package com.thinkspark.view;

import com.thinkspark.dao.NewsDao;
import com.thinkspark.model.NewsArticle;

import javafx.application.Application;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DisasterHelperUI extends Application {

    private static final String BACKGROUND_COLOR = "#FFFFFF";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String SECONDARY_BLUE = "#007BFF";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String CARD_BACKGROUND_COLOR = "#F4F7FC";
    private static final String BORDER_COLOR = "#E2E8F0";
    private static final String ALERT_BACKGROUND_COLOR = "#FFFBEB";
    private static final String ALERT_TEXT_COLOR = "#B45309";

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox rootLayout = new VBox(70); 
        rootLayout.setPadding(new Insets(50, 80, 80, 80));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(rootLayout); 
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background: " + BACKGROUND_COLOR + ";");
        scrollPane.getStyleClass().add("edge-to-edge");

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

        Scene scene = new Scene(overallRoot, 1920, 1080); 
        primaryStage.setTitle("Safe Serve");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setScene(scene); 

        final Scene finalScene = scene; 
 
        Node header = createHeader(primaryStage, finalScene);

        Node heroSection = createHeroSection();

        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(70); 
        mainGrid.setVgap(70); 
        mainGrid.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(65);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(35);
        mainGrid.getColumnConstraints().addAll(col1, col2);

        VBox leftColumn = new VBox(70);
        leftColumn.getChildren().addAll(
            createQuickAccessModule(primaryStage, finalScene), 
            createNewsFeedModule()
        );

        VBox rightColumn = new VBox(70); 
        rightColumn.getChildren().addAll(
            createLiveAlertsModule(),
            createStatisticsModule(primaryStage, finalScene) 
        );

        mainGrid.add(leftColumn, 0, 0);
        mainGrid.add(rightColumn, 1, 0);

        rootLayout.getChildren().addAll(header, heroSection, mainGrid);

        floatingButton.setOnAction(e -> {
            new FloatingButtonPage(primaryStage, finalScene).start(primaryStage);
        });

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
        primaryStage.show();
    }

    private Node createHeader(Stage stage, Scene scene) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(25, 0, 35, 0));

        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logoView.setFitHeight(70);
        logoView.setPreserveRatio(true);

        Label logoLabel = new Label("Safe Serve");
        logoLabel.setFont(getFont(44, FontWeight.BOLD)); 
        logoLabel.setTextFill(Color.web(PRIMARY_BLUE));

        HBox logoBox = new HBox(25, logoView, logoLabel); 
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navLinks = new HBox(70);
        navLinks.setAlignment(Pos.CENTER);
        
        Button homeLink = createNavLink("Home");
        homeLink.setOnAction(e -> this.start(stage)); 

        Button assistanceLink = createNavLink("Assistance");
        assistanceLink.setOnAction(e -> new AssistanceView(stage, scene, "Citizen").start(stage));

        Button resourcesLink = createNavLink("Resources");
        resourcesLink.setOnAction(e -> new ResourcesView(stage, scene).start(stage));

        Button aboutUsLink = createNavLink("About Us");
        aboutUsLink.setOnAction(e -> new AboutUsView(stage, scene).start(stage));

        navLinks.getChildren().addAll(homeLink, assistanceLink, resourcesLink, aboutUsLink);

        HBox authButtons = new HBox(30);
        authButtons.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Log In");
        loginBtn.setFont(getFont(20, FontWeight.BOLD)); 
        loginBtn.setTextFill(Color.web(PRIMARY_BLUE));
        loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 3; -fx-background-radius: 35; -fx-border-radius: 35; -fx-padding: 15 35; -fx-cursor: hand;"); // Increased padding, border width and radius

        Button signupBtn = new Button("Sign Up");
        signupBtn.setFont(getFont(20, FontWeight.BOLD));
        signupBtn.setTextFill(Color.web(FONT_COLOR_LIGHT));
        signupBtn.setStyle("-fx-background-color: " + SECONDARY_BLUE + "; -fx-background-radius: 35; -fx-padding: 15 35; -fx-cursor: hand;"); // Increased padding and radius
        
        loginBtn.setOnAction(e -> {
            new LoginUI().start(stage);
        });

        signupBtn.setOnAction(e -> {
            new SignUpForm().start(stage);
        });
        
        authButtons.getChildren().addAll(loginBtn, signupBtn);
        HBox.setMargin(navLinks, new Insets(0, 80, 0, 0)); 

        header.getChildren().addAll(logoBox, spacer, navLinks, authButtons);
        return header;
    }
    
    private Button createNavLink(String text) {
        Button link = new Button(text);
        link.setFont(getFont(22, FontWeight.MEDIUM)); 
        link.setTextFill(Color.web(FONT_COLOR_DARK));
        link.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
        link.setOnMouseEntered(e -> link.setUnderline(true));
        link.setOnMouseExited(e -> link.setUnderline(false));
        return link;
    }

    private Node createHeroSection() {
        VBox heroBox = new VBox(35); 
        heroBox.setAlignment(Pos.CENTER);
        heroBox.setPadding(new Insets(90, 70, 90, 70));
        heroBox.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 30;"); 

        Label title = new Label("We Are Here To Help");
        title.setFont(getFont(66, FontWeight.BOLD)); 
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Label subtitle = new Label("Your trusted partner in navigating emergencies. Get access to critical information, resources, and support when you need it most.");
        subtitle.setFont(getFont(24, FontWeight.NORMAL)); 
        subtitle.setTextFill(Color.web(FONT_COLOR_DARK));
        subtitle.setOpacity(0.8);
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(1100); 
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        heroBox.getChildren().addAll(title, subtitle);
        return heroBox;
    }

    private Node createQuickAccessModule(Stage stage, Scene scene) {
        VBox card = createDashboardCard("Quick Access");
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(35); 
        buttonGrid.setVgap(35); 
        buttonGrid.setPadding(new Insets(30, 0, 0, 0)); 
        
        Button disasterInfoBtn = createQuickAccessButton("Disaster Info Type");
        disasterInfoBtn.setOnAction(e -> new DisasterInfoTypeView(stage, scene).start(stage));

        Button safeZoneBtn = createQuickAccessButton("Nearest Safe Zone");
        safeZoneBtn.setOnAction(e -> new NearestSafeZoneView(stage, scene).start(stage));

        Button reliefResourcesBtn = createQuickAccessButton("Relief Resources");
        reliefResourcesBtn.setOnAction(e -> new ReliefResourcesView(stage, scene).start(stage));
        
        Button donateBtn = createQuickAccessButton("Donate Now");
        donateBtn.setOnAction(e -> new DonationsView(stage, scene).start(stage));

        buttonGrid.add(disasterInfoBtn, 0, 0);
        buttonGrid.add(safeZoneBtn, 1, 0);
        buttonGrid.add(reliefResourcesBtn, 0, 1);
        buttonGrid.add(donateBtn, 1, 1);

        card.getChildren().add(buttonGrid);
        return card;
    }
    
    private Button createQuickAccessButton(String text) {
        Button button = new Button(text);
        button.setFont(getFont(22, FontWeight.BOLD));
        button.setTextFill(Color.web(FONT_COLOR_DARK));
        button.setStyle("-fx-background-color: " + BACKGROUND_COLOR + "; -fx-background-radius: 18; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 18; -fx-cursor: hand;"); // Increased radius
        button.setPrefSize(400, 110); 
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);
        return button;
    }

    private Node createLiveAlertsModule() {
        VBox card = createDashboardCard("Live Alerts");
        card.setStyle("-fx-background-color: " + ALERT_BACKGROUND_COLOR + "; -fx-background-radius: 25; -fx-padding: 40;"); // Increased padding and radius
        
        Label title = (Label) card.getChildren().get(0);
        title.setTextFill(Color.web(ALERT_TEXT_COLOR));
        
        VBox alertsContainer = new VBox(30); 
        alertsContainer.setPadding(new Insets(25, 0, 0, 0)); 
        
        alertsContainer.getChildren().add(createAlertItem("High wind advisory in effect until 8 PM."));
        alertsContainer.getChildren().add(createAlertItem("Road closure on Main St due to flooding."));
        alertsContainer.getChildren().add(createAlertItem("Community shelter open at the civic center."));
        
        card.getChildren().add(alertsContainer);
        return card;
    }
    
    private Node createAlertItem(String text) {
        Label alertLabel = new Label("• " + text);
        alertLabel.setFont(getFont(20, FontWeight.MEDIUM)); 
        alertLabel.setTextFill(Color.web(ALERT_TEXT_COLOR));
        alertLabel.setWrapText(true);
        return alertLabel;
    }

    private Node createStatisticsModule(Stage stage, Scene scene) {
        VBox card = createDashboardCard("Statistics");
        GridPane grid = new GridPane();
        grid.setHgap(35); 
        grid.setVgap(35); 
        grid.setPadding(new Insets(30, 0, 0, 0)); 
        
        grid.add(createStatItem("12K", "People Rescued"), 0, 0);
        grid.add(createStatItem("56K", "Requests Handled"), 1, 0);
        grid.add(createStatItem("343", "Active Volunteers"), 0, 1);
        grid.add(createStatItem("39K", "Resources Delivered"), 1, 1);

        card.getChildren().add(grid);

        // New button to open AnalyticsView
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
        VBox item = new VBox(-7);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Label numberLabel = new Label(number);
        numberLabel.setFont(getFont(48, FontWeight.BOLD));
        numberLabel.setTextFill(Color.web(PRIMARY_BLUE));
        
        Label textLabel = new Label(label);
        textLabel.setFont(getFont(20, FontWeight.NORMAL));
        textLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        item.getChildren().addAll(numberLabel, textLabel);
        return item;
    }

    private Node createNewsFeedModule() {
        VBox card = createDashboardCard("Latest News");
        card.setPadding(new Insets(40));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 25; " + 
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 6);"); 

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
                    vbox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1; -fx-padding: 15;"); // Attractive styling for each item

                    Label title = new Label(item.getTitle());
                    title.setWrapText(true);
                    title.setFont(DisasterHelperUI.getFont(24, FontWeight.BOLD)); 
                    title.setTextFill(Color.web(PRIMARY_BLUE)); 

                    Label description = new Label(item.getDescription());
                    description.setWrapText(true);
                    description.setFont(DisasterHelperUI.getFont(18, FontWeight.NORMAL));
                    description.setTextFill(Color.web(FONT_COLOR_DARK)); 

                    vbox.getChildren().addAll(title, description);
                    setGraphic(vbox);
                }
            }
        });

        listView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        listView.setFixedCellSize(200); 
        listView.setPrefHeight(600);

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600); 
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); 

        card.getChildren().add(scrollPane);
        return card;
    }
    
    private VBox createDashboardCard(String title) {
        VBox card = new VBox(25); 
        card.setPadding(new Insets(40)); 
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 25;"); 
        VBox.setVgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD)); 
        titleLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        
        card.getChildren().add(titleLabel);
        return card;
    }
}