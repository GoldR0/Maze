package application;



import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class WirePuzzle {

    private Stage window;
    private Scene puzzleScene;
    private double[] wiresXLeft = {100, 100, 100, 100, 100};  // מיקומי X של האותיות בצד שמאל
    private double[] wiresXRight = {700, 700, 700, 700, 700}; // מיקומי X של האותיות בצד ימין
    private double[] wiresYLeft = {100, 200, 300, 400, 500};  // מיקומי Y של האותיות בצד שמאל
    private double[] wiresYRight = {500, 400, 300, 200, 100}; // מיקומי Y של האותיות בצד ימין

    private int selectedWireIndex = -1;  // אינדקס האות שנבחרה להזזה
    private double lineEndX, lineEndY;   // מיקום הקצה הנוכחי של הקו הנמתח
    private boolean isDragging = false;  // האם נבחרה אות להזזה

    private boolean[][] connections = new boolean[5][5]; // מפת חיבורים בין אותיות
    private boolean[] completedWires = new boolean[5];   // סימון אם יש חיבור שלם לכל אות

    private int totalConnections = 0;  // מספר החיבורים הכוללים

    private Image spiderImage;

    public WirePuzzle(Stage window) {
        this.window = window;
    }

    public void startWirePuzzle() {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // Set background image for the puzzle
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/acavish.png"));
        BackgroundImage background = new BackgroundImage(
            backgroundImage, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, 
            new BackgroundSize(1600, 1400, false, false, false, false) // הגדרת גודל לתמונה
        );
        root.setBackground(new Background(background));

        // Load the spider image
        spiderImage = new Image(getClass().getResourceAsStream("/resources/spider1.png"));

        // Create two instruction labels
        VBox topLayout = new VBox();
        Label instructionLabel1 = new Label("Spider");
        Label instructionLabel2 = new Label("House");

        instructionLabel1.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");
        instructionLabel2.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");

        topLayout.getChildren().addAll(instructionLabel1, instructionLabel2);
        root.setTop(topLayout);

        drawPuzzle(gc);

        // Mouse click event to select a letter on the left side
        canvas.setOnMouseClicked(e -> handleMouseClick(e, gc));

        // Ensure the canvas can receive focus
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        // Key events for moving wires with W, A, S, D keys
        canvas.setOnKeyPressed(event -> {
            if (isDragging && selectedWireIndex != -1) {
                switch (event.getCode()) {
                    case W -> lineEndY -= 30; // Move up
                    case S -> lineEndY += 30; // Move down
                    case A -> lineEndX -= 30; // Move left
                    case D -> lineEndX += 30; // Move right
                    case ENTER -> checkConnection(gc); // Check connection on Enter
                    default -> {}
                }
                drawPuzzle(gc);  // Redraw with the dragging line
            }
        });

        puzzleScene = new Scene(root, 1400, 1000);
        window.setScene(puzzleScene);
        canvas.requestFocus(); // Ensure the canvas listens to keyboard inputs
    }

    // Handle mouse clicks to select letters on the left side
    private void handleMouseClick(MouseEvent e, GraphicsContext gc) {
        double mouseX = e.getX();
        double mouseY = e.getY();

        for (int i = 0; i < wiresXLeft.length; i++) {
            if (mouseX >= wiresXLeft[i] - 20 && mouseX <= wiresXLeft[i] + 20 && mouseY >= wiresYLeft[i] - 20 && mouseY <= wiresYLeft[i] + 20) {
                if (!completedWires[i]) {
                    selectedWireIndex = i;
                    lineEndX = wiresXLeft[i] + 20;
                    lineEndY = wiresYLeft[i];
                    isDragging = true;
                    break;
                }
            }
        }
        drawPuzzle(gc);
    }

    // Check if the dragged line reaches the correct right-side letter
    private void checkConnection(GraphicsContext gc) {
        for (int j = 0; j < wiresXRight.length; j++) {
            if (Math.abs(lineEndX - wiresXRight[j]) < 20 && Math.abs(lineEndY - wiresYRight[j]) < 20) {
                if (!completedWires[selectedWireIndex] && !connections[selectedWireIndex][j]) {
                    connections[selectedWireIndex][j] = true;
                    completedWires[selectedWireIndex] = true;
                    totalConnections++;
                    drawPuzzle(gc);

                    if (totalConnections == 5) {
                        showNextLevel();
                    }
                    return;
                }
            }
        }
    }

    // Draw the puzzle components (letters, wires, etc.)
    private void drawPuzzle(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 600);

        // Draw the spider image at the bottom
        gc.drawImage(spiderImage, 350, 450, 150, 150);

        gc.setFont(new Font(20));

        // Draw the letters on the left side
        String[] leftLetters = {"A", "B", "C", "D", "E"};
        gc.setFont(new Font("Arial", 28));
        for (int i = 0; i < leftLetters.length; i++) {
            gc.setFill(Color.BLACK);
            gc.fillText(leftLetters[i], wiresXLeft[i], wiresYLeft[i]);
        }

        // Draw the letters on the right side
        String[] rightLetters = {"Anabelle", "Chucky", "Freddy", "Michael", "ghost"};
        gc.setFont(new Font("Arial", 22));  
        for (int i = 0; i < rightLetters.length; i++) {
            gc.setFill(Color.BLACK);
            gc.fillText(rightLetters[i], wiresXRight[i], wiresYRight[i]);
        }

        // Draw completed wires in blue and checkmark
        gc.setStroke(Color.WHITE);
        for (int i = 0; i < connections.length; i++) {
            for (int j = 0; j < connections[i].length; j++) {
                if (connections[i][j]) {
                    gc.strokeLine(wiresXLeft[i] + 20, wiresYLeft[i], wiresXRight[j] - 20, wiresYRight[j]);
                    gc.setFill(Color.GREEN);
                    gc.fillText("✔", wiresXRight[j] + 20, wiresYRight[j]);
                }
            }
        }

        // Draw the currently dragging line in red
        if (isDragging && selectedWireIndex != -1) {
        	gc.setLineWidth(5); //hegdalti tipa
            gc.setStroke(Color.RED);     
            gc.strokeLine(wiresXLeft[selectedWireIndex] + 20, wiresYLeft[selectedWireIndex], lineEndX, lineEndY);
        }
    }

    private void showNextLevel() {
        BorderPane nextLevelPane = new BorderPane();

        // הגדרת תמונת הרקע
        Image backgroundImage = new Image(getClass().getResourceAsStream("/resources/killersfront.png"));
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1400, 1000, false, false, false, false)
        );
        nextLevelPane.setBackground(new Background(background));

        // הוספת אירוע לחיצה על הרקע כדי להתחיל את משחק ה-SpeechToTextGame
        nextLevelPane.setOnMouseClicked(event -> {
            SpeechToTextGame speechGame = new SpeechToTextGame(window);
            speechGame.startSpeechToTextGame(); // הפעלת המשחק SpeechToTextGame
        });

        Scene nextLevelScene = new Scene(nextLevelPane, 1400, 1000);
        window.setScene(nextLevelScene);
    }

}
