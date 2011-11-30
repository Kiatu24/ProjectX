package projectx.Maps;

import projectx.Components.DrawableGameComponent;
import projectx.Components.Game;
import projectx.Components.Texture;
import projectx.Components.Util;
import projectx.Sprite.Sprite;

public class Weapon implements DrawableGameComponent{
	Texture[] frames;
	String filename = "";
	String name = "";
	Sprite sprite;
	public int range = 10;
	
	public Weapon(Game game, String name, String filename, Sprite sprite) {
		frames = Util.splitTexture(game, filename, 100);
		
		this.name = name;
		this.filename = filename;
		this.sprite = sprite;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		if (sprite.currentFrame != 24) {
			frames[sprite.currentFrame].draw(sprite.x, sprite.y);
		}
	}

	@Override
	public void destroy() {
		
	}
}
