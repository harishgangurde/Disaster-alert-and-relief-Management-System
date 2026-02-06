package com.thinkspark.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AboutUsView extends Application {

    private static final String BACKGROUND_COLOR = "#F4F7FC";
    private static final String PRIMARY_BLUE = "#005A9C";
    private static final String FONT_COLOR_DARK = "#2D3748";
    private static final String CARD_BACKGROUND_COLOR = "#FFFFFF";

    private final Scene previousScene;
    private final Stage primaryStage;

    private static Font getFont(double size, FontWeight weight) {
        return Font.font("System", weight, size);
    }

    public AboutUsView(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox(45);
        rootLayout.setPadding(new Insets(60));
        rootLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        rootLayout.setAlignment(Pos.TOP_CENTER);

        Node header = createHeader();
        Node aboutContent = createAboutContent();

        rootLayout.getChildren().addAll(header, aboutContent);

        ScrollPane scrollPane = new ScrollPane(rootLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("About Us | Disaster Relief Helper");
    }

    private Node createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0));

        Label title = new Label("💡 About Safe Serve");
        title.setFont(getFont(48, FontWeight.BOLD));
        title.setTextFill(Color.web(FONT_COLOR_DARK));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("⬅ Back to Dashboard");
        backButton.setFont(getFont(24, FontWeight.MEDIUM));
        backButton.setTextFill(Color.web(PRIMARY_BLUE));
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-border-color: " + PRIMARY_BLUE + "; -fx-border-radius: 10; -fx-border-width: 2;");
        backButton.setPadding(new Insets(12, 24, 12, 24));
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
            primaryStage.setTitle("Citizen Dashboard | Disaster Relief Helper");
        });

        header.getChildren().addAll(title, spacer, backButton);
        return header;
    }

    private Node createAboutContent() {
        VBox card = new VBox(45);
        card.setPadding(new Insets(60));
        card.setStyle("-fx-background-color: " + CARD_BACKGROUND_COLOR + "; " +
                "-fx-background-radius: 18; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 15, 0, 0, 6);");
        card.setMaxWidth(1400);

        // === Shashi Sir Section (First) ===
        VBox teacherTitleBox = new VBox();
        Label teacherTitle = new Label("📘 Our Teacher: Shashi Bagal");
        teacherTitle.setFont(getFont(32, FontWeight.BOLD));
        teacherTitle.setTextFill(Color.web(PRIMARY_BLUE));
        teacherTitleBox.getChildren().add(teacherTitle);

        HBox shashiBox = new HBox(30);
        shashiBox.setAlignment(Pos.CENTER_RIGHT);
        shashiBox.setPadding(new Insets(15, 0, 45, 0));

        Text shashiText = new Text(
                "Mr. Shashi Bagal is the founder and chief mentor at Core2web, a premier software training institute in Pune. With over 9 years of experience in software development and education, he is widely respected for his deep knowledge and clarity in teaching core programming concepts. "
                        + "His areas of expertise include Java, Data Structures, and Object-Oriented Programming. Shashi Sir's passion for teaching and his practical approach have helped thousands of students start successful careers in tech. "
                        + "He continues to mentor learners across India through online and offline platforms."
        );
        shashiText.setFont(getFont(22, FontWeight.NORMAL));
        shashiText.setWrappingWidth(900);
        shashiText.setFill(Color.web(FONT_COLOR_DARK));
        shashiText.setTextAlignment(TextAlignment.JUSTIFY);

        ImageView shashiImage = loadImage("/Assets/Images/shashisir.jpg", 400, 400);
        shashiBox.getChildren().addAll(shashiText, shashiImage);

        // === Core2web Section (Second) ===
        VBox coreTitleBox = new VBox();
        Label coreTitle = new Label("🏫 About Core2web – Pune");
        coreTitle.setFont(getFont(32, FontWeight.BOLD));
        coreTitle.setTextFill(Color.web(PRIMARY_BLUE));
        coreTitleBox.getChildren().add(coreTitle);

        HBox core2webBox = new HBox(30);
        core2webBox.setAlignment(Pos.CENTER_LEFT);
        core2webBox.setPadding(new Insets(15, 0, 45, 0));

        ImageView core2webImage = loadImage("/Assets/Images/core2web.jpg", 400, 400);
        Text core2webText = new Text(
                "Core2web is a top-tier IT training institute founded in 2017 in Pune, Maharashtra. Known for its hands-on approach to programming education, Core2web offers industry-focused training in Java, Python, C++, Data Structures, Operating Systems, and more. "
                        + "The institute has empowered over 10,000 students by equipping them with strong technical skills and preparing them for real-world challenges. Its mission is to make every learner industry-ready by teaching them to 'Know the Code Till the Core.' "
                        + "Core2web is a trusted choice for anyone looking to build a career in the software industry."
        );
        core2webText.setFont(getFont(22, FontWeight.NORMAL));
        core2webText.setWrappingWidth(900);
        core2webText.setFill(Color.web(FONT_COLOR_DARK));
        core2webText.setTextAlignment(TextAlignment.JUSTIFY);
        core2webBox.getChildren().addAll(core2webImage, core2webText);

        HBox projectSection = new HBox(45);
        projectSection.setAlignment(Pos.CENTER_LEFT);
        projectSection.setPadding(new Insets(15, 0, 45, 0));

        VBox projectTextBox = new VBox(15);
        Label projectTitle = new Label("📌 Our Project : Safe Serve");
        projectTitle.setFont(getFont(32, FontWeight.BOLD));
        projectTitle.setTextFill(Color.web(PRIMARY_BLUE));

        Text projectText = new Text(
                "Safe Serve is a comprehensive disaster relief platform designed to connect citizens, volunteers, and authorities during emergencies. "
                        + "Our mission is to streamline the process of reporting incidents, requesting aid, and distributing resources efficiently to save lives and support communities in times of crisis."
        );
        projectText.setFont(getFont(22, FontWeight.NORMAL));
        projectText.setWrappingWidth(900);
        projectText.setFill(Color.web(FONT_COLOR_DARK));
        projectText.setTextAlignment(TextAlignment.JUSTIFY);
        projectTextBox.getChildren().addAll(projectTitle, projectText);

        ImageView logoImage = loadImage("/logo.png", 350, 350);
        projectSection.getChildren().addAll(projectTextBox, logoImage);

        VBox teamSection = (VBox) createSection(
                "👥 Our Team : ThinkSpark",
                "ThinkSpark is a team of passionate developers, designers, and problem-solvers dedicated to using technology for social good. We believe in the power of innovation to create practical solutions for real-world challenges. The Safe Serve project is our commitment to building a more resilient and connected society."
        );

        HBox teamRow = new HBox(45);
        teamRow.setAlignment(Pos.CENTER);
        teamRow.getChildren().addAll(
                createPersonCard("Shivkumar Kapse (Team Lead)", "/Assets/Images/shiv.jpg"),
                createPersonCard("Atharva Guthe", "/Assets/Images/atharva.jpg"),
                createPersonCard("Varad Wani", "/Assets/Images/varad.jpg"),
                createPersonCard("Harish Gangurde", "/Assets/Images/harish.jpg")
        );
        teamSection.getChildren().add(teamRow);

        Node missionSection = createSection(
                "🎯 Our Goal :",
                "To empower communities with a reliable and intuitive platform that facilitates rapid response and effective communication during disasters, ensuring that help reaches those who need it most, as quickly as possible."
        );

        card.getChildren().addAll(
                teacherTitleBox, shashiBox,
                coreTitleBox, core2webBox,
                projectSection,
                teamSection,
                missionSection
        );

        VBox container = new VBox(card);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private Node createSection(String title, String content) {
        VBox section = new VBox(15);

        Label titleLabel = new Label(title);
        titleLabel.setFont(getFont(32, FontWeight.BOLD));
        titleLabel.setTextFill(Color.web(PRIMARY_BLUE));

        Text contentText = new Text(content);
        contentText.setFont(getFont(22, FontWeight.NORMAL));
        contentText.setFill(Color.web(FONT_COLOR_DARK));
        contentText.setWrappingWidth(1300);
        contentText.setTextAlignment(TextAlignment.JUSTIFY);

        section.getChildren().addAll(titleLabel, contentText);
        return section;
    }

    private ImageView loadImage(String path, double width, double height) {
        try {
            Image image = new Image(getClass().getResource(path).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (Exception e) {
            System.err.println("Image not found: " + path);
            return new ImageView();
        }
    }

    private Node createPersonCard(String name, String imagePath) {
        VBox personCard = new VBox(15);
        personCard.setAlignment(Pos.CENTER);
        personCard.setPadding(new Insets(15));

        ImageView imageView = loadImage(imagePath, 250, 250);

        Label nameLabel = new Label(name);
        nameLabel.setFont(getFont(22, FontWeight.BOLD));
        nameLabel.setTextFill(Color.web(FONT_COLOR_DARK));
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setWrapText(true);

        personCard.getChildren().addAll(imageView, nameLabel);
        return personCard;
    }
}