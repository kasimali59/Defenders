package interstellarDefence;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {
	
	private PImage Background;
	private Game game;
	
	boolean isPlaying = false;
	
	public void settings() {	
		size(1280, 720);
	}
	
	// Initialisation code goes here.
	public void setup() {
		background(0);
		surface.setTitle("Interstellar Defence");
		Background = loadImage("Backgrounds/darkPurple.png");
	}
	
	// Update code goes here.	
	public void update() {
		if(isPlaying) {
			game.update();
			if(game.destroyLevel) {
				isPlaying = false;
			}
		}
		else {
			if(keyPressed) {
				if(key == '1') {
					game = new Game(2, 3, this);
					isPlaying = true;
				} else if(key == '2') {
					game = new Game(3, 3, this);
					isPlaying = true;
				} else if(key == '3') {
					game = new Game(5, 3, this);
					isPlaying = true;
				}
			}
		}
				
	}
	
	// Draw code goes here.
	public void draw() {
		update(); // Update function is apart of the processing core. So create one by using the draw call.
		background(0);
		
		if(isPlaying) {
			game.draw();
		} else {
			for(int i = 0; i < Math.ceil((double)width / Background.width); i++) {
				for(int j = 0; j < Math.ceil((double)height / Background.height); j++) {
					image(Background, i * 256, j * 256);
				}
			}
			textAlign(CENTER);
			textSize(24);
			text("Interstellar Defence", width / 2, height / 4);
			text("Press '1' To Play Level 1", width / 2, height - 200);
			text("Press '2' To Play Level 2", width / 2, height - 150);
			text("Press '3' To Play Level 3", width / 2, height - 100);
			text("Assets By kenney.nl", 150, height - 50);
		}	
		
	}
		
	// Main entry point for the game.
	public static void main(String[] args) {
		PApplet.main("interstellarDefence.Main");
	}

}


