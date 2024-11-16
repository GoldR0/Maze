להלן קובץ README.md עבור הפרויקט שלך, שניתן להעלות ל-GitHub:

# Escape Room Game

A JavaFX-based interactive escape room game with multiple mini-games, immersive visuals, and sound effects. Players navigate through a series of challenges to complete the game.

## Features

- *Dynamic Gameplay*: Multiple mini-games including Typing Challenge, Maze Game, Button Puzzle, and Wire Puzzle.
- *Immersive Experience*:
  - Fullscreen visuals with high-quality background images.
  - Scary jumpscares with accompanying sound effects.
- *Progression System*: Players progress through levels with seamless transitions.
- *Custom Design*: Unique graphics and sound resources for an engaging experience.

## Installation

1. *Clone the repository*:
   ```bash
   git clone https://github.com/<your-username>/escape-room-game.git
   cd escape-room-game

	2.	Set up your environment:
	•	Install Java 21+ (OpenJDK recommended).
	•	Install JavaFX SDK.
	3.	Add JavaFX to your IDE:
	•	Configure JavaFX libraries in your IDE (e.g., IntelliJ IDEA, Eclipse).
	•	Set VM options to include JavaFX:

--module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls,javafx.fxml,javafx.media


	4.	Run the game:
	•	Execute the Main class from your IDE.


How to Play

	1.	Startup:
	•	The game starts with a splash screen featuring multiple clicks to transition.
	2.	Main Menu:
	•	Choose from the available mini-games or start a new game.
	3.	Mini-Games:
	•	Typing Challenge: Type sentences correctly before time runs out.
	•	Maze Game: Navigate through a maze to reach the end point.
	•	Button Puzzle: Solve the puzzle by activating all buttons.
	•	Wire Puzzle: Solve the wiring challenge to power up the generator.
  • FindDifferentNumberGame: Find the hiding number between all the numbers.
  • MemoryGame: Match between pairs of killers images.
	4.	Win Conditions:
	•	Complete all mini-games to escape!



Key Components

	•	JavaFX Framework: Used for UI, animations, and user interactions.
	•	MediaPlayer: Plays audio files for immersive effects.
	•	Scene Management: Each game stage is handled by separate JavaFX scenes.



Author

Developed by [Roy,Shay,Shaked]. Feel free to connect and suggest improvements!
