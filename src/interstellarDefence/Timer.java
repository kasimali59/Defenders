package interstellarDefence;

import processing.core.PApplet;

public class Timer {
	
	private PApplet parent;
	
	public float milliseconds;
	public float start;
	
	public Timer(PApplet parent) {
		
		this.parent = parent;		
		start = parent.millis();
	}
	
	public void step() {
		milliseconds = parent.millis();
	}
	
	public void reset() {
		start = milliseconds;
	}
	
	public int delta() {
		return (int) (milliseconds - start);
	}
	
}
