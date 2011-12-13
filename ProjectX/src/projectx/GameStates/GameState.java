package projectx.GameStates;

import java.awt.event.KeyEvent;

import projectx.Components.Game;
import projectx.Maps.Map;
import projectx.Maps.MapTransition;
import projectx.Sprite.Player;

/**
 * A class defining a Game State
 * Mostly used for super class purposes
 */
public class GameState {
	public Game game;
	public Map map;
	public boolean upPressed = false, downPressed = false, rightPressed = false, leftPressed = false; // for smooth movement
	public boolean isPaused = false; // for pausing the game
	public String messageText = "";
	
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
		game.music.stop();
		Player p = map.player;
		map.destroy();
		map.load(mapName, version);
		map.setPlayer(p);
		playMusic();
		
		for(MapTransition spawn : game.gameState.map.spawn)
		{
			if(spawn.map.equals(oldMap) && spawn.version.equals(oldVersion))
			{
				map.player.x = spawn.x - 35;
				map.player.y = spawn.y - 30;
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
	
	public void playMusic() {
		if (map.name.equals("homevillage") && map.version == 1) {
			game.music.play(game.music.VILLAGE);
		}
		else if (map.name.equals("homevillage") && map.version == 2) {
			game.music.play(game.music.BURNT_VILLAGE);
		}
		else if (map.name.equals("plains")) {
			game.music.play(game.music.FIELD);
		}
		else if (map.name.equals("forest")) {
			game.music.play(game.music.FOREST);
		}
		else if (map.name.equals("forest2")) {
			game.music.play(game.music.FOREST);
		}
		else if (map.name.equals("village2")) {
			game.music.play(game.music.ELF_VILLAGE);
		}
		else if (map.name.equals("dungeon")) {
			game.music.play(game.music.DUNGEON);
		}
		else if (map.name.equals("dungeon2")) {
			game.music.play(game.music.BATTLE);
		}
	}
}
