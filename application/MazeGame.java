package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import application.ButtonPuzzle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MazeGame {

    private ImageView scareImageView;
    private Image scareImage;
    private MediaPlayer scareSound;

    private static final Logger logger = Logger.getLogger(MazeGame.class.getName());

    private static final int ROWS = 20;
    private static final int COLS = 30;
    private static final int CELL_SIZE = 40;
    private static final int WIDTH = COLS * CELL_SIZE;
    private static final int HEIGHT = ROWS * CELL_SIZE;

    private Cell[][] grid = new Cell[ROWS][COLS];
    private Cell player;
    private Cell end;
    private Timeline gameLoop;
    private Stage window;

    private boolean gameEnded = false;
    private static final double PLAYER_SIZE = (CELL_SIZE - 10) / 2;

    public MazeGame(Stage window) {
        this.window = window;

        scareImage = new Image(getClass().getResourceAsStream("/resources/jumpscare1.png"));
        scareImageView = new ImageView(scareImage);
        scareImageView.setFitWidth(WIDTH);
        scareImageView.setFitHeight(HEIGHT);
        scareImageView.setVisible(false);

        // אתחול קובץ השמע
        Media scareMedia = new Media(getClass().getResource("/resources/scary.mp3").toString());
        scareSound = new MediaPlayer(scareMedia);
    }

    public void startMazeGame() {
        logger.info("Starting the Maze game");

        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        root.getChildren().add(scareImageView);

        initializeGrid();
        generateMaze();

        player = grid[0][0];
        end = grid[ROWS - 1][COLS - 1];

        drawMaze(gc);

        gameLoop = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            drawMaze(gc);
            if (player == end && !gameEnded) {
                gameEnded = true;
                gc.setFill(Color.GREEN);
                gc.fillText("You Win the Maze!, Lets go to the next room", WIDTH / 2 - 40, HEIGHT / 2);
                gameLoop.stop();
                goToNextGame();
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this::handlePlayerMovement);

        window.setScene(scene);
        window.setTitle("Maze Game");
        window.setFullScreen(true);
        window.show();
    }

    private void goToNextGame() {
        Image nextImage = new Image(getClass().getResourceAsStream("/resources/maavardark1.png"));
        ImageView nextImageView = new ImageView(nextImage);
        nextImageView.setPreserveRatio(false);
        nextImageView.fitWidthProperty().bind(window.widthProperty());
        nextImageView.fitHeightProperty().bind(window.heightProperty());

        BorderPane layout = new BorderPane(nextImageView);
        nextImageView.setOnMouseClicked(e -> {
            ButtonPuzzle buttonPuzzle = new ButtonPuzzle(window);
            buttonPuzzle.startButtonPuzzle();
        });

        Scene nextScene = new Scene(layout, window.getWidth(), window.getHeight());
        window.setScene(nextScene);
        window.setFullScreen(true);
    }

    private void initializeGrid() {
        logger.info("Initializing maze grid");
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }
    }

    private void generateMaze() {
        logger.info("Generating maze using recursive backtracking");
        Cell start = grid[0][0];
        start.visited = true;
        List<Cell> stack = new ArrayList<>();
        stack.add(start);

        while (!stack.isEmpty()) {
            Cell current = stack.get(stack.size() - 1);
            Cell next = getUnvisitedNeighbor(current);

            if (next != null) {
                next.visited = true;
                removeWalls(current, next);
                stack.add(next);
            } else {
                stack.remove(stack.size() - 1);
            }
        }

        end = grid[ROWS - 1][COLS - 1];
        end.bottomWall = false;
        logger.info("Maze generated with path to end.");
    }

    private Cell getUnvisitedNeighbor(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();

        if (cell.row > 0 && !grid[cell.row - 1][cell.col].visited) neighbors.add(grid[cell.row - 1][cell.col]);
        if (cell.row < ROWS - 1 && !grid[cell.row + 1][cell.col].visited) neighbors.add(grid[cell.row + 1][cell.col]);
        if (cell.col > 0 && !grid[cell.row][cell.col - 1].visited) neighbors.add(grid[cell.row][cell.col - 1]);
        if (cell.col < COLS - 1 && !grid[cell.row][cell.col + 1].visited) neighbors.add(grid[cell.row][cell.col + 1]);

        if (neighbors.isEmpty()) {
            return null;
        }
        Collections.shuffle(neighbors);
        return neighbors.get(0);
    }

    private void removeWalls(Cell current, Cell next) {
        int x = current.col - next.col;
        int y = current.row - next.row;

        if (x == 1) {
            current.leftWall = false;
            next.rightWall = false;
        } else if (x == -1) {
            current.rightWall = false;
            next.leftWall = false;
        } else if (y == 1) {
            current.topWall = false;
            next.bottomWall = false;
        } else if (y == -1) {
            current.bottomWall = false;
            next.topWall = false;
        }
    }

    private void drawMaze(GraphicsContext gc) {
        gc.setFill(Color.rgb(245, 245, 220));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.clearRect(0, 0, WIDTH, HEIGHT);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col].draw(gc);
            }
        }

        gc.setFill(Color.BLUE);
        gc.fillOval(player.col * CELL_SIZE + 10, player.row * CELL_SIZE + 10, PLAYER_SIZE, PLAYER_SIZE);

        gc.setFill(Color.RED);
        gc.fillRect(end.col * CELL_SIZE + 5, end.row * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
    }

    private void handlePlayerMovement(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        int newRow = player.row;
        int newCol = player.col;

        switch (keyCode) {
            case W -> newRow--;
            case S -> newRow++;
            case A -> newCol--;
            case D -> newCol++;
            default -> { return; }
        }

        if (isValidMove(newRow, newCol, keyCode)) {
            player = grid[newRow][newCol];
            logger.info("Player moved to (" + newRow + ", " + newCol + ")");
        } else {
            logger.warning("Invalid move, resetting player to start.");
            resetPlayer();
        }
    }

    private boolean isValidMove(int newRow, int newCol, KeyCode direction) {
        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
            logger.warning("Move out of bounds.");
            return false;
        }

        Cell current = grid[player.row][player.col];
        Cell target = grid[newRow][newCol];

        return switch (direction) {
            case W -> !current.topWall && !target.bottomWall;
            case S -> !current.bottomWall && !target.topWall;
            case A -> !current.leftWall && !target.rightWall;
            case D -> !current.rightWall && !target.leftWall;
            default -> false;
        };
    }

    private void resetPlayer() {
        scareImageView.setFitWidth(window.getWidth());
        scareImageView.setFitHeight(window.getHeight());
        scareImageView.setVisible(true);

        // הפעלת השמע
        scareSound.stop(); // עוצר כל השמעה קודמת, במידה ויש
        scareSound.play(); // נגן מחדש את השמע

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
            scareImageView.setVisible(false);
            scareImageView.setFitWidth(WIDTH);
            scareImageView.setFitHeight(HEIGHT);
            player = grid[0][0];
            logger.info("Player reset to start point (0, 0)");
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private class Cell {
        int row, col;
        boolean visited = false;
        boolean topWall = true, bottomWall = true, leftWall = true, rightWall = true;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        void draw(GraphicsContext gc) {
            gc.setStroke(Color.BLACK);

            if (topWall) gc.strokeLine(col * CELL_SIZE, row * CELL_SIZE, (col + 1) * CELL_SIZE, row * CELL_SIZE);
            if (rightWall) gc.strokeLine((col + 1) * CELL_SIZE, row * CELL_SIZE, (col + 1) * CELL_SIZE, (row + 1) * CELL_SIZE);
            if (bottomWall) gc.strokeLine((col + 1) * CELL_SIZE, (row + 1) * CELL_SIZE, col * CELL_SIZE, (row + 1) * CELL_SIZE);
            if (leftWall) gc.strokeLine(col * CELL_SIZE, (row + 1) * CELL_SIZE, col * CELL_SIZE, row * CELL_SIZE);
        }
    }
}
