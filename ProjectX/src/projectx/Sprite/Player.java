package projectx.Sprite;

import projectx.Components.Game;

/**
 * Defines a Player class that builds on a Sprite
 */
public class Player extends Sprite {
	/**
	 * Creates a new instance of a Player
	 * 
	 * @param game The game
	 * @param name The name of the player
	 * @param filename The name of the file for the image
	 */
	public Player(Game game, String name, String filename) {
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
