package projectx.Controllers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import projectx.Components.Game;
import projectx.Components.GameComponent;
import projectx.Components.Util;
import projectx.Maps.CutsceneAction;
import projectx.Maps.Map;
import projectx.Sprite.Enemy;
import projectx.Sprite.Sprite;

public class CutsceneController implements GameComponent{
	private Game game;
	public List<CutsceneAction> actions = new ArrayList<CutsceneAction>();
	private int currentAction = 0;
	private Sprite currentSprite;
	public boolean pauseForText = false;
	private CutsceneAction action;
	public int leeway = 10;
	public String name = "";
	
	public CutsceneController(Game game) {
		this.game = game;
	}

	@Override
	public void update() {
		if ((currentSprite == null || !action.name.equals(currentSprite.name)) && action != null) {
			currentSprite = action.name.equals("Player") ? game.gameState.map.player : game.gameState.map.getSprite(action.name);
			
			if (currentSprite == null) {
				return;
			}
			if (action.x == -1) {
				action.x = currentSprite.x;
			}
			if (action.y == -1) {
				action.y = currentSprite.y;
			}
		}
		
		if (currentSprite != null && currentAction < actions.size()) {
			if (Util.isCloseTo(currentSprite.x, currentSprite.y, action.x, action.y, leeway)) {
				if (action.text.equals("")) {
					currentSprite.direction = action.direction;
					ready();
				}
				else {
					currentSprite.direction = action.direction;
					if (action.text.equals("{Enemy}")) {
						Map m = currentSprite.map;
						Enemy e = new Enemy(game, "???", "Male");
						e.map = m;
						e.x = currentSprite.x;
						e.y = currentSprite.y;
						m.allSprites.add(e);
						m.enemies.add(e);
						currentSprite.hide();
						currentSprite = e;
						game.gameState.messageText = "...";
						pauseForText = true;
					}
					else {
						game.gameState.messageText = action.text;
						pauseForText = true;
					}
				}
			}
			else {
				if (currentSprite.y >= action.y + leeway) {
					currentSprite.moveUp();
				}
				else if (currentSprite.y <= action.y - leeway) {
					currentSprite.moveDown();
				}
				else if (currentSprite.x <= action.x - leeway) {
					currentSprite.moveRight();
				}
				else if (currentSprite.x >= action.x + leeway) {
					currentSprite.moveLeft();
				}
			}
		}
		else {
			game.gameState.map.isCutscene = false;
			game.gameState.messageText = "";
			destroy();
		}
	}

	@Override
	public void destroy() {
		name = "";
		currentSprite = null;
		action = null;
		actions = new ArrayList<CutsceneAction>();
		currentAction = 0;
	}
	
	public void play(String name) {
		if (!this.name.equals(name)) {
			game.gameState.map.isCutscene = true;
			load(name);
		}
	}
	
	public void ready() {
		if (pauseForText) {
			pauseForText = false;
			currentAction++;
			
			if (currentAction < actions.size()) {
				action = actions.get(currentAction);
			}
//			else {
//				game.gameState.map.isCutscene = false;
//				game.gameState.messageText = "";
//				destroy();
//			}
		}
	}
	
	private void load(String name) {
		try {
			FileInputStream fstream = new FileInputStream("cutscenes/" + name);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str;
			
			while ((str = br.readLine()) != null) {
				String[] parts = str.split("/");
				CutsceneAction action = new CutsceneAction();
				
				action.name = parts[0];
				action.x = Integer.parseInt(parts[1]);
				action.y = Integer.parseInt(parts[2]);
				action.direction = Integer.parseInt(parts[3]);
				action.text = parts.length > 4 ? parts[4] : "";
				
				actions.add(action);
			}
			
			fstream.close();
			in.close();
			br.close();
			
			action = actions.get(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
