package projectx;

/**
 * Defines a Player class that builds on a Sprite
 */
public class Player extends Sprite {
	public Inventory inventory;
	public Stats stats;
	
	/**
	 * Creates a new instance of a Player
	 * 
	 * @param game The game
	 * @param name The name of the player
	 * @param filename The name of the file for the image
	 */
	public Player(Game game, String name, String filename) {
		super(game, name, filename);
		
		inventory = new Inventory();
		stats = new Stats();
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
