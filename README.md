

# Escape Room Game

A JavaFX-based interactive escape room game with multiple mini-games, immersive visuals, and sound effects. Players navigate through a series of challenges to complete the game.

## Features

- *Dynamic Gameplay*: Multiple mini-games including Typing Challenge, Maze Game, Button Puzzle, and Wire Puzzle.
- *Immersive Experience*:
  - Fullscreen visuals with scary background images.
  - Scary jumpscares with accompanying sound effects.
- *Progression System*: Players progress through levels with seamless transitions.
- *Custom Design*: Unique graphics and sound resources for an engaging experience.
-*Speech Interaction*: A unique mini-game that uses microphone input to recognize players' speech and provide real-time responses.
  - Engage with the game through voice commands and solve puzzles using your spoken answers.

## Installation
For javaFX:

	1.	Set up your environment:
	•	Install Java 21+ (OpenJDK recommended).
	•	Install JavaFX SDK.
		(the sdk is in the file)
	2.	Add JavaFX to your IDE:
	•	Configure JavaFX libraries in your IDE (e.g., IntelliJ IDEA, Eclipse),add the external jars from the sdk(javafx-sdk-23.0.1--->lib--->mark all the files),
		and the json-20240303.jar file .
	•	Set VM options to include JavaFX:

        --module-path <path-to-javafx-sdk>/lib --add-modules javafx.controls,javafx.fxml,javafx.media

 	for example of VM :
		--module-path "C:\Users\Yourname\Desktop\javafx-sdk-22.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.media

For Python:

  	1. Install **Python** 3.9.13 or later.
	Use pip to install the necessary dependencies:
		pip install sounddevice vosk
	2.Download the vosk-model-small-en-us-0.15
	3.Place the model folder inside the /speech directory in your project.
	Ensure the following structure is in place:
 
	
	├── speech 
 		├─main.py
	├── vosk-model-small-en-us-0.15/

 	4.Run the script to confirm that it works:
		python speech/main.py
	5.Speak into your microphone, and ensure the text is transcribed correctly.

paths:

  	in the python code:
   		change to your path for the vosk:
	line11: model = vosk.Model(r"C:\Users\Yourname\Desktop\eSpeak\vosk-model-small-en-us-0.15")

	in the java code:
		in SpeechToTextGame.java class:
		change the paths for the python file and main.py to your paths (the first is for the pyhton app to show,second for the main.py python code):
 	line 90: "C:\\Users\\Yourname\\AppData\\Local\\Programs\\Python\\Python39\\python.exe",       
	line 91: "C:\\Users\\Yourname\\Desktop\\eSpeak\\speech\\main.py"

Start the game from the Main.java

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
            •       FindDifferentNumberGame: Find the hiding number between all the numbers.
            •       MemoryGame: Match between pairs of killers images.
            •	    SpeechToTextGame: Use your voice to complete the answers
    	4.	Win Conditions:
      	•	Complete all mini-games to escape!



Key Components

	•	JavaFX Framework: Used for UI, animations, and user interactions.
	•	MediaPlayer: Plays audio files for immersive effects.
	•	Scene Management: Each game stage is handled by separate JavaFX scenes.



Author

Developed by [Roy,Shay,Shaked]. Feel free to connect and suggest improvements!
