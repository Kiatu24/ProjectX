package projectx.Controllers;

import java.awt.Point;

import projectx.Components.Game;
import projectx.Components.GameComponent;
import projectx.Components.Util;
import projectx.Sprite.Sprite;

public class AIEnemyController implements GameComponent {
	Game game;
	public Sprite sprite;
	//public int changeInterval = 50;
	//private int changeCounter = 0;
	public Sprite target = null;
	
	/**
	 * Creates an instance of an AIController
	 * 
	 * @param game The game that contains the sprite
	 * @param sprite The sprite that this will control
	 */
	public AIEnemyController(Game game, Sprite sprite) {
		this.game = game;
		this.sprite = sprite;
		sprite.speed = 1;
	}
	
	@Override
	public void update() {
		if (target != null) {
			sprite.move(new Point(target.x - sprite.x, target.y - sprite.y));
			
			if (Util.getDistance(sprite, target) < 40) {
				sprite.turnTo(target);
				sprite.attack();
			}
		}
		else {
			target = game.gameState.map.player;
		}
	}

	@Override
	public void destroy() {
		
	}

}
