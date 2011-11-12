package projectx;

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
		frames[sprite.currentFrame].draw(sprite.x, sprite.y);
	}

	@Override
	public void destroy() {
		
	}
}
