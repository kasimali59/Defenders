package interstellarDefence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import interstellarDefence.Powerup.PowerType;
import processing.core.PApplet;
import processing.core.PImage;

public class Game {

	private PApplet parent;
	
	private Defender defender;	
		
	private int score = 0;
	private int lives = 3;
	private int killCount = 0;	
	
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();
	private ArrayList<Powerup> powerupsToRemove = new ArrayList<Powerup>();
	private PImage Background;
	
	private ArrayList<Invader> invaders = new ArrayList<Invader>();
	private ArrayList<Invader> invadersToRemove = new ArrayList<Invader>();
	
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
	
	private Invader[][] invadersFormation;
	private int invaderFormationSpeed = 2;
	private int nCols;
	private int nRows;
	
	private final int FORMATION = 1;
	private final int FREE_ROAM = 2;
	private final int GAME_OVER = 3;
	private int gameMode = FORMATION;
	
	public boolean destroyLevel = false;
	
	private File scoreData = null;
	private Scanner in = null;
	private PrintWriter writer = null;
	private ArrayList<Integer> scores = new ArrayList<Integer>();	
	private ArrayList<Integer> newScores = new ArrayList<Integer>();
	private boolean leaderboardUpdated = false;
	
	public Game(int nRows, int nCols, PApplet parent) {	
		this.parent = parent;
		Background = parent.loadImage("Backgrounds/darkPurple.png");
		defender = new Defender(parent.width / 2, parent.height - 70, parent);
		invadersFormation = new Invader[nRows][nCols];
		this.nCols = nCols;
		this.nRows = nRows;
		
		spawnFormation();
		
		loadLeaderboards();
	}
	
	private void spawnFormation() {
		// Loop through the currently empty 2D array and place a new instance of an invader, changing it position from the previous to create a formation of invaders.
		for(int i = 0; i < invadersFormation.length; i++) {
			for(int j = 0; j < invadersFormation[i].length; j++) {
				invadersFormation[i][j] = new Invader(parent.width / invadersFormation.length + i * 120, 100 + 120 * j, parent);
			}
		}
	}
	
	private void resetLevel() {
		invaders.clear();
		spawnFormation();
		gameMode = FORMATION;		
		powerups.clear();
		killCount = 0;
		lives = 3;
		score = 0;
		loadLeaderboards();
		leaderboardUpdated = false;
	}
	
	private void loadLeaderboards() {
		
		scoreData = new File("data/" + nRows + "x" + nCols + ".txt");
		scores.clear();
		try {
			in = new Scanner(scoreData);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}
		
		if(in != null) {
			while(in.hasNext()) {
				scores.add(in.nextInt());
			}
		}
		
		in.close();
		
		newScores.clear();
		newScores.addAll(scores);
		
	}
	
