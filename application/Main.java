package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage window;
    private static Scene menuScene;
    private int logoClickCount = 0;  // משתנה כדי לעקוב אחרי מספר הלחיצות

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        
        // Set the application icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/icongame.png"));
        primaryStage.getIcons().add(icon);

        
        
        // מסך פתיחה עם תמונת logo.png
        Image logoImage = new Image(getClass().getResourceAsStream("/resources/logo.png"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(1400);
        logoImageView.setFitHeight(1000);

        BorderPane splashLayout = new BorderPane();
        splashLayout.setCenter(logoImageView);

        Scene splashScene = new Scene(splashLayout, 1400, 1000);

        // שינוי התמונה על פי הלחיצות
        logoImageView.setOnMouseClicked(event -> {
            logoClickCount++;
            switch (logoClickCount) {
                case 1:
                    // בלחיצה ראשונה - הצגת logo.png (כבר מוצגת כברירת מחדל)
                    break;
                case 2:
                    // בלחיצה שנייה - הצגת logod.png
                    logoImageView.setImage(new Image(getClass().getResourceAsStream("/resources/ba2.png")));
                    break;
                case 3:
                    // בלחיצה שלישית - הצגת logof.png
                    logoImageView.setImage(new Image(getClass().getResourceAsStream("/resources/ba3.png")));
                    break;
                case 4:
                    // בלחיצה רביעית - מעבר לתפריט הראשי
                    showMainMenu();
                    break;
                default:
                    break;
            }
        });

        window.setScene(splashScene);
        window.setTitle("EscapeRoomGame");
        window.show();    }

    // פונקציה להצגת התפריט הראשי
    private void showMainMenu() {
        BorderPane root = new BorderPane();

        // תמונת הרקע לתפריט הראשי
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/lobby.png"));
        BackgroundImage background = new BackgroundImage(backgroundImage, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.DEFAULT, new BackgroundSize(1500, 900, true, true, true, true));
        root.setBackground(new Background(background));

        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setMaxWidth(800);

        // יצירת כפתורים עבור התפריט
        Button startGameButton = new Button("Start Game");
        startGameButton.setStyle(
            "-fx-background-image: url('/resources/buttons.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-repeat: no-repeat;" +
            "-fx-background-position: center center;" +
            "-fx-text-fill: red;" +
            "-fx-font-size: 36px;"
        );
        startGameButton.setPrefWidth(300);
        startGameButton.setPrefHeight(50);
        startGameButton.setOnAction(e -> startGame());

        Button levelsButton = new Button("Levels");
        levelsButton.setStyle(
            "-fx-background-image: url('/resources/buttons.png');" +
            "-fx-background-size: cover;" + 
            "-fx-background-repeat: no-repeat;" + 
            "-fx-background-position: center center;" + 
            "-fx-text-fill: red;" +            
            "-fx-font-size: 26px;"
        );
        levelsButton.setPrefWidth(300);
        levelsButton.setPrefHeight(50);

        Button creditsButton = new Button("Credits");
        creditsButton.setStyle(
            "-fx-background-image: url('/resources/buttons.png');" +
            "-fx-background-size: cover;" +      
            "-fx-background-repeat: no-repeat;" + 
            "-fx-background-position: center center;" + 
            "-fx-text-fill: red;" +            
            "-fx-font-size: 26px;"
        );
        creditsButton.setPrefWidth(300);
        creditsButton.setPrefHeight(50);

        Button exitButton = new Button("Exit");
        exitButton.setStyle(
            "-fx-background-image: url('/resources/buttons.png');" +
            "-fx-background-size: cover;" +      
            "-fx-background-repeat: no-repeat;" + 
            "-fx-background-position: center center;" + 
            "-fx-text-fill: red;" +             
            "-fx-font-size: 20px;"
        );
        exitButton.setPrefWidth(300);
        exitButton.setPrefHeight(50);

        levelsButton.setOnAction(e -> showLevelsMenu());
        creditsButton.setOnAction(e -> showCredits());
        exitButton.setOnAction(e -> window.close());

        menuLayout.getChildren().addAll(startGameButton, levelsButton, creditsButton, exitButton);
        root.setCenter(menuLayout);

        menuScene = new Scene(root, 1500, 900);
        window.setScene(menuScene);
    }

    // פונקציה לתצוגת תפריט ה-Levels
    private void showLevelsMenu() {
        VBox levelsLayout = new VBox(20);
        levelsLayout.setAlignment(Pos.CENTER);
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/backscare.png"));
        BackgroundImage background = new BackgroundImage(backgroundImage, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, 
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true));

        levelsLayout.setBackground(new Background(background));

        Button startButton = new Button("Typing Challenge");
        startButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        startButton.setPrefWidth(300);
        startButton.setPrefHeight(50);
        startButton.setOnAction(e -> startGame());

        Button mazeButton = new Button("Maze Game");
        mazeButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        mazeButton.setPrefWidth(300);
        mazeButton.setPrefHeight(50);
        mazeButton.setOnAction(e -> {
            MazeGame mazeGame = new MazeGame(window);
            mazeGame.startMazeGame();
        });

        Button puzzleButton = new Button("Button Puzzle");
        puzzleButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        puzzleButton.setPrefWidth(300);
        puzzleButton.setPrefHeight(50);
        puzzleButton.setOnAction(e -> {
            ButtonPuzzle buttonPuzzle = new ButtonPuzzle(window);
            buttonPuzzle.startButtonPuzzle();
        });

        Button wirePuzzleButton = new Button("Wire Puzzle Game");
        wirePuzzleButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        wirePuzzleButton.setPrefWidth(300);
        wirePuzzleButton.setPrefHeight(50);
        wirePuzzleButton.setOnAction(e -> {
            WirePuzzle wirePuzzle = new WirePuzzle(window);
            wirePuzzle.startWirePuzzle();
        });

        Button findNumberGameButton = new Button("Find Different Number Game");
        findNumberGameButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        findNumberGameButton.setPrefWidth(300);
        findNumberGameButton.setPrefHeight(50);
        findNumberGameButton.setOnAction(e -> {
            FindDifferentNumberGame game = new FindDifferentNumberGame();
            game.start(window);
        });

        Button memoryGameButton = new Button("Memory Game");
        memoryGameButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        memoryGameButton.setPrefWidth(300);
        memoryGameButton.setPrefHeight(50);
        memoryGameButton.setOnAction(e -> {
            MemoryGame memoryGame = new MemoryGame();
            memoryGame.start(new Stage());
        });

        Button backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 18px;");
        backButton.setPrefWidth(300);
        backButton.setPrefHeight(50);
        backButton.setOnAction(e -> window.setScene(menuScene));

        levelsLayout.getChildren().addAll(startButton, mazeButton, puzzleButton, wirePuzzleButton, findNumberGameButton, memoryGameButton, backButton);

        Scene levelsScene = new Scene(levelsLayout, 1200, 800);
        window.setScene(levelsScene);
    }

    // Function to show credits
    private void showCredits() {
        VBox creditsLayout = new VBox(20);
        creditsLayout.setAlignment(Pos.CENTER);
        creditsLayout.setStyle("-fx-background-color: #D3D3D3;");

        Label creditsLabel = new Label("Credits:\nDeveloped by [Roy,Shay,Shaked]");
        Label creditsLabel1 = new Label("All photos generated by AI");
        Button returnButton = new Button("Return to Menu");
        returnButton.setOnAction(e -> window.setScene(menuScene));

        creditsLayout.getChildren().addAll(creditsLabel ,creditsLabel1, returnButton);

        Scene creditsScene = new Scene(creditsLayout, 800, 600);
        window.setScene(creditsScene);
    }

    private void startGame() {
        TypingChallenge typingChallenge = new TypingChallenge(window);
        typingChallenge.startTypingChallenge();
    }

    public static Scene getMenuScene() {
        return menuScene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}




