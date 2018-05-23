package interstellarDefence;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class Entity {
	// This shall provide access to processing core functionality.
	protected PApplet parent;
	// Store the screen coordinates for the image to be drawn.
	public int x;
	public int y;
	// The image used to represent the entity.
	public PImage texture;
	// Store the size of the texture once loaded.
	public int width;
	public int height;
	// Store the velocity that is to be applied to the coordinates.
	public int xVel;
	public int yVel;
	// Refers to the angle at which the entity will be drawn at.
	float angle = 0;
	// isAlive state can be used to mark the entity for removal.
	public boolean isAlive = true;
	
	public Entity(int x, int y, PApplet parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
				
	}
	
	public void loadTexture(String URL) {
		texture = parent.loadImage(URL);
		width = texture.width;
		height = texture.height;
	}
	
	public void update() {
		this.x += xVel;
		this.y += yVel;
	}
	
	public boolean isColliding(Entity other) {
		if(x - width / 2 < other.x + other.width / 2 && x + width / 2 > other.x - other.width / 2 &&
				y - height / 2 < other.y + other.height / 2 && y + height / 2 > other.y - other.height / 2) {
			return true;
		} else {
			return false;
		}
	}
	
	public float angleBetweenPoint(int x, int y) {
		return (float) Math.atan2(this.y - y, this.x - x);
	}
	
	public float angleBetweenEntity(Entity other) {
		return (float) Math.atan2(this.y - other.y, this.x - other.x);
	}
	
	public void draw() {
		parent.pushMatrix(); // Store a copy of the transformation matrix.
		parent.translate(x, y); // Translate the origin point of the coordinate system to that of the entities coordinates.
		parent.imageMode(PConstants.CENTER); // Centre the image.
		parent.rotate(angle); // Apply rotation.
		parent.image(texture, 0, 0); // Draw the image. Centred on the new origin point.
		parent.popMatrix(); // Return back to the original transformation matrix.
		parent.imageMode(PConstants.CORNERS); // Revert back to drawing images on their corners.
	}
		
}
