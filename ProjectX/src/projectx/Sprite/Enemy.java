package projectx.Sprite;

import projectx.Components.Game;
import projectx.Controllers.AIEnemyController;

public class Enemy extends Sprite {
	public AIEnemyController controller;
	
	/**
	 * Creates a new instance of an enemy
	 * 
	 * @param game The game
	 * @param name The name of the enemy
	 * @param filename The name of the file for the image
	 */
	public Enemy(Game game, String name, String filename) {
		super(game, name, filename);
		controller = new AIEnemyController(game, this);
		this.setWeapon("BasicSword");
		stats.isDamagable = true;
	}
	
	@Override
	public void update() {
		super.update();
		if (!map.isCutscene) {
			controller.update();
		}
	}
	
	@Override
	public void draw() {
		super.draw();
	}
}
