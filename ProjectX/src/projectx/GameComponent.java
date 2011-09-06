package projectx;

/**
 * An interface that defines the basic methods for a game component
 */
public interface GameComponent {
	/**
	 * Called to update the component
	 */
	public void update();
	
	/**
	 * Called to dispose of the component
	 */
	public void destroy();
}