	// Update code goes here.	
	public void update() {
		if(gameMode != GAME_OVER) {
			if(defender.isAlive) {
				defender.update();
				handleDefenderMovement();
				if(gameMode != FORMATION) {
					if(invaders.size() < 5) { // Limit the maximum number of free-moving invaders.
						int width = Math.random() < 0.5 ? -parent.width : parent.width; // Change the side the invaders in on randomly so the defender has to be adaptive.
						int height =  Math.random() < 0.5 ? -parent.height : parent.height;
						invaders.add(new Invader((int)(width + parent.random(0, width / 3)), (int)(height + parent.random(0, height / 3)), parent));
					}
					for(Invader i : invaders) { // Iterate over each invader updating their position and perform other useful logic/checks.
						if(i.isAlive) {
							i.update();
							// Have each invader look and move towards the defender.
							i.angle = (float) (i.angleBetweenEntity(defender) + Math.PI / 2);
							i.xVel = (int) (((ShipEntity) i).shipVelocity / 2 * Math.cos(i.angle + Math.PI / 2));
							i.yVel = (int) (((ShipEntity) i).shipVelocity / 2 * Math.sin(i.angle + Math.PI / 2));
							if(Math.random() > 0.9) { i.shoot(); }; // Randomly probability of shooting at defender. Actual probability of shooting is 0.1 * 0.1 = 0.01
							for(Laser l : defender.lasers) {
								if(l.isColliding(i)) { // Check if the invader has collided with the defenders lasers.
									i.isAlive = false;
									l.isAlive = false;
									score += 10;
									if(Math.random() > 0.9) { // Randomly drop a power up for the defender.
										powerups.add(new Powerup(i.x, i.y, parent));
									}
									explosions.add(new Explosion(i.x, i.y, parent));
								}
							}
							if(defender.isColliding(i)) { // Check if the invader has flown into the defender. 
								i.isAlive = false; 
								if(defender.hasShield) { // Only kill the player if they don't have shield.
									defender.hasShield = false; 
								} else {
									defender.isAlive = false;
								}
							}
							for(Laser l : i.lasers) { // Check to see if the invaders' lasers have collided with the defender.
								if(l.isColliding(defender)) {
									l.isAlive = false;
									if(defender.hasShield) {
										defender.hasShield = false;
									} else {
										defender.isAlive = false;
									}
								}
							}
						} else {
							invadersToRemove.add(i); // Any invaders that are no longer alive need to be removed but must be done when safe. 
						}
					}
				} else {
					updateFormation(); // If formation for update them rather than the free roaming invaders.
				}
			} 
			
			if(lives > 0 && defender.isAlive == false) {
				defender = new Defender(parent.width / 2, parent.height - 70, parent); // If the defender has been killed but still has lives left then respawn the defender.
				invaders.clear(); // Clear the list of invaders to give the player a fighting chance when the respawn.
				lives--;
			}
			
			if(lives == 0) {
				gameMode = GAME_OVER; // Defender ran out of lives so change to gameOver screen.
			}
			
			updatePowerups();
						
			invaders.removeAll(invadersToRemove); // It is now safe to remove invaders that have been mark for removal since the game is no longer looping over the arrayList
									
		} else {
			if(parent.keyPressed) {
				if(parent.key == 'R' || parent.key == 'r') {
					resetLevel();	
				} else if(parent.key == 'B' || parent.key == 'b') {
					destroyLevel = true;
				}
			}
		}
		
		for(Explosion e : explosions) {
			if(e.isAlive) {
				e.update();
			} else {
				explosionsToRemove.add(e);
			}
		}
		
		explosions.removeAll(explosionsToRemove);
		
	}	
	
	private void handleDefenderMovement() {
		if(gameMode == FREE_ROAM) {
			defender.angle = (float) (defender.angleBetweenPoint(parent.mouseX, parent.mouseY) - Math.PI / 2); // Have the ship face the mouse. -Math.PI / 2 corrects the fact the image has the ship face up to being with.
			
			// Steer the defender towards the mouse when the user presses the mouse button down.
			if(parent.mousePressed) {
				defender.xVel = (int) (defender.shipVelocity * Math.cos(defender.angle - Math.PI / 2));
				defender.yVel = (int) (defender.shipVelocity * Math.sin(defender.angle - Math.PI / 2));
			
	 		} else {
	 			defender.xVel = defender.yVel = 0; // Stop the ship from moving as the user has now let go of the mouse.
	 		}
		} else {
			defender.x = parent.mouseX; // Have the defender follow the mouse - horizontally only.
		}
		
	}
	
	private void updateFormation() {		
		for(int i = 0; i < invadersFormation.length; i++) {
			for(int j = 0; j < invadersFormation[i].length; j++) {
				if(invadersFormation[i][j].isAlive) {
					if(Math.random() > 0.99) { invadersFormation[i][j].shoot(); };
					invadersFormation[i][j].update();
					//invaders[i][j].angle = (float) (Math.PI / 2);
					invadersFormation[i][j].xVel = invaderFormationSpeed;
					if(invadersFormation[i][j].x < invadersFormation[i][j].texture.width / 2) {
						invaderFormationSpeed  = 2;
					} else if(invadersFormation[i][j].x > parent.width - invadersFormation[i][j].texture.width / 2) {
						invaderFormationSpeed = -2;
					}
					for(Laser l : defender.lasers) { // Check if the formation invaders collide with the defender's lasers.
						if(invadersFormation[i][j].isColliding(l)) { 
							invadersFormation[i][j].isAlive = false;
							l.isAlive = false;
							score += 10;
							killCount++; // Increase kill count used to determine if the defender has killed all of the formation invaders.
							if(Math.random() > 0.9) { // Invader has died so randomly drop a power up.
								powerups.add(new Powerup(invadersFormation[i][j].x, invadersFormation[i][j].y, parent));
							}
							explosions.add(new Explosion(invadersFormation[i][j].x, invadersFormation[i][j].y, parent));
						}
					}
					for(Laser InvLaser : invadersFormation[i][j].lasers) { // Check if the lasers fired by the formation have collided with the defender.
						if(InvLaser.isColliding(defender)) {
							InvLaser.isAlive = false;
							if(defender.hasShield) {
								defender.hasShield = false;
							} else {
								defender.isAlive = false;
							}
						}
					}
				}				
			}
		}
		if(killCount >= nRows * nCols) {
			gameMode = FREE_ROAM; // Defender has killed all over the formation invader's so change gameMode to free-moving mode.
		}
	}
	
