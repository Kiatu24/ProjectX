package projectx.Maps;

import java.awt.Rectangle;

import projectx.Components.GameComponent;

/**
 * A spawn and un-spawn point 
 *
 */
public class MapTransition implements GameComponent
{
	public String map, version;
	public int x, y;
	
	/**
	 * Creates an instance of a spawn point
	 * 
	 * @param map = the map the player is going to/coming from
	 * @param x = x point location of transition
	 * @param y = y point location of transition
	 */
	MapTransition(String map, String version, int x, int y)
	{
		this.map = map;
		this.version = version;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public Rectangle getBounds() 
	{
		return new Rectangle(x, y, 49, 49);
	}
}
