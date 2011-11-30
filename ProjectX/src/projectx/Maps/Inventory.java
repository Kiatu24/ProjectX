package projectx.Maps;

import java.util.ArrayList;
import java.util.List;


/**
 * A class to manage the Inventory of the player
 */
public class Inventory {
	public long gold = 0;
	public List<Item> items = new ArrayList<Item>();
	
	/**
	 * Creates an instance of an Inventory
	 */
	public Inventory() {
		
	}
	
	/**
	 * Adds an item to the existing collection
	 * 
	 * If an Item exists already, it adds the quantity
	 * If it is a new Item, it adds it
	 * 
	 * @param name The name of the Item to add
	 * @param quantity The quantity of that Item
	 * @param effect The effect the Item has
	 */
	public void addItem(String name, int quantity, ItemEffect effect) {
		if (name == "Gold") {
			gold += quantity;
			return;
		}
		
		boolean isInList = false;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).name == name) {
				isInList = true;
				items.get(i).quantity += quantity;
			}
		}
		
		if (!isInList) {
			items.add(new Item(name, quantity, effect));
		}
	}
}
