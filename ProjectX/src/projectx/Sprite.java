package projectx;

import java.awt.Rectangle;
import java.util.List;

/**
 * A basic Sprite class that handles movement and drawing
 */
public class Sprite implements DrawableGameComponent, Comparable<Sprite> {
	Game game;
	String name = "";
	int x = 0;
	int y = 0;
	int speed = 4;
	int currentFrame = 1;
	Texture[] frames;
	boolean moving = false;
	int animationAdd = 0;
	int interval = 10, attackInterval = 4;
	int animationCounter = 0;
	public boolean isTalking = false;
	
	public Weapon weapon = null;
	public Inventory inventory = null;
	public Stats stats = null;
	
	final int DOWN = 0, UP = 3, RIGHT = 6, LEFT = 9;
	int direction = DOWN;
	
	SpriteStatus status = SpriteStatus.NONE;
	
	private Map map;
	
	/**
	 * Creates a new Sprite instance
	 * 
	 * @param game The Game that the Sprite is a part of
	 * @param name The name of the Sprite
	 * @param filename The filename of the image
	 */
	public Sprite(Game game, String name, String filename) {
		frames = Util.splitTexture(game, filename, 100);

		this.name = name;
		this.game = game;
		
		inventory = new Inventory();
		stats = new Stats();
	}

	@Override
	public void update() {
		if (map == null) {
			this.map = game.gameState.map;
		}
		
		if (status == SpriteStatus.ATTACKING) {
			currentFrame = 12 + direction + animationAdd;
			
			if (animationCounter >= attackInterval) {
				if (animationAdd >= 2) {
					animationAdd = 0;
					
					Enemy enemy = checkEnemies();
					if (enemy != null) {
						attack(enemy);
					}
					
					status = SpriteStatus.NONE;
				}
				else {
					animationAdd++;
				}
				animationCounter = 0;
			}
			else {
				animationCounter++;
			}
		}
		else {
			currentFrame = direction + (moving ? (animationAdd == 3 ? 1 : animationAdd) : 1);
			
			if (moving) {
				if (animationCounter >= interval) {
					if (animationAdd >= 3) {
						animationAdd = 0;
					}
					else {
						animationAdd++;
					}
					animationCounter = 0;
				}
				else {
					animationCounter++;
				}
				
				moving = false;
			}
			else {
				animationAdd = 0;
				animationCounter = 0;
			}
			
			if (weapon != null) {
				weapon.update();
			}
		}
	}

	@Override
	public void draw() {
		frames[currentFrame].draw(x, y);
		if (weapon != null) {
			weapon.draw();
		}
	}

	@Override
	public void destroy() {
		
	}
	
	/**
	 * Moves the sprite right at the given speed
	 */
	public void moveRight() {
		if (!isTalking && status != SpriteStatus.ATTACKING) {
			moving = true;
			direction = RIGHT;
			Rectangle newR = getBounds();
			newR.x += speed;
			if (!checkCollision(newR)) {
				this.x += speed;
			}
		}
	}
	
	/**
	 * Moves the sprite left at the given speed
	 */
	public void moveLeft() {
		if (!isTalking && status != SpriteStatus.ATTACKING) {
			moving = true;
			direction = LEFT;
			Rectangle newR = getBounds();
			newR.x -= speed;
			if (!checkCollision(newR)) {
				this.x -= speed;
			}
		}
	}
	
	/**
	 * Moves the sprite up at the given speed
	 */
	public void moveUp() {
		if (!isTalking && status != SpriteStatus.ATTACKING) {
			moving = true;
			direction = UP;
			Rectangle newR = getBounds();
			newR.y -= speed;
			if (!checkCollision(newR)) {
				this.y -= speed;
			}
		}
	}
	
	/**
	 * Moves the sprite down at the given speed
	 */
	public void moveDown() {
		if (!isTalking && status != SpriteStatus.ATTACKING) {
			moving = true;
			direction = DOWN;
			Rectangle newR = getBounds();
			newR.y += speed;
			if (!checkCollision(newR)) {
				this.y += speed;
			}
		}
	}
	
	/**
	 * Gets the collision bounds for the sprite
	 * 
	 * @return A rectangle containing the bounds
	 */
	public Rectangle getBounds() {
		Rectangle r = new Rectangle(x + 35, y + 35,30, 35);
		return r;
	}
	
