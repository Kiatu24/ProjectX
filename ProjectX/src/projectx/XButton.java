package projectx;

import java.awt.Point;
import java.awt.Rectangle;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * A UI Button that is portable across platforms
 */
public class XButton implements DrawableGameComponent {
	Game game;
	public Texture textureDown, textureUp;
	public String text = "";
	public int x = 0;
	public int y = 0;
	public boolean isClicked = false;
	
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
		textureDown = new Texture(game, "ButtonDown");
		textureUp = new Texture(game, "ButtonUp");
	}
	
	@Override
	public void update() {
		Point mousePos = game.mousePos;
		if (game.mouseClicked && Util.isColliding(getBounds(), new Rectangle(mousePos.x, mousePos.y, 5, 5))) {
			isClicked = true;
		}
		else {
			isClicked = false;
		}
	}

	@Override
	public void draw() {
		if (isClicked) {
			textureDown.draw(x, y);
		}
		else {
			textureUp.draw(x, y);
		}
		
		Util.drawString(game, text, x + 10,	y + 18, GLUT.BITMAP_HELVETICA_18);
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
		return new Rectangle(x, y + textureDown.height, textureDown.width, textureDown.height);
	}
}
