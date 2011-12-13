package projectx.Maps;

import java.awt.Rectangle;

public class SceneTile {
	public int x = 0;
	public int y = 0;
	public int width = 50;
	public int height = 50;
	public String name;
	public boolean repeat;
	public boolean hasPlayed = false;
	
	public SceneTile(int x, int y, String name, boolean repeat) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.repeat = repeat;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
}
