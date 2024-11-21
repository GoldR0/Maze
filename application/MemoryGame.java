package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Window;

public class MemoryGame extends Application {

    private static final int ROWS = 4;  // 4x3 grid for 6 pairs
    private static final int COLS = 3;  // 3 columns per row
    private List<ImageView> selectedCards = new ArrayList<>();
    private List<ImageView> matchedCards = new ArrayList<>();
    private int mistakes = 0;
    private final int maxMistakes = 20;
    private final int totalPairs = 6;
    private boolean allowExitFullScreen = false; // משתנה לבדיקת מצב "Y"

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showLevelsMenu(primaryStage);
    }

    // Display the levels menu and automatically start MemoryGame, closing the menu
    private void showLevelsMenu(Stage menuStage) {
        // Start MemoryGame and close other stages
        startMemoryGame(menuStage);
    }

    // Start the MemoryGame and close all other stages
    private void startMemoryGame(Stage menuStage) {
        // Close all open stages except the new game stage
        closeAllOtherStages(menuStage);

        // Create a new Stage for the MemoryGame
        Stage gameStage = new Stage();
        gameStage.setTitle("Memory Game");

        GridPane grid = new GridPane();
        List<Image> images = loadImages();  // Load the images from the 'resources' directory

        // Double the images to create pairs
        images.addAll(new ArrayList<>(images));

        // Shuffle the images randomly
        Collections.shuffle(images);

        // Create the ImageView grid
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                ImageView imageView = createCard(images.remove(0));
                grid.add(imageView, j, i);
            }
        }

        Scene scene = new Scene(grid, 600, 600);
        gameStage.setScene(scene);
        gameStage.show();
    }

    // Helper method to close all other stages except the one passed as parameter
    private void closeAllOtherStages(Stage keepOpenStage) {
        // Create a copy of the current windows list to avoid ConcurrentModificationException
        List<Window> windowsToClose = new ArrayList<>(Stage.getWindows());
        
        for (Window window : windowsToClose) {
            if (window instanceof Stage stage && stage != keepOpenStage && stage.isShowing()) {
                stage.close();
            }
        }
    }

    private ImageView createCard(Image image) {
        ImageView card = new ImageView();
        card.setFitWidth(200);
        card.setFitHeight(150);

        // Set the card initially with a hidden placeholder
        Image hiddenImage = new Image(getClass().getResource("/resources/siman.png").toExternalForm());
        card.setImage(hiddenImage);

        // Event listener for when the card is clicked
        card.setOnMouseClicked(e -> {
            if (!selectedCards.contains(card) && !matchedCards.contains(card)) {
                card.setImage(image);  // Reveal the card
                selectedCards.add(card);

                if (selectedCards.size() == 2) {
                    checkForMatch();
                }
            }
        });

        return card;
    }

    // Check if two selected cards match
    private void checkForMatch() {
        ImageView card1 = selectedCards.get(0);
        ImageView card2 = selectedCards.get(1);

        // Check if the images of the two cards match
        if (card1.getImage().getUrl().equals(card2.getImage().getUrl())) {
            matchedCards.add(card1);  // Keep the cards open
            matchedCards.add(card2);

            // Check if all pairs are matched
            if (matchedCards.size() / 2 == totalPairs) {
                showWinScreen();
            }
        } else {
            mistakes++;
            if (mistakes >= maxMistakes) {
                showGameOver();
                return;
            }

            // Hide the cards after a short delay (for user to see)
            Timeline hideCards = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> {
                    card1.setImage(new Image(getClass().getResource("/resources/siman.png").toExternalForm()));
                    card2.setImage(new Image(getClass().getResource("/resources/siman.png").toExternalForm()));
                })
            );
            hideCards.play();
        }

        selectedCards.clear();  // Clear the selected cards list
    }

    // Show an alert when the player loses the game
    private void showGameOver() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game Over! You made too many mistakes.", ButtonType.OK);
        alert.showAndWait().ifPresent(response -> restartGame());
    }

    // Restart the game by resetting the stage
    private void restartGame() {
        Stage primaryStage = new Stage();
        startMemoryGame(primaryStage);
    }

    // Show win screen when the player wins
    private void showWinScreen() {
        ImageView winImageView = new ImageView(new Image(getClass().getResourceAsStream("/resources/thank.png")));
        winImageView.setPreserveRatio(false);

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        winImageView.setFitWidth(screenWidth);
        winImageView.setFitHeight(screenHeight);

        StackPane layout = new StackPane(winImageView);
        Scene winScene = new Scene(layout, screenWidth, screenHeight);
        Stage primaryStage = (Stage) selectedCards.get(0).getScene().getWindow();

        primaryStage.setScene(winScene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("Press 'Y' to exit fullscreen");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        winScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Y) {
                allowExitFullScreen = true;
                primaryStage.setFullScreen(false);
                primaryStage.close();
                showNewWindowWithBackground();
            } else {
                event.consume();
            }
        });

        primaryStage.fullScreenProperty().addListener((observable, wasFullScreen, isFullScreen) -> {
            if (!isFullScreen && !allowExitFullScreen) {
                primaryStage.setFullScreen(true);
            } else {
                allowExitFullScreen = false;
            }
        });
    }

    private void showNewWindowWithBackground() {
        Stage newStage = new Stage();
        newStage.setTitle("New Window");

        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/credits.png"));
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        StackPane layout = new StackPane();
        layout.setBackground(new Background(background));

        Scene scene = new Scene(layout, 1400, 1000);
        newStage.setScene(scene);
        newStage.show();
    }

    private List<Image> loadImages() {
        List<Image> images = new ArrayList<>();
        images.add(new Image(getClass().getResource("/resources/dracula1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/resources/michel1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/resources/ghostface1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/resources/freddy1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/resources/anabel1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/resources/chucky1.png").toExternalForm()));  

        return images;
    }
}
