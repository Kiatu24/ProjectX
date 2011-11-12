package projectx;

import java.awt.event.KeyEvent;

/**
 * A class defining a Game State
 * Mostly used for super class purposes
 */
public class GameState {
	Game game;
	Map map;
	boolean upPressed = false, downPressed = false, rightPressed = false, leftPressed = false; // for smooth movement
	boolean isPaused = false; // for pausing the game
	
	/**
	 * Creates an instance of a GameState
	 * @param game The Game that the GameState is a part of
	 */
	public GameState(Game game) {
		this.game = game;
	}
	
	/**
	 * Changes maps
	 * 
	 * @param mapName the name of the destination map
	 * @param version the version of the destination map
	 * @param x the player spawn x coordinate
	 * @param y the player spawn y coordinate
	 * Updates the gamestate
	 */
	public void switchMap(String mapName, String version, String oldMap, String oldVersion)
	{
		map.destroy();
		map.load(mapName, version);	
		
		for(MapTransition spawn : game.gameState.map.spawn)
		{
			if(spawn.map.equals(oldMap) && spawn.version.equals(oldVersion))
			{
				map.player.x = spawn.x;
				map.player.y = spawn.y - 16;
			}
		}
	}
	
	public void pauseGame()
	{
		if(isPaused == false)
		{	
			isPaused = true;
		}
		else 
			isPaused = false;
	}
	
	/**
	 * Updates the gamestate
	 */
	public void update() {}
	
	/**
	 * Draws the gamestate
	 */
	public void draw() {
		
	}
	
	/**
	 * Destroys the gamestate
	 */
	public void destroy() {
		
	}
	
	/**
	 * Gets the next GameState
	 * @return The next GameState
	 */
	public GameState getNext() {
		return null;
	}
	
	/**
	 * Handles Key Pressed Events for this GameState
	 * @param key The KeyEvent from the Game
	 */
	public void handleKeyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if(key.getKeyCode() == KeyEvent.VK_P)
		{
			pauseGame();
		}
	}
	
	/**
	 * Handles Key Released Events for this GameState
	 * @param key The KeyEvent from the Game
	 */
	public void handleKeyReleased(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
	}
}
