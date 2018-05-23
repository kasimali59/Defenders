package interstellarDefence;


import processing.core.PApplet;
import processing.core.PImage;

public class Explosion extends Entity {

	
	PImage explosionImage[] = new PImage[9];
	
	Timer animationTimer;
	int frameCount = 0;
	public Explosion(int x, int y, PApplet parent) {
		
		super(x, y, parent);
		
		for(int i = 0; i < 9; i++) {
			PImage image;
			image = parent.loadImage("data/Regular-explosion/regularExplosion" + 0 + i + ".png");
			explosionImage[i] = image;
		}
		
		animationTimer = new Timer(parent);
		
		texture = explosionImage[0];
		
	}
	
	public void update() {
		super.update();
		
		animationTimer.step();
		
		if(animationTimer.delta() > 100) {
			frameCount++;
			if(frameCount < explosionImage.length) {
				texture = explosionImage[frameCount];
			} else {
				isAlive = false;
			}
			animationTimer.reset();
			
		}
		
		
	}

}
