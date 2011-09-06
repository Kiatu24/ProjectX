package projectx;

/**
 * An Item that can be used by the player
 */
public class Item implements GameComponent {
	public String name;
	public ItemEffect effect;
	public int quantity;
	
	/**
	 * Creates an instance of an Item
	 * 
	 * @param name The name of the Item
	 * @param quantity The quantity of the Item
	 * @param effect The effect the Item has on the player
	 */
	public Item(String name, int quantity, ItemEffect effect) {
		this.name = name;
		this.effect = effect;
		this.quantity = quantity;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void destroy() {
		
	}
}
