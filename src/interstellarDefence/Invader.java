package interstellarDefence;

import processing.core.PApplet;

public class Invader extends ShipEntity {
		
	public Invader(int x, int y, PApplet parent) {
		super(x, y, parent);
		
		shipType = ShipType.EnemyBlack3;
		loadTexture(shipType.URL);
		
	}
	
	public void update() {
		super.update();
				
	}
	
	public void shoot() {
		if(Math.random() > 0.99) {
			Laser l = new Laser(x, y, parent);
			// Provide the lasers' with the correct velocity and angle when firing out of the invader.
			l.xVel = (int) (laserVelocity * Math.cos(angle + Math.PI / 2));
			l.yVel = (int) (laserVelocity * Math.sin(angle + Math.PI / 2));
			l.angle = angle;
			lasers.add(l);
		}
	}
	
	public void draw() {
				
		super.draw();
	}
	
}
