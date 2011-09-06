package projectx;

import java.awt.event.KeyEvent;

/**
 * The GameState for the Overworld gameplay
 */
public class OverworldGameState extends GameState {
	Texture messageBox;
	String messageText = "";
	XButton button;
	
	/**
	 * Creates an instance of the OverworldGameState
	 * @param game The Game that the GameState is a part of
	 */
	public OverworldGameState(Game game) {
		super(game);
		
		// loads a new map
		map = new Map(game);
		map.load("test-map", "1");
		
		// loads a player
		Player player = new Player(game, "Player1", "Male");
		map.setPlayer(player);
		
		Util.splitTexture(game, "Male", 64);
		
		// TODO: make spawn points a part of the map
		map.player.x = 50;
		map.player.y = 50;
		
		messageBox = new Texture(game, "messageBox");
		button = new XButton(game, "Woop", 500, 0);
	}
	
	@Override
	public void update() {
		button.update();
		
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
		
		map.update();
	}
	
	@Override
	public void draw() {
		map.draw();
		
		messageBox.draw(0, game.frame.getHeight() - messageBox.height, game.frame.getWidth(), messageBox.height);
		Util.drawString(game, messageText, 10, game.frame.getHeight() - messageBox.height / 2 + 5);
		
		button.draw();
	}
	
	@Override
	public void handleKeyPressed(KeyEvent key) {
		super.handleKeyPressed(key);
		if (key.getKeyCode() == KeyEvent.VK_SPACE) {
			NPC npc = map.player.checkNPC();
			if (npc != null) {
				messageText = npc.text;
				npc.isTalking = true;
				npc.turnTo(map.player);
				return;
			}
			
			ItemContainer container = map.player.checkItems();
			if (container != null) {
				messageText = container.obtain(map.player);
				return;
			}
		}
	}
	
	@Override
	public void handleKeyReleased(KeyEvent key) {
		super.handleKeyReleased(key);
	}
}
