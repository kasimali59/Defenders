package interstellarDefence;

import processing.core.PApplet;

public class Laser extends Entity {
	
	public Laser(int x, int y, PApplet parent) {
		super(x, y, parent);
		
		this.loadTexture("data/Lasers/laserRed13.png");
		
	}
	
	public void update() {
		super.update();
				
		if(this.y < 0 || this.y > parent.height || this.x < 0 || this.x > parent.width) {
			this.isAlive = false;
		}		
		
	}
	
	public void draw() {
		super.draw();
	}
	
}
