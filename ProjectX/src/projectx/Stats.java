package projectx;

/**
 * A class that stores RPG Stats for a character
 */
public class Stats {
	public int currentHP = 0;
	public int totalHP = 0;
	public int currentMP = 0;
	public int totalMP = 0;
	public int attack = 0;
	public int defense = 0;
	public int magic = 0;
	public int magicDefense = 0;
	public long exp = 0;
	public int level = 0;
	public boolean isDamagable = false;
	
	/**
	 * Creates an instance of Stats
	 */
	public Stats() {
		
	}
	
	/**
	 * Adds experience
	 * 
	 * @param exp How much exp to add
	 */
	public void addEXP(long exp) {
		this.exp += exp;
		// check to see if the character has grown a level
	}
	
	/**
	 * Calculates damage if this stat is damagable
	 * 
	 * @param other The attacking stat
	 */
	public void calculateDamage(Stats other) {
		if (isDamagable) {
			// calculate damage here
			System.out.println("ouch");
		}
	}
	
	/**
	 * Grows a level
	 */
	public void growLevel() {
		level++;
		// add nodes for level grid
		// do anything else necessary for leveling up
	}
	
	/**
	 * Calculates how much exp is needed to reach the next level
	 * 
	 * @return The amount to next level
	 */
	public long getToNextLevel() {
		// calculate how much the character has to earn to get to the next level
		return 0;
	}
}
