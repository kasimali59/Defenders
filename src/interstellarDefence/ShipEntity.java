package interstellarDefence;

import java.util.ArrayList;

import processing.core.PApplet;

public class ShipEntity extends Entity {

int shipVelocity = 6;
	
	public ArrayList<Laser> lasers = new ArrayList<Laser>();
	public ArrayList<Laser> lasersToRemove = new ArrayList<Laser>();
	
	public int laserVelocity = 12;
	
	public ShipType shipType = ShipType.PlayerRed1;
	
	public boolean hasShield = false;
	
	private Shield shield;
		
	public ShipEntity(int x, int y, PApplet parent) {
		
		super(x, y, parent);
				
		shield = new Shield(x, y, parent);
		
	}
	
	public void update() {
		
		super.update();
		
		shield.x = x;
		shield.y = y;
		shield.angle = angle;
		// Iterate through all of the lasers within the list.
		for(Laser l : lasers) {
			if(l.isAlive) {
				l.update(); // If the laser isAlive then update it.
			} else {
				lasersToRemove.add(l); // If not mark it for removal later when safe to do so.
			}
		}
		
		lasers.removeAll(lasersToRemove); // It is now safe to remove the lasers that were previously marked for removal.
		
	}
	
	// Create a laser at the Ships position and provide it with the velocity and angle to allow it to shoot away from the ship.
	public void shoot() {
		Laser l = new Laser(x, y, parent);
		l.xVel = (int) (laserVelocity * Math.cos(angle - Math.PI / 2));
		l.yVel = (int) (laserVelocity * Math.sin(angle - Math.PI / 2));
		l.angle = angle;
		lasers.add(l);
	}
	
	public void draw() {
		
		// Draw all of the lasers. Do it before the super.draw call to create the effect of the lasers being drawn below the ship as if the cannon was below.
		for(Laser l : lasers) {
			l.draw(); 
		}
		
		super.draw();
		if(hasShield) {
			shield.draw();
		}
		
		
	}
	
	public enum ShipType {
		PlayerBlue1 ("data/Player/playerShip1_blue.png"),
		PlayerGreen1 ("data/Player/playerShip1_green.png"),
		PlayerOrange1 ("data/Player/playerShip1_orange.png"),
		PlayerRed1 ("data/Player/playerShip1_red.png"),
		PlayerBlue2 ("data/Player/playerShip2_blue.png"),
		PlayerGreen2 ("data/Player/playerShip2_green.png"),
		PlayerOrange2 ("data/Player/playerShip2_orange.png"),
		PlayerRed2 ("data/Player/playerShip2_red.png"),
		PlayerBlue3 ("data/Player/playerShip3_blue.png"),
		PlayerGreen3 ("data/Player/playerShip3_green.png"),
		PlayerOrange3 ("data/Player/playerShip3_orange.png"),
		PlayerRed3 ("data/Player/playerShip3_red.png"),
		EnemyBlack1 ("data/Enemies/enemyBlack1.png"),
		EnemyBlack2 ("data/Enemies/enemyBlack2.png"),
		EnemyBlack3 ("data/Enemies/enemyBlack3.png"),
		EnemyBlack4 ("data/Enemies/enemyBlack4.png"),
		EnemyBlack5 ("data/Enemies/enemyBlack5.png"),
		EnemyBlue1 ("data/Enemies/enemyBlue1.png"),
		EnemyBlue2 ("data/Enemies/enemyBlue2.png"),
		EnemyBlue3 ("data/Enemies/enemyBlue3.png"),
		EnemyBlue4 ("data/Enemies/enemyBlue4.png"),
		EnemyBlue5 ("data/Enemies/enemyBlue5.png"),
		EnemyGreen1 ("data/Enemies/enemyGreen1.png"),
		EnemyGreen2 ("data/Enemies/enemyGreen2.png"),
		EnemyGreen3 ("data/Enemies/enemyGreen3.png"),
		EnemyGreen4 ("data/Enemies/enemyGreen4.png"),
		EnemyGreen5 ("data/Enemies/enemyGreen5.png"),
		EnemyRed1 ("data/Enemies/enemyRed1.png"),
		EnemyRed2 ("data/Enemies/enemyRed2.png"),
		EnemyRed3 ("data/Enemies/enemyRed3.png"),
		EnemyRed4 ("data/Enemies/enemyRed4.png"),
		EnemyRed5 ("data/Enemies/enemyRed5.png");
		
		public String URL;
		
		ShipType(String URL) {
			this.URL = URL;
		}
		
	}

}
