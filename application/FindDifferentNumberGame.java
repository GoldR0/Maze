package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class FindDifferentNumberGame extends Application {

    private Stage window;
    private int currentLevel = 1;  // מציין את השלב הנוכחי
    private boolean firstLevelCompleted = false;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        showLevel1();
    }

    // תצוגת השלב הראשון
    private void showLevel1() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        Label instruction = new Label("michael turned himself to a 1 between all of these l , Find him!");
        instruction.setStyle("-fx-font-size: 42px; -fx-text-fill: black; -fx-padding: 30px;"); 
        
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/numberfind.png"));
        BackgroundImage background = new BackgroundImage(backgroundImage, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, 
            new BackgroundSize(1400, 1000, true, true, true, true));
        layout.setBackground(new Background(background));
        
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        
        // יצירת מטריצה עם l כאשר יש "1" אחד שונה
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                Button button = new Button("l");
                button.setMinSize(30, 30);
                if (row == 15 && col == 15) {  // מיקום המספר השונה 8
                    button.setText("1");
                    button.setOnAction(e -> handleCorrectSelection());
                } else {
                    button.setOnAction(e -> handleWrongSelection());
                }
                grid.add(button, col, row);
            }
        }

        layout.getChildren().addAll(instruction, grid);
        Scene scene = new Scene(layout, 1400, 1000);
        window.setScene(scene);
        window.show();
    }

    private void showLevel2() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);  // קביעת המיקום של ההודעה בחלק העליון

        // הגדרת תמונת הרקע
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/numberfind.png"));
        BackgroundImage background = new BackgroundImage(backgroundImage, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, 
            new BackgroundSize(1400, 1000, true, true, true, true));
        layout.setBackground(new Background(background));

        Label instruction = new Label("michael turned himself to an S between all of these 5 , Find him!");
        instruction.setStyle("-fx-font-size: 42px; -fx-text-fill: black; -fx-padding: 30px;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        // יצירת מטריצת 5 עם תו S שונה
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                Button button = new Button("5");
                button.setMinSize(30, 30);
                if (row == 4 && col == 16) {  // מיקום של התו S
                    button.setText("S");
                    button.setOnAction(e -> handleWin());  // בחירה נכונה, רק הכפתור הזה יוביל לניצחון
                } else {
                    button.setOnAction(e -> handleWrongSelection());  // שאר הכפתורים יובילו לטעות
                }
                grid.add(button, col, row);
            }
        }

        layout.getChildren().addAll(instruction, grid);

        Scene scene = new Scene(layout, 1400, 1000);
        window.setScene(scene);
        window.show();
    }


    // טיפול בבחירה הנכונה בשלב 1
    private void handleCorrectSelection() {
        if (!firstLevelCompleted) {
            firstLevelCompleted = true;
            currentLevel = 2;
            showLevel2();  // מעבר לשלב 2
        }
    }

    // טיפול בבחירה הנכונה בשלב 2
    private void handleCorrectSelections() {
        if (firstLevelCompleted && currentLevel == 2) {
            handleWin();  // אם בחר נכון בשני השלבים
        }
    }

    // טיפול בבחירה הלא נכונה
    private void handleWrongSelection() {
        // כאן תוכל להוסיף פעולה או הודעה במידה והמשתמש לחץ על מקום לא נכון
        System.out.println("Wrong choice! Try again.");
    }

    // פונקציה לטיפול בניצחון במשחק
    private void handleWin() {
        VBox winLayout = new VBox(20);
        winLayout.setAlignment(Pos.TOP_CENTER);
        winLayout.setStyle("-fx-padding: 20;");
        
        // הגדרת תמונת הרקע
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/maavarmichael.png"));
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(600, 400, false, false, true, true)
        );
        winLayout.setBackground(new Background(background));
  
        // הודעת נצחון
        Label winLabel = new Label("Your eyes are sharp, Michael can't hide from you!");

        winLabel.setStyle(
        	    "-fx-font-size: 34px; " +
        	    "-fx-text-fill: white; " +
        	    "-fx-font-family: 'Courier New';" +  // לדוגמה: "Courier New"
        	    "-fx-font-weight: bold;"
        	);
        
        
        
        
        
        
        
        
        // כפתור מעבר לחדר הבא
        Button nextRoomButton = new Button("Next Room");
        nextRoomButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16px;");
        nextRoomButton.setOnAction(e -> {
            // מעבר למשחק הזיכרון
            MemoryGame memoryGame = new MemoryGame();
            memoryGame.start(new Stage());
            window.close();  // סגירת החלון הנוכחי
        });
        
        
        
        // הוספת ההודעה והכפתור לפריסת הנצחון
        winLayout.getChildren().addAll(winLabel, nextRoomButton);
        

        // הגדרת הסצנה החדשה עם הפריסת הנצחון
        Scene winScene = new Scene(winLayout, 1200, 800);  // שינוי גודל החלון ל-600x400
        window.setScene(winScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

