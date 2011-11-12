package projectx;

public class Enemy extends Sprite {
	/**
	 * Creates a new instance of an enemy
	 * 
	 * @param game The game
	 * @param name The name of the enemy
	 * @param filename The name of the file for the image
	 */
	public Enemy(Game game, String name, String filename) {
		super(game, name, filename);
		
		stats.isDamagable = true;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void draw() {
		super.draw();
	}
}
