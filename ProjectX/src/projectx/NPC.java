package projectx;

/**
 * A class for non-playable characters
 */
public class NPC extends Sprite {
	String text = "";
	public AIController controller;
	
	/**
	 * Creates a new instance of an NPC
	 * 
	 * @param game The Game that the NPC is a part of
	 * @param name The name of the NPC
	 * @param filename The name of the file for the image
	 */
	public NPC(Game game, String name, String filename) {
		super(game, name, filename);
		controller = new AIController(game, this);
	}
	
	@Override
	public void update() {
		super.update();
		
		controller.update();
	}
}
