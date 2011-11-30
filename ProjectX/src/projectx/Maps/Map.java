package projectx.Maps;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import projectx.Components.DrawableGameComponent;
import projectx.Components.Game;
import projectx.Components.Texture;
import projectx.Components.Util;
import projectx.Controllers.CutsceneController;
import projectx.Sprite.Enemy;
import projectx.Sprite.NPC;
import projectx.Sprite.Player;
import projectx.Sprite.Sprite;

/**
 * A Map class that has holds map data, NPC data and player data
 */
public class Map implements DrawableGameComponent{
	Game game;
	public Texture[] images;
	public int[][] bottomLayer;
	public int[][] middleLayer;
	public int[][] eventLayer;
	public int[][] upperLayer;
	public NPC[] npcs;
	public List<Sprite> allSprites;
	public ItemContainer[] containers;
	public Texture[] spawnPoints;
	public Texture[] transitions;
	public List<Enemy> enemies;

	public MapTransition[] trans, spawn;
	public int width, height, tileWidth, tileHeight;
	public Point cameraPos;
	public Player player;
	public boolean drawExtras = false;
	public String currentMap = "";
	public String currentVersion = "";
	
	private Sprite remove = null;
	public boolean isCutscene = false;
	public CutsceneController cutscene;
	public int enemyNumber = 0;
	
