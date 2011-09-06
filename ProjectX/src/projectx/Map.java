package projectx;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

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
	public Sprite[] allSprites;
	public ItemContainer[] containers;
	public int width, height, tileWidth, tileHeight;
	public Point cameraPos;
	public Player player;
	
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
		
		// loads the textures
		images = Util.splitTexture(game, "MapTiles", 50);
		tileWidth = 49;
		tileHeight = 49;
		
		// position of the camera
		cameraPos = new Point(0, 0);
	}
	
	@Override
	public void update() {
		for (ItemContainer container : containers) {
			if (container.isActive) {
				container.update();
			}
		}
		for (int i = 0; i < npcs.length; i++) {
			npcs[i].update();
		}
		player.update();
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
		Arrays.sort(allSprites);
		for (Sprite sprite : allSprites) {
			if (sprite != null) {
				sprite.draw();
			}
		}
		
		// draw the upper layer
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				DrawTile(upperLayer[x][y], x, y);
			}
		}
	}
	
	@Override
	public void destroy() {
		
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
		bottomLayer = load(mapname, "bottom", version);
		middleLayer = load(mapname, "middle", version);
		upperLayer = load(mapname, "upper", version);
		//npcs = loadNPC(mapname, version);
		loadEventLayer(mapname, version);
		allSprites = new Sprite[npcs.length + 1];
		for (int i = 0; i < npcs.length; i++) {
			allSprites[i] = npcs[i];
		}
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
			int width = Integer.parseInt(dimensions[0]);
			int height = Integer.parseInt(dimensions[1]);
			
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
			e.printStackTrace();
		}
		
		return null;
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
			
			String[] counts = br.readLine().split("/");
			npcs = new NPC[Integer.parseInt(counts[0])];
			containers = new ItemContainer[Integer.parseInt(counts[1])];
			int npcCount = 0;
			int itemCount = 0;
			
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
				ItemEffect.valueOf(tokens[3].trim()));//ItemEffect.HP_PLUS_20);
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
		allSprites[npcs.length] = player;
	}
}
