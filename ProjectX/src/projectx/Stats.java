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
	
	/**
	 * Creates an instance of Stats
	 */
	public Stats() {
		
	}
	
	public void addEXP(long exp) {
		this.exp += exp;
		// check to see if the character has grown a level
	}
	
	public void calculateDamage(Stats other) {
		// calculate damage here
	}
	
	public void growLevel() {
		level++;
		// add nodes for level grid
		// do anything else necessary for leveling up
	}
	
	public long getToNextLevel() {
		// calculate how much the character has to earn to get to the next level
		return 0;
	}
}
