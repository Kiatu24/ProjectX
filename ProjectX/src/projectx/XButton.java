package projectx;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A UI Button that is portable across platforms
 */
public class XButton implements DrawableGameComponent {
	Game game;
	public Texture texture;
	public String text = "";
	public int x = 0;
	public int y = 0;
	public boolean isHover = false;
	
	/**
	 * Creates a new XButton
	 * 
	 * @param game The game
	 * @param text The text to be displayed in the button
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public XButton(Game game, String text, int x, int y) {
		this.game = game;
		this.text = text;
		this.x = x;
		this.y = y;
		texture = new Texture(game, "ButtonBack");
	}
	
	@Override
	public void update() {
		Point mousePos = game.mousePos;
		if (Util.isColliding(getBounds(), new Rectangle(mousePos.x, mousePos.y, 5, 5))) {
			isHover = true;
		}
		else {
			isHover = false;
		}
	}

	@Override
	public void draw() {
		if (isHover) {
			texture.draw(x, y);
		}
	}
	
	@Override
	public void destroy() {
		
	}
	
	/**
	 * Gets the bounds of the button
	 * 
	 * @return A rectangle containing the bounds
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y + texture.height, texture.width, texture.height);
	}
}
