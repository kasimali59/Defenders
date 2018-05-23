package interstellarDefence;

import processing.core.PApplet;

public class Shield extends Entity {

	public Shield(int x, int y, PApplet parent) {
		
		super(x, y, parent);
		
		loadTexture("data/Effects/shield2.png");
		
	}

}
