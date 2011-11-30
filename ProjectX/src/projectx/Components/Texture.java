package projectx.Components;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;


/**
 * A base class for drawing a texture
 * This class handles all of the opengl
 * necessary for drawing textures
 */
public class Texture {
	protected Game game;
	protected WritableRaster raster;
	public int width = 0, height = 0;
	public BufferedImage image = null;
	public boolean isBlinking = false;
	private boolean blink = false;
	public int blinkRate = 30;
	private int blinkCount = 0;
	public int x = 0;
	public int y = 0;
	
	/**
	 * Creates an instance of a Texture
	 * 
	 * @param game The Game that the texture is a part of
	 * @param filename The name of the file
	 */
	public Texture(Game game, String filename) {
		this.game = game;
		try {
		     image = ImageIO.read(new File("images/" + filename + ".png"));
		} catch (IOException e) {
		     e.printStackTrace();
		}
		loadGLStuff();
	}
	
	/**
	 * Creates an instance of a Texture
	 * 
	 * @param game The Game that the texture is a part of
	 * @param bufferedImage The Image to be drawn
	 */
	public Texture(Game game, BufferedImage bufferedImage) {
		this.game = game;
		image = bufferedImage;
		loadGLStuff();
	}
	
	/**
	 * Updates the image, only necessary for blinking
	 */
	public void update() {
		if (isBlinking) {
			if (blinkCount > blinkRate) {
				blink = !blink;
				blinkCount = 0;
			}
			blinkCount++;
		}
	}
	
	/**
	 * Draws the texture at the global x and y of this texture
	 */
	public void draw() {
		if (!isBlinking || blink) {
			draw(x, y);
		}
	}
	
	/**
	 * Draws the image at the specified point
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void draw(int x, int y) {
		if (!isBlinking || blink) {
			draw(x, y, width, height);
		}
	}
	
	/**
	 * Draws the image with the specified bounds
	 * 
	 * @param r The bounds to draw the image in
	 */
	public void draw(Rectangle r) {
		draw(r.x, r.y, r.width, r.height);
	}
	
	/**
	 * Draws the image at the specified point and bounds
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param w The width
	 * @param h The height
	 */
	public void draw(int x, int y, int w, int h) {
		GL2 gl = game.gl;
		DataBufferByte dukeBuf = (DataBufferByte)raster.getDataBuffer();
		byte[] dukeRGBA = dukeBuf.getData();
		ByteBuffer bb = ByteBuffer.wrap(dukeRGBA);
		bb.position(0);
		bb.mark();
		gl.glBindTexture(GL.GL_TEXTURE_2D, 13);
		gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		gl.glTexImage2D (GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, bb);

		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture (GL.GL_TEXTURE_2D, 13);
		gl.glBegin (GL2.GL_POLYGON);
		gl.glTexCoord2d (0, 0);
		gl.glVertex2d(x, y);
		gl.glTexCoord2d(1, 0);
		gl.glVertex2d(x + w, y);
		gl.glTexCoord2d(1, 1);
		gl.glVertex2d(x + w, y + h);
		gl.glTexCoord2d(0, 1);
		gl.glVertex2d(x, y + h);
		gl.glEnd();
	}
	
	/**
	 * Gets the bounds of the texture position at 0, 0
	 * @return A rectangle containing the bounds
	 */
	public Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}
	
	/**
	 * Takes care of loading and setting up the texture
	 */
	private void loadGLStuff() {
		width = image.getWidth();
	    height = image.getHeight();
		
		GL2 gl = game.gl;
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, game.frame.getWidth(), game.frame.getHeight(), 0, 0, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); 
		gl.glEnable (GL.GL_BLEND);
		
		raster = Raster.createInterleavedRaster (DataBuffer.TYPE_BYTE, width, height, 4, null);
		ComponentColorModel colorModel = new ComponentColorModel (ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {8,8,8,8}, true,
				false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
		BufferedImage dukeImg = new BufferedImage (colorModel, raster, false, null);
		Graphics2D g = dukeImg.createGraphics();
		g.drawImage(image, null, null);
	}
}