	/**
	 * Checks if this Sprite is colliding with the Map's middle layer
	 * 
	 * @param r The rectangle to check against the map
	 * @return Whether there is a collision
	 */
	public boolean checkCollision(Rectangle r) {
		// checks the middle layer of the map
		int[][] ml = map.middleLayer;
		
		for (int x = 0; x < map.width; x++) {
			for (int y = 0; y < map.height; y++) {
				if (ml[x][y] != -1) {
					if (Util.isColliding(r, map.getTileBounds(x, y))) {
						return true;
					}
				}
			}
		}
		
		// checks the NPCs
		NPC[] npcs = map.npcs;
		
		for (int i = 0; i < npcs.length; i++) {
			NPC npc = npcs[i];
			if (npc != this && Util.isColliding(r, npc.getBounds())) {
				return true;
			}
		}
		
		// check the player
		Sprite player = map.player;
		if (player != this && Util.isColliding(r, player.getBounds())) {
			return true;
		}
		
		// check the items
		ItemContainer[] containers = map.containers;
		
		for (int i = 0; i < containers.length; i++) {
			ItemContainer container = containers[i];
			if (container.isActive && Util.isColliding(r, container.getBounds())) {
				return true;
			}
		}
		
		List<Enemy> enemies = map.enemies;
		
		for (Enemy enemy : enemies) {
			if (enemy != this && Util.isColliding(r, enemy.getBounds())) {
				return true;
			}
		}
		
		for (int i = 0; i < game.gameState.map.trans.length; i++) {
			MapTransition trans = game.gameState.map.trans[i];
			if (Util.isColliding(r, trans.getBounds())) 
			{
				game.gameState.switchMap(trans.map, trans.version, game.gameState.map.currentMap, game.gameState.map.currentVersion);
				return true;
			}
		}
		
		// did not find anything
		return false;
	}
	
	/**
	 * Checks collisions against the NPCs if the NPC is not this Spite
	 * 
	 * @return The NPC collided with or null if no collision took place
	 */
	public NPC checkNPC() {
		Rectangle r = getBounds();
		
		r.x += (direction == RIGHT ? 10 : 0);
		r.x += (direction == LEFT ? -10 : 0);
		r.y += (direction == UP ? -10 : 0);
		r.y += (direction == DOWN ? 10 : 0);
		
		NPC[] npcs = map.npcs;
		
		for (int i = 0; i < npcs.length; i++) {
			NPC npc = npcs[i];
			if (npc != this && Util.isColliding(r, npc.getBounds())) {
				return npc;
			}
		}
		return null;
	}
	
	/**
	 * Checks collisions against the Items on the map
	 * 
	 * @return The ItemContainer collided with or null if no collision took place
	 */
	public ItemContainer checkItems() {
		Rectangle r = getBounds();
		
		r.x += (direction == RIGHT ? 10 : 0);
		r.x += (direction == LEFT ? -10 : 0);
		r.y += (direction == UP ? -10 : 0);
		r.y += (direction == DOWN ? 10 : 0);
		
		ItemContainer[] containers = map.containers;
		
		for (int i = 0; i < containers.length; i++) {
			ItemContainer container = containers[i];
			if (container.isActive && Util.isColliding(r, container.getBounds())) {
				return container;
			}
		}
		return null;
	}
	
	public Enemy checkEnemies() {
		Rectangle r = getBounds();
		
		int range = weapon.range;
		r.x += (direction == RIGHT ? range : 0);
		r.x += (direction == LEFT ? -range : 0);
		r.y += (direction == UP ? -range : 0);
		r.y += (direction == DOWN ? range : 0);
		
		List<Enemy> enemies = map.enemies;
		
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			if (enemy != this && Util.isColliding(r, enemy.getBounds())) {
				return enemy;
			}
		}
		return null;
	}
	
	/**
	 * Checks collisions against NPCs and gets the Text associated with them
	 * 
	 * @return The text from the NPC
	 */
	public String getNPCText() {
		NPC n = checkNPC();
		
		if (n != null) 
			return n.text;
		return "";
	}
	
	/**
	 * Turns this sprite to face another
	 * 
	 * @param sprite The Other Sprite that this will face
	 */
	public void turnTo(Sprite sprite) {
		if (sprite.direction == UP) {
			direction = DOWN;
		}
		else if (sprite.direction == DOWN) {
			direction = UP;
		}
		else if (sprite.direction == RIGHT) {
			direction = LEFT;
		}
		else if (sprite.direction == LEFT) {
			direction = RIGHT;
		}
	}
	
	/**
	 * Sets the current weapon
	 * 
	 * @param weaponName The name of the new weapon
	 */
	public void setWeapon(String weaponName) {
		weapon = new Weapon(game, weaponName, weaponName, this);
	}
	
	/**
	 * Attacks an enemy
	 * 
	 * @param enemy The enemy to attack
	 */
	private void attack(Enemy enemy) {
		enemy.stats.calculateDamage(stats);
	}
	
	public void attack() {
		status = SpriteStatus.ATTACKING;
	}

	@Override
	public int compareTo(Sprite other) {
		if (other != null) {
			if (other.y > y) {
				return -1;
			}
			else if (other.y < y) {
				return 1;
			}
			return 0;
		}
		return 0;
	}
}
