package application;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



public class ButtonPuzzle {

    private Stage window;
    private Scene buttonScene;
    private Button[] buttons = new Button[8];
    private boolean gameWon = false;  // משתנה כדי להימנע מלחיצות נוספות אחרי ניצחון

    public ButtonPuzzle(Stage window) {
        this.window = window;
    }

    public void startButtonPuzzle() {
        // יצירת GridPane שמכיל את הכפתורים
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setStyle("-fx-background-color: black;"); // שינוי רקע המשחק לשחור

        // יצירת 8 כפתורים
        for (int i = 0; i < 8; i++) {
            buttons[i] = new Button("OFF");
            buttons[i].setStyle("-fx-background-color: red; -fx-text-fill: white;");

            // שינוי המצב של כל כפתור בלחיצה
            final int index = i;
            buttons[i].setOnAction(e -> toggleButton(index));

            buttons[i].setMinSize(100, 100);
            buttons[i].setPrefSize(200, 200);

            gridPane.add(buttons[i], i % 4, i / 4);
        }

        buttonScene = new Scene(gridPane, 800, 400);
        window.setScene(buttonScene);
    }

    private void toggleButton(int index) {
        if (gameWon) {
            return;
        }

        changeButtonState(index);

        if (index > 0) {
            changeButtonState(index - 1);
        }
        if (index < buttons.length - 1) {
            changeButtonState(index + 1);
        }

        if (checkAllButtonsOn()) {
            gameWon = true;
            showWinMessage();
        }
    }

    private void changeButtonState(int index) {
        if (buttons[index].getText().equals("OFF")) {
            buttons[index].setText("ON");
            buttons[index].setStyle("-fx-background-color: green; -fx-text-fill: white;");
        } else {
            buttons[index].setText("OFF");
            buttons[index].setStyle("-fx-background-color: red; -fx-text-fill: white;");
        }
    }

    private boolean checkAllButtonsOn() {
        for (Button button : buttons) {
            if (button.getText().equals("OFF")) {
                return false;
            }
        }
        return true;
    }

    private void showWinMessage() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-alignment: center;");

        // הגדרת תמונת רקע שתתאים לכל גודל חלון
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/maavarb.png"));
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)); // התאמה מלאה לגודל החלון

        layout.setBackground(new Background(background));
        
        
        
        // Playing the "scaregenerator.mp3" audio file
        Media sound = new Media(getClass().getResource("/resources/scaregenerator.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        
        
        
        

        // הוספת אירוע לחיצה על כל האזור של VBox
        layout.setOnMouseClicked(event -> {
            WirePuzzle wirePuzzle = new WirePuzzle(window);
            wirePuzzle.startWirePuzzle();
        });

        buttonScene = new Scene(layout, 1500, 1200);
        window.setScene(buttonScene);
    }
}
