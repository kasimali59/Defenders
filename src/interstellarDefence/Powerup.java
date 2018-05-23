package interstellarDefence;

import processing.core.PApplet;

public class Powerup extends Entity {
	
	public PowerType powerType = PowerType.Shield;
	
	private Timer timer;
		
	public Powerup(int x, int y, PApplet parent) {
		super(x, y, parent);
		
		powerType = PowerType.values()[(Math.random() < 0.5 ? 0 : 1)];
		
		loadTexture(powerType.URL);
		
		timer = new Timer(parent);
		
	}
	
	public void update() {
		super.update();
		
		timer.step();
		// Remove power up after 5 seconds.
		if(timer.delta() > 5000) {
			isAlive = false;
		}
		
		
		if(y > parent.height) {
			isAlive = false;
		}
		
	}
	
	public enum PowerType {
		Shield ("data/Power-ups/shield_gold.png"),
		PointsBoost ("data/Power-ups/star_gold.png");
				
		public String URL;
		
		PowerType(String URL) {
			this.URL = URL;
		}
		
	}
	
}
