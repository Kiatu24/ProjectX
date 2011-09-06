package projectx;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * A Game class that controls the Game State transitions
 */
public class Game implements GLEventListener, KeyListener, MouseListener {
	public GameState gameState; // the current game state
	public JFrame frame; // the frame that contains the game
	public GL2 gl; // used for drawing images
	public GLU glu = new GLU(); // used for drawing images
	public GLUT glut = new GLUT();
	public Point mousePos = new Point(0, 0);
	
	/**
	 * Creates a Game with a JOGL Frame
	 */
	public Game() {
		frame = Util.newJOGLFrame(this, "Project X", true);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// sets a new drawable instance and clears the screen to black
		gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		mousePos = MouseInfo.getPointerInfo().getLocation();
		
		gameState.update();
		
		gameState.draw();

        gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  
		
		gameState = new OverworldGameState(this);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glViewport(0, 0, width, height);
	}

	@Override
	public void keyPressed(KeyEvent key) {
		// allows the game to exit with the escape key
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		
		gameState.handleKeyPressed(key);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		gameState.handleKeyReleased(key);
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
}
