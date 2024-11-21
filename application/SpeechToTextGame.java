package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpeechToTextGame {

    private Stage window;
    private Text questionText; // שימוש ב-Text במקום Label
    private TextField inputField; // שדה להצגת התשובה
    private String[] questions = {
            "The first killer usually walks around with a hat on his head",
            "The second killer is not a man",
            "The third killer's weapon makes the noise of an electric motor"
    };
    private String[][] correctAnswers = {
            {"max"},
            {"jim", "jeam", "jeem","gym"},
            {"jak", "jack", "jac", "jeck", "jek"}
    };
    private int currentQuestionIndex = 0; // אינדקס השאלה הנוכחית

    public SpeechToTextGame(Stage window) {
        this.window = window;
    }

    public void startSpeechToTextGame() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 50; -fx-alignment: center;");

        // הוספת תמונה כרקע
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/talkimage.png"));
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1400 , 1400 , false, false, false, false)
        );
        layout.setBackground(new Background(bgImage));

        // יצירת טקסט עם מסגרת שחורה
        questionText = new Text("Question: " + questions[currentQuestionIndex]);
        questionText.setFont(Font.font("Arial", 34)); // גודל הגופן
        questionText.setFill(Color.WHITE); // צבע המילוי
        questionText.setStroke(Color.BLACK); // צבע המסגרת
        questionText.setStrokeWidth(1.5); // עובי המסגרת

        inputField = new TextField(); // יצירת TextField
        inputField.setPromptText("Your answer will appear here");

        Button recordButton = new Button("Record Answer");
        recordButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        recordButton.setOnAction(e -> recordAnswer());

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        submitButton.setOnAction(e -> submitAnswer());

        Button backButton = new Button("Back to Levels");
        backButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        backButton.setOnAction(e -> window.setScene(Main.getMenuScene())); // חזרה לתפריט הראשי

        layout.getChildren().addAll(questionText, inputField, recordButton, submitButton, backButton);

        Scene speechScene = new Scene(layout, 1400, 1000); // הגדרת גודל התצוגה
        window.setScene(speechScene);
    }

    private void recordAnswer() {
        try {
            // הפעלת הסקריפט Python
            ProcessBuilder builder = new ProcessBuilder(
                    "C:\\Users\\Roy\\AppData\\Local\\Programs\\Python\\Python39\\python.exe",
                    "C:\\Users\\Roy\\Desktop\\eSpeak\\speech\\main.py"
            );
            builder.environment().put("VOSK_LOG_LEVEL", "0"); // כיבוי לוגים
            builder.redirectErrorStream(true); // איחוד פלטים
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            // קריאה של פלט התהליך
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("LOG")) { // התעלמות משורות לוג
                    result.append(line.trim()).append(" ");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String recognizedAnswer = result.toString().trim();

                // הסרת הודעת ההקלטה מהפלט
                recognizedAnswer = recognizedAnswer.replace("Recording... Please speak.", "").trim();

                if (recognizedAnswer.isEmpty()) {
                    inputField.setText("No answer recognized. Please try again.");
                } else {
                    inputField.setText(recognizedAnswer); // הצגת התוצאה המסוננת
                }
            } else {
                inputField.setText("Error during speech recognition. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            inputField.setText("An error occurred during speech recognition. Please try again.");
        }
    }

    private void submitAnswer() {
        String userAnswer = inputField.getText().trim().toLowerCase();
        if (userAnswer.isEmpty()) {
            inputField.setText("Please record or type an answer first.");
            return;
        }

        validateAnswer(userAnswer);
    }

    private void validateAnswer(String userAnswer) {
        for (String answer : correctAnswers[currentQuestionIndex]) {
            if (userAnswer.equals(answer)) {
                inputField.setText("Correct! Well done.");
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    questionText.setText("Question: " + questions[currentQuestionIndex]);
                    inputField.clear(); // איפוס התשובה הקודמת
                } else {
                    // מעבר למשחק FindDifferentNumberGame
                    startFindDifferentNumberGame();
                }
                return;
            }
        }
        inputField.setText("Incorrect. Please try again.");
    }

    private void startFindDifferentNumberGame() {
        FindDifferentNumberGame findGame = new FindDifferentNumberGame();
        findGame.start(window); // הפעלת המשחק הבא
    }
}
