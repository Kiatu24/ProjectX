package projectx;

/**
 * An interface that defines a draw method on top of the Game Component interface
 */
public interface DrawableGameComponent extends GameComponent {
	/**
	 * Called to draw the component
	 */
	public void draw();
}
