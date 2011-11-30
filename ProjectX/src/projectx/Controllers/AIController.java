package projectx.Controllers;

import projectx.Components.Game;
import projectx.Components.GameComponent;
import projectx.Sprite.Sprite;

/**
 * A controller for the AI of a sprite
 */
public class AIController implements GameComponent {
	Game game;
	public Sprite sprite;
	public int changeInterval = 50;
	private int changeCounter = 0;
	
	/**
	 * Creates an instance of an AIController
	 * 
	 * @param game The game that contains the sprite
	 * @param sprite The sprite that this will control
	 */
	public AIController(Game game, Sprite sprite) {
		this.game = game;
		this.sprite = sprite;
		sprite.speed = 1;
	}
	
	@Override
	public void update() {
		if (!sprite.isTalking) {
			if (changeCounter <= changeInterval) {
				if (sprite.direction == sprite.UP) {
					sprite.moveUp();
				}
				else if (sprite.direction == sprite.DOWN) {
					sprite.moveDown();
				}
				else {
					sprite.direction = sprite.DOWN;
				}
				changeCounter++;
			}
			else {
				changeCounter = 0;
				if (sprite.direction == sprite.UP) {
					sprite.direction = sprite.DOWN;
				}
				else if (sprite.direction == sprite.DOWN) {
					sprite.direction = sprite.UP;
				}
			}
		}
	}
	
	@Override
	public void destroy() {
		
	}
}
