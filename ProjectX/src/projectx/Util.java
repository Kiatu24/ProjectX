package projectx;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * A utility class for easy access to frequently used code
 */
public class Util {
	/**
	 * Creates a new JFrame with JOGL prepared
	 * 
	 * @param game The Game that will occupy this window
	 * @param title The title of the window
	 * @param fullscreen Whether it's full screen
	 * @return A JOGL-ready JFrame
	 */
	public static JFrame newJOGLFrame(Game game, String title, boolean fullscreen) {
		JFrame frame = new JFrame(title);
		
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		if (fullscreen) {
			frame.setUndecorated(true);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(game);
        canvas.addKeyListener(game);
        canvas.setFocusable(true);
        frame.add(canvas);
		
		frame.setVisible(true);
		FPSAnimator animator = new FPSAnimator(canvas, 60, true);
		animator.start();
		return frame;
	}
	
	/**
	 * Gets a center position for an image
	 * 
	 * @param game The current Game
	 * @param img The image to be centered
	 * @return A point at which the image would be centered
	 */
	public static Point getCenterPos(Game game, BufferedImage img) {
		Point pos = new Point();
		pos.x = game.frame.getWidth() / 2 - game.frame.getWidth() / 2;
		pos.y = game.frame.getHeight() / 2 - game.frame.getHeight() / 2;
		return pos;
	}
	
	/**
	 * Checks if two Rectangles are overlapping or colliding
	 * 
	 * @param box1 The first box to be checked
	 * @param box2 The second box to be checked
	 * @return A boolean verifying a collision
	 */
	public static boolean isColliding(Rectangle box1, Rectangle box2) {
		if (!((box1.x + box1.width) >= box2.x)) {
			return false;
		}
		if (!(box1.x <= (box2.x + box2.width))) {
			return false;
		}     
		if (!((box1.y - box1.height) <= box2.y)) {
			return false;
		}
		if (!(box1.y >= (box2.y - box2.height))) {
			return false;
		}
		return true;
	}
    
    /**
     * Draws text at the specified position
     * 
     * @param game The Game that the string will be drawn in
     * @param text The text to be drawn
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public static void drawString(Game game, String text, int x, int y) {
    	game.gl.glColor3f(0.0f, 0.0f, 0.0f);
    	game.gl.glRasterPos2i(x, y);
		game.glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, text);
    }
    
    /**
     * Splits a texture into individual frames
     * 
     * @param game The game
     * @param texture The texture to be split
     * @param squareSize The size that the squares are
     * @return A Texture[] containing the frames
     */
    public static Texture[] splitTexture(Game game, String textureName, int squareSize) {
    	Texture texture = new Texture(game, textureName);
    	int columns = texture.width / squareSize;
    	int rows = texture.height / squareSize;
    	
    	Texture[] newTextures = new Texture[columns * rows];
    	
    	for (int i = 0; i < newTextures.length; i++) {
    		BufferedImage image = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_ARGB);
    		
    		int xStart = squareSize * (i % columns);
			int yStart = squareSize * (i / columns);
			
			for (int x = xStart; x < xStart + squareSize; x++) {
				for (int y = yStart; y < yStart + squareSize; y++) {
					int rgb = texture.image.getRGB(x, y);
					
					image.setRGB(x - xStart, y - yStart, rgb);
				}
			}
			newTextures[i] = new Texture(game, image);
    	}
    	
    	return newTextures;
    }
}