	/**
	 * Creates a new instance of a Map
	 * 
	 * @param game The Game that the Map is a part of
	 */
	public Map(Game game) {
		this.game = game;
		// initiates the map to a 10 x 10 grid of grass
		width = 10;
		height = 10;
		
		bottomLayer = new int[width][height];
		middleLayer = new int[width][height];
		upperLayer = new int[width][height];
		npcs = new NPC[0];
		containers = new ItemContainer[0];
		enemies = new ArrayList<Enemy>();
		allSprites = new ArrayList<Sprite>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bottomLayer[x][y] = -1;
				middleLayer[x][y] = -1;
				upperLayer[x][y] = -1;
			}
		}
		
		// loads the textures
		images = Util.splitTexture(game, "MapTiles", 50);
		tileWidth = 49;
		tileHeight = 49;
		
		// position of the camera
		cameraPos = new Point(0, 0);
		spawnPoints = new Texture[0];
		transitions = new Texture[0];
		cutscene = new CutsceneController(game);
	}
	
	@Override
	public void update() {
		if (isCutscene) {
			cutscene.update();
		}
		
		if (remove != null) {
			allSprites.remove(remove);
			enemies.remove(remove);
		}
		
		if(game.gameState.isPaused == false) {
			for (ItemContainer container : containers) {
				if (container.isActive) {
					container.update();
				}
			}
			for (int i = 0; i < npcs.length; i++) {
				npcs[i].update();
			}
			for (Enemy enemy : enemies) {
				enemy.update();
			}
			if (player != null) player.update();
			
			if (drawExtras) {
				for (int i = 0; i < spawnPoints.length; i++) {
					spawnPoints[i].update();
				}

				for (int i = 0; i < transitions.length; i++) {
					transitions[i].update();
				}
			}
		}
	}
	
	@Override
	public void draw() {
		// draw the bottom and middle layers
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				DrawTile(bottomLayer[x][y], x, y);
				DrawTile(middleLayer[x][y], x, y);
			}
		}

		// draw the items
		for (ItemContainer container : containers) {
			if (container.isActive) {
				container.draw();
			}
		}
		
		// sort and draw NPCs and player
		Object[] sprites = allSprites.toArray();
		Arrays.sort(sprites);
		for (Object sprite : sprites) {
			if (sprite != null) {
				Sprite s = (Sprite) sprite;
				s.draw();
			}
		}
		
		// draw the upper layer
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				DrawTile(upperLayer[x][y], x, y);
			}
		}
		
		// draw extras
		if (drawExtras) {
			for (int i = 0; i < spawnPoints.length; i++) {
				spawnPoints[i].draw();
			}
			
			for (int i = 0; i < transitions.length; i++) {
				transitions[i].draw();
			}
		}
		
		if(game.gameState.isPaused == true)
		{
			Texture texture = new Texture(game, "Pause");
			texture.draw(600, 600);
		}
	}
	
	@Override
	public void destroy() 
	{
		bottomLayer = new int [0][0];
		middleLayer = new int [0][0];
		upperLayer = new int [0][0];
		
		npcs = new NPC[0];
		allSprites = new ArrayList<Sprite>();
		containers = new ItemContainer[0];
		trans = new MapTransition[0];
	}
	
	/**
	 * Sets the size of the map, retaining the old map
	 * Essentially just increasing or decreasing size
	 * 
	 * @param newWidth The new width of the map
	 * @param newHeight The new height of the map
	 */
	public void setSize(int newWidth, int newHeight) {
		int oldWidth = width;
		int oldHeight = height;
		this.width = newWidth;
		this.height = newHeight;
		int[][] oldBottom = bottomLayer.clone();
		int[][] oldMiddle = middleLayer.clone();
		int[][] oldUpper = upperLayer.clone();
		
		bottomLayer = new int[newWidth][newHeight];
		middleLayer = new int[newWidth][newHeight];
		upperLayer = new int[newWidth][newHeight];
		
		for (int x = 0; x < newWidth; x++) {
			for (int y = 0; y < newHeight; y++) {
				if (x < oldWidth && y < oldHeight) {
					bottomLayer[x][y] = oldBottom[x][y];
					middleLayer[x][y] = oldMiddle[x][y];
					upperLayer[x][y] = oldUpper[x][y];
				}
				else {
					bottomLayer[x][y] = -1;
					middleLayer[x][y] = -1;
					upperLayer[x][y] = -1;
				}
			}
		}
	}

	/**
	 * Draws a tile of the map at the specified point
	 * 
	 * @param t The number of the image to draw
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	private void DrawTile(int t, int x, int y) {
		if (t != -1) {
			images[t].draw(x * tileWidth - cameraPos.x, y * tileHeight - cameraPos.y);
		}
	}
	
	/**
	 * Loads a map of the given name and version
	 * 
	 * @param mapname Name of the map
	 * @param version Version of the map
	 */
	public void load(String mapname, String version) {
		currentMap = mapname;
		currentVersion = version;
		bottomLayer = load(mapname, "bottom", version);
		middleLayer = load(mapname, "middle", version);
		upperLayer = load(mapname, "upper", version);
		//npcs = loadNPC(mapname, version);
		loadEventLayer(mapname, version);
		for (int i = 0; i < npcs.length; i++) {
			allSprites.add(npcs[i]);
		}
		enemies = loadEnemies(enemyNumber);
	}
	
	/**
	 * A private load function that loads a specified layer of the map
	 * 
	 * @param mapname Name of the map
	 * @param layer Name of the layer
	 * @param version Version of the map
	 * @return A 2D int array containing the map layer data
	 */
	private int[][] load(String mapname, String layer, String version) {
		int[][] layerArr;
		try {
			FileInputStream fstream = new FileInputStream("maps/" + mapname + "_" + layer + "_" + version);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str;
			int x = 0;
			int y = 0;
			
			String[] dimensions = br.readLine().split(" ");
			width = Integer.parseInt(dimensions[0]);
			height = Integer.parseInt(dimensions[1]);
			
			layerArr = new int[width][height];
			
			while((str = br.readLine()) != null) {
				String[] strArr = str.split(" ");
				
				for (int i = 0; i < strArr.length; i++) {
					layerArr[x][y] = Integer.parseInt(strArr[i]);
					
					y++;
				}
				
				y = 0;
				x++;
			}
			br.close();
			in.close();
			fstream.close();
			
			return layerArr;
		} catch (Exception e) {
			System.out.println("Cannot find file: " + mapname + "_" + layer + "_" + version);
			System.exit(0);
		}
		return null;
	}
	
	private List<Enemy> loadEnemies(int number) {
		List<Enemy> e = new ArrayList<Enemy>();
		for (int i = 0; i < number; i++) {
			Enemy enemy = new Enemy(game, "Butthole", "Male");
			
			Random r = new Random();
			int rw = 0;
			int rh = 0;
			
			boolean isInPlace = false;
			while (!isInPlace) {
				rw = r.nextInt(width);
				rh = r.nextInt(height);
				
				enemy.map = this;
				enemy.placeAtTile(rw, rh);
				
				boolean isValid = true;
				if (middleLayer[rw][rh] == -1 && bottomLayer[rw][rh] != -1) {
					for (Sprite s : allSprites) {
						if (Util.isColliding(enemy.getBounds(), s.getBounds())) {
							isValid = false;
						}
					}
					isInPlace = isValid;
				}
			}
			
			e.add(enemy);
			allSprites.add(enemy);
		}
		
		return e;
	}
	
	/**
	 * A private load function that loads the event layer of the map
	 * 
	 * @param mapname Name of the map
	 * @param version Version of the map
	 */
	private void loadEventLayer(String mapname, String version) {
		try {
			FileInputStream fstream = new FileInputStream("maps/" + mapname + "_event_" + version);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str;
			
			int npcCount = 0;
			int itemCount = 0;
			int transCount = 0;
			int spawnCount = 0;
			
			String firstLine = br.readLine();
			if (firstLine != null) {
				String[] counts = firstLine.split("/");
				npcs = new NPC[Integer.parseInt(counts[0])];
				containers = new ItemContainer[Integer.parseInt(counts[1])];
				spawn = new MapTransition[Integer.parseInt(counts[2])];
				trans = new MapTransition[Integer.parseInt(counts[3])];
				enemyNumber = Integer.parseInt(counts[4]);
			}
			else {
				npcs = new NPC[0];
				containers = new ItemContainer[0];
				return;
			}
			
			while((str = br.readLine()) != null) {
				if (!str.startsWith("#")) {
					String[] tokens = str.split("/");
					if (str.startsWith("0")) {
						npcs[npcCount] = loadNPC(tokens);
						npcCount++;
					}
					else if (str.startsWith("1")) {
						containers[itemCount] = loadItem(tokens);
						itemCount++;
					}
					else if (str.startsWith("2"))
					{
						spawn[spawnCount] = loadTransition(tokens);
						spawnCount++;
						System.out.println("set spawn: " + spawnCount);
					}
					else if (str.startsWith("3"))
					{
						trans[transCount] = loadTransition(tokens);
						transCount++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A private load function that loads the Items layer of the map
	 * 
	 * @param tokens The tokens to create the Item
	 * @return An Item
	 */
	private ItemContainer loadItem(String[] tokens) {
		ItemContainer container = new ItemContainer(game, tokens[1].trim(), Integer.parseInt(tokens[2].trim()),
				ItemEffect.valueOf(tokens[3].trim()));
		container.x = Integer.parseInt(tokens[4].trim());
		container.y = Integer.parseInt(tokens[5].trim());
		
		return container;
	}
	
	/**
	 * A private load function that loads the NPC layer of the map
	 * 
	 * @param tokens The tokens to create the NPC
	 * @return An NPC
	 */
	private NPC loadNPC(String[] tokens) {
		NPC n = new NPC(game, tokens[1].trim(), "Male");
		n.x = Integer.parseInt(tokens[2].trim());
		n.y = Integer.parseInt(tokens[3].trim());
		n.text = tokens[4];
		
		return n;
	}
	
	private MapTransition loadTransition(String[] tokens)
	{
		MapTransition trans = new MapTransition(tokens[3].trim(), tokens[4].trim(), Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()));
		
		return trans;
	}
	
	/**
	 * Gets the bounds for a specified tile
	 * 
	 * @param tileX The x coordinate of the Tile
	 * @param tileY The y coordinate of the Tile
	 * @return A rectangle containing the bounds
	 */
	public Rectangle getTileBounds(int tileX, int tileY) {
		return new Rectangle(tileX * tileWidth, tileY * tileHeight, tileWidth, tileHeight);
	}
	
	/**
	 * Resets all NPCs to continue their walking
	 */
	public void stopTalkingNPCs() {
		for (NPC npc : npcs) {
			npc.isTalking = false;
		}
	}
	
	/**
	 * Sets the player for the map to update and draw
	 * 
	 * @param player The player sprite
	 */
	public void setPlayer(Player player) {
		this.player = player;
		player.x = spawn.length > 0 ? spawn[0].x : 10;
		player.y = spawn.length > 0 ? spawn[0].y : 10;
		System.out.println("set player: " + player.x + " " + player.y + "|" + spawn[0].x + " " + spawn[0].y);
		allSprites.add(player);
	}
	
	/**
	 * Clears the map to empty spaces
	 */
	public void clear() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bottomLayer[x][y] = -1;
				middleLayer[x][y] = -1;
				upperLayer[x][y] = -1;
			}
		}
	}
	
	public void removeSprite(Sprite sprite) {
		remove = sprite;
	}
	
	public Sprite getSprite(String name) {
		for (Sprite s : allSprites) {
			if (s.name.equals(name)) {
				return s;
			}
		}
		return null;
	}
}
