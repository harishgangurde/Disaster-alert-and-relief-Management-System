package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class AnalyticsView extends Application {
    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String FONT_COLOR_MEDIUM = "#4A5568";
    private static final String SUCCESS_COLOR = "#38A169"; 
    private static final String WARNING_COLOR = "#FFC107";
    private static final String DANGER_COLOR = "#DC3545"; 

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public AnalyticsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(30);
        rootLayout.setPadding(new Insets(40));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        rootLayout.getChildren().addAll(createHeader(), createAnalyticsContent()); 

        Scene scene = new Scene(rootLayout,1920,1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Safe Serve | Analytics"); 
    }

    private Node createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label title = new Label("📊 Analytics & Insights");
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
            primaryStage.setTitle("Safe Serve | Analytics");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createAnalyticsContent() {
        VBox contentLayout = new VBox(40);
        contentLayout.setAlignment(Pos.CENTER);

        GridPane overviewStatsGrid = new GridPane();
        overviewStatsGrid.setHgap(50);
        overviewStatsGrid.setVgap(30);
        overviewStatsGrid.setPadding(new Insets(20, 0, 20, 0)); 

        overviewStatsGrid.add(createStatCard("Total Incidents", "1,250", PRIMARY_BLUE), 0, 0);
        overviewStatsGrid.add(createStatCard("Resolved Incidents", "980", SUCCESS_COLOR), 1, 0);
        overviewStatsGrid.add(createStatCard("Active Alerts", "15", DANGER_COLOR), 2, 0);
        overviewStatsGrid.add(createStatCard("Volunteers Engaged", "343", FONT_COLOR_MEDIUM), 0, 1);
        overviewStatsGrid.add(createStatCard("Resources Deployed", "8.2K", FONT_COLOR_MEDIUM), 1, 1);
        overviewStatsGrid.add(createStatCard("NGOs Partnered", "12", FONT_COLOR_MEDIUM), 2, 1);

        VBox overviewCard = new VBox(overviewStatsGrid);
        overviewCard.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                              "-fx-background-radius: 12; " +
                              "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        overviewCard.setPadding(new Insets(30));
        overviewCard.setMaxWidth(1200); 
        overviewCard.setAlignment(Pos.CENTER);

        HBox chartsRow1 = new HBox(40); 
        chartsRow1.setAlignment(Pos.CENTER);
        chartsRow1.getChildren().addAll(createIncidentTypeBarChart(), createIncidentStatusPieChart());
        
        HBox chartsRow2 = new HBox(40); 
        chartsRow2.setAlignment(Pos.CENTER);
        chartsRow2.getChildren().addAll(createVolunteerActivityLineChart());

        contentLayout.getChildren().addAll(overviewCard, chartsRow1, chartsRow2);
        return contentLayout;
    }

    private Node createStatCard(String title, String value, String colorHex) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                      "-fx-background-radius: 10; " +
                      "-fx-border-color: " + colorHex + "; -fx-border-width: 2;"); 
        card.setPrefSize(250, 120); 

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(16, FontWeight.NORMAL));
        titleLabel.setTextFill(Color.web(FONT_COLOR_MEDIUM));

        Label valueLabel = new Label(value);
        valueLabel.setFont(getFont(32, FontWeight.BOLD));
        valueLabel.setTextFill(Color.web(colorHex));

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private Node createIncidentTypeBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Incidents by Type (Last 30 Days)");
        xAxis.setLabel("Incident Type");
        yAxis.setLabel("Number of Incidents");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Incidents");
        series.getData().add(new XYChart.Data<>("Flood", 200));
        series.getData().add(new XYChart.Data<>("Earthquake", 50));
        series.getData().add(new XYChart.Data<>("Wildfire", 75));
        series.getData().add(new XYChart.Data<>("Medical", 120));
        series.getData().add(new XYChart.Data<>("Other", 30));

        barChart.getData().add(series);
        barChart.setPrefSize(700, 400); 
        barChart.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 12;");
        barChart.setPadding(new Insets(20));

        return barChart;
    }

    private Node createIncidentStatusPieChart() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Resolved", 78),
                        new PieChart.Data("Assigned", 15),
                        new PieChart.Data("Pending", 7)
                );
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Incident Status Distribution");
        chart.setLabelsVisible(true);
        chart.setPrefSize(500, 400); 
        chart.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 12;");
        chart.setPadding(new Insets(20));

        return chart;
    }

    private Node createVolunteerActivityLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        javafx.scene.chart.LineChart<String, Number> lineChart = new javafx.scene.chart.LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Volunteer Hours Logged (Last 6 Months)");
        xAxis.setLabel("Month");
        yAxis.setLabel("Hours");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Hours Logged");
        series.getData().add(new XYChart.Data<>("Jan", 150));
        series.getData().add(new XYChart.Data<>("Feb", 180));
        series.getData().add(new XYChart.Data<>("Mar", 220));
        series.getData().add(new XYChart.Data<>("Apr", 200));
        series.getData().add(new XYChart.Data<>("May", 250));
        series.getData().add(new XYChart.Data<>("Jun", 280));

        lineChart.getData().add(series);
        lineChart.setPrefSize(1200, 400);
        lineChart.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; -fx-background-radius: 12;");
        lineChart.setPadding(new Insets(20));

        return lineChart;
    }

}