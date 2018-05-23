package interstellarDefence;

import processing.core.PApplet;

public class Defender extends ShipEntity {
	
	public Defender(int x, int y, PApplet parent) {
		super(x, y, parent);
		
		shipType = ShipType.PlayerRed1;
		loadTexture(shipType.URL);
			
	}
	
	public void update() {
		super.update();
		
		// Limit the defender the size of the screen.
		if(x < texture.width / 2) {
			x = texture.width / 2;
		} else if(x > parent.width - texture.width / 2) {
			x = parent.width - texture.width / 2;
		}
		
		if(!parent.keyPressed) {
			if(parent.key == ' ') {
				parent.key = 'Q';
				if(lasers.size() < 4) { // Limit the defender to four lasers on screen at any one moment.
					shoot(); // Defender has shot the 'laser canon' so add a new laser to the arrayList for updating and drawing across the screen.
				}
				
			}
		}
	}
	
	public void draw() {
		super.draw();
	}
	
}
