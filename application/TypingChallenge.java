package application;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



public class TypingChallenge {

    private Stage window;
    private Scene typingScene;
    private String[] sentences = {
        "michael myers is the father of this house",
        "in the dark freddy always seems to be behind you",
        "search well and you will see the ghost in your room",
        "if anabel want to play with you never say no",
        "when u hear the silence expect to meet chucky"
    };

    private String currentSentence;
    private Timeline timer;
    private int timeRemaining;

    public TypingChallenge(Stage window) {
        this.window = window;
    }

    public void startTypingChallenge() {
        timeRemaining = 10;

        // יצירת ה-Layout למשחק ההקלדה
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 50 50 50 50; -fx-alignment: center;");

        // הגדרת תמונת הרקע ל- backtype.png
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/backend.png"));
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1400, 1200, false, false, false, false)
        );
        layout.setBackground(new Background(background));

        Label sentenceLabel = new Label();
        Label timerLabel = new Label("Time remaining: " + timeRemaining + " seconds");
        timerLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: red;");
        
        TextField typingField = new TextField();
        typingField.setPrefSize(300, 30);
        
        
        
        
        Button submitButton = new Button("Submit");

        Random rand = new Random();
        currentSentence = sentences[rand.nextInt(sentences.length)];
        sentenceLabel.setText(currentSentence);
        sentenceLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold; -fx-text-fill: black;");

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText("Time remaining: " + timeRemaining + " seconds");

            if (timeRemaining <= 0) {
                timer.stop();
                showResult(false, layout);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        submitButton.setOnAction(e -> {
            String userInput = typingField.getText();
            if (userInput.equals(currentSentence)) {
                timer.stop();
                showResult(true, layout);
            } else {
                showResult(false, layout);
            }
        });

        layout.getChildren().addAll(sentenceLabel, timerLabel, typingField, submitButton);

        typingScene = new Scene(layout, 1200, 800);
        window.setScene(typingScene);
    }

    
    
    
    
    
    private void showResult(boolean success, VBox layout) {
        layout.getChildren().clear();

        if (success) {
            // הוספת התמונה maavarmaze.png לאחר ניצחון
            Image winImage = new Image(getClass().getResourceAsStream("/resources/beforemaze.png"));
            ImageView winImageView = new ImageView(winImage);
            winImageView.setFitWidth(1200); // התאמת גודל התמונה
            winImageView.setFitHeight(1400);
            
            // כאשר לוחצים על התמונה, מעבר למשחק MazeGame
            winImageView.setOnMouseClicked(e -> {
                MazeGame mazeGame = new MazeGame(window);
                mazeGame.startMazeGame();
            });

            layout.getChildren().add(winImageView);
        } else {
            // תצוגת כשלון - נסיון נוסף
            Label resultLabel = new Label("Time's up or incorrect typing! Try again.");
            resultLabel.setStyle("-fx-font-size: 45px; -fx-font-weight: bold; -fx-text-fill: black;");
            
            // Play failure sound "scaryclown.mp3"
            Media sound = new Media(getClass().getResource("/resources/scaryclown.mp3").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();

            
            
            Button tryAgainButton = new Button("Try Again");
            tryAgainButton.setOnAction(e -> {
                timer.stop();
                timeRemaining = 10;
                startTypingChallenge();
            });

            layout.getChildren().addAll(resultLabel, tryAgainButton);
        }
    }
}
