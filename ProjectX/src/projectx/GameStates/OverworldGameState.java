package projectx.GameStates;

import java.awt.event.KeyEvent;

import projectx.Components.Game;
import projectx.Components.Texture;
import projectx.Components.Util;
import projectx.Maps.ItemContainer;
import projectx.Maps.Map;
import projectx.Sprite.NPC;
import projectx.Sprite.Player;

/**
 * The GameState for the Overworld gameplay
 */
public class OverworldGameState extends GameState {
	Texture messageBox;
	
	/**
	 * Creates an instance of the OverworldGameState
	 * @param game The Game that the GameState is a part of
	 */
	public OverworldGameState(Game game) {
		super(game);
		
		// loads a new map
		map = new Map(game);
		map.load("test-forest", "1");
		
		// loads a player
		Player player = new Player(game, "Player1", "Male");
		player.setWeapon("BasicSword");
		map.setPlayer(player);
		
		Util.splitTexture(game, "Male", 64);
		
		// TODO: make spawn points a part of the map
		//map.player.x = 200;
		//map.player.y = 75;
		
		messageBox = new Texture(game, "messageBox");
	}
	
	
	@Override
	public void update()
	{		
		if(game.gameState.isPaused == false)
		{
			// player movement
			if (upPressed) {
				map.player.moveUp();
			}
			if (downPressed) {
				map.player.moveDown();
			}
			if (leftPressed) {
				map.player.moveLeft();
			}
			if (rightPressed) {
				map.player.moveRight();
			}
			if (map.player.moving) {
				if (messageText != "") {
					map.stopTalkingNPCs();
				}
				messageText = "";
			}
		}
		map.update();
	}
	
	@Override
	public void draw() {
		map.draw();
		
		messageBox.draw(0, game.frame.getHeight() - messageBox.height, game.frame.getWidth(), messageBox.height);
		Util.drawString(game, messageText, 10, game.frame.getHeight() - messageBox.height / 2 + 5);
	}
	
	@Override
	public void handleKeyPressed(KeyEvent key) {
		if (map.isCutscene) {
			if (key.getKeyCode() == KeyEvent.VK_SPACE) {
				map.cutscene.ready();
			}
			return;
		}
		
		super.handleKeyPressed(key);
		if (key.getKeyCode() == KeyEvent.VK_SPACE) {
			NPC npc = map.player.checkNPC();
			if (npc != null) {
				messageText = npc.name + ": " + npc.text;
				npc.isTalking = true;
				npc.turnToTalk(map.player);
				return;
			}
			
			ItemContainer container = map.player.checkItems();
			if (container != null) {
				messageText = container.obtain(map.player);
				return;
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_A) {
			map.player.attack();
			map.cutscene.play("test-scene");
		}
	}
	
	@Override
	public void handleKeyReleased(KeyEvent key) {
		super.handleKeyReleased(key);
	}
}
