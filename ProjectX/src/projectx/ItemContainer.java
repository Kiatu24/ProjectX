package projectx;

import java.awt.Rectangle;

/**
 * A container that can hold Items and be drawn on the map
 */
public class ItemContainer implements DrawableGameComponent {
	public Texture texture;
	public int x = 0;
	public int y = 0;
	Game game;
	public String itemName;
	public int quantity = 0;
	public ItemEffect effect;
	public boolean isActive = true;
	
	/**
	 * Creates an instance of an ItemContainer
	 * 
	 * @param game The game
	 * @param itemName The name of the contained Item
	 * @param quantity The quantity of the contained Item
	 * @param effect The effect of the contained Item
	 */
	public ItemContainer(Game game, String itemName, int quantity, ItemEffect effect) {
		this.game = game;
		this.itemName = itemName;
		this.quantity = quantity;
		this.effect = effect;
		texture = new Texture(game, "ItemBag");
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		texture.draw(x, y);
	}
	
	@Override
	public void destroy() {
		
	}
	
	/**
	 * Gets the bounds of the container
	 * 
	 * @return A rectangle containing the bounds
	 */
	public Rectangle getBounds() {
		return new Rectangle(x + 5, y - 22, 15, 22);
	}
	
	/**
	 * Gives the player the Item and returns the message to display
	 * 
	 * @param player The player who picked the Item up
	 * @return The message from the Item
	 */
	public String obtain(Player player) {
		String message = "You obtained " + quantity + " " + itemName + 
				(quantity == 1 ? "" : (itemName.contains("Gold") ? "" : "s"));
		player.inventory.addItem(itemName, quantity, effect);
		isActive = false;
		return message;
	}
	
	@Override
	public String toString() {
		return itemName + ": " + effect + " x" + quantity;
	}
}