	private void updatePowerups() {
		for(Powerup p : powerups) {
			if(p.isAlive) {
				p.update();
				if(gameMode == FORMATION) {
					p.yVel = 4;
				}
				if(p.isColliding(defender)) { // Check if the power ups dropped have been picked up by the defender.
					p.isAlive = false;
					if(p.powerType.name() == PowerType.values()[0].name()) { // Shield
						defender.hasShield = true;
					} else { // Points Boost.
						score += 100;
					}
				}
			} else {
				powerupsToRemove.add(p); // Power up has been marked for removal due to them leaving the bottom of the screen.
			}
		}
		
		powerups.removeAll(powerupsToRemove); // Now safe to remove power ups.
	}
	
	// Draw code goes here.
	public void draw() {
		update(); // Update function is apart of the processing core. So create one by using the draw call.
		parent.background(0);		
		
		drawBackground();
		
		if(gameMode == FORMATION) {
			drawFormation();
		} else {
			for(Invader i : invaders) {
				i.draw();
			}
		}
		
		for(Explosion e : explosions) {
			e.draw();
		}
		
		drawDefender();
		
		drawPowerups();
						
		drawHUD();	
		
	}	
	
	private void drawLeaderboard() {
		for(Integer i : scores) {
			String scorePosition = "";
			if(scores.indexOf(i) == 0) {
				scorePosition = "1st";
			} else if(scores.indexOf(i) == 1) {
				scorePosition = "2nd";
			} else if(scores.indexOf(i) == 2) {
				scorePosition = "3rd";
			}
			if(i < score) {
				parent.text("New High Score! Well Done!", parent.width / 2, parent.height / 2 - 100);
				if(!leaderboardUpdated) {
					updateLeaderboard();
				}
			}
			parent.text(scorePosition + ": " + i, parent.width / 2, (parent.height / 2 + scores.indexOf(i) * 25) - 50);
		}
	}
	
	private void updateLeaderboard() {
		boolean isNewScore = true; // Boolean prevent the same score being added twice.
		for(Integer i : newScores) {
			if(i == score) {
				isNewScore = false;
			}
		}
		if(isNewScore == true) {
			newScores.add(2, score);
			Collections.sort(newScores);
			Collections.reverse(newScores);
			
			newScores.remove(3);
			
			scoreData.delete();
			
			try {
				writer = new PrintWriter(scoreData); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Integer i : newScores) {
				writer.println(i.toString());
			}
			in.close();
			writer.close();
		}		
		
		leaderboardUpdated = true;
		
	}
	
	private void drawBackground() { // Draw the star filled background. Math.ceil used to round up to ensure the minimum number of tiles is drawn to cover the entire screen.
		for(int i = 0; i < Math.ceil((double)parent.width / Background.width); i++) {
			for(int j = 0; j < Math.ceil((double)parent.height / Background.height); j++) {
				parent.image(Background, i * 256, j * 256);
			}
		}
	}
	
	private void drawDefender() {
		if(defender.isAlive) {
			defender.draw();
		}
	}
	
	private void drawHUD() {
		if(gameMode != GAME_OVER) {
			parent.textSize(24);
			parent.text("Score: " + score, 75, 50);
			parent.text("Lives: " + lives, parent.width - 75, 50);
		} else {
			parent.textSize(24);
			parent.text("Game Over!", parent.width / 2, parent.height / 4);
			drawLeaderboard();
			parent.text("Final Score: " + score , parent.width / 2, parent.height - parent.height / 3);
			parent.text("Press 'R' To Restart Level", parent.width / 2, parent.height - 200);
			parent.text("Press 'B' To Return To Menu", parent.width / 2, parent.height - 150);
		}	
	}
	
	private void drawPowerups() {
		for(Powerup p : powerups) {
			p.draw();
		}
	}
	
	private void drawFormation() {
		for(int i = 0; i < invadersFormation.length; i++) {
			for(int j = 0; j < invadersFormation[i].length; j++) {
				if(invadersFormation[i][j].isAlive) {
					invadersFormation[i][j].draw();
				}				
			}
		}
	}	
}
