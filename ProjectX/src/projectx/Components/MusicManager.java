package projectx.Components;

import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.*;


/**
 * This class manages the music for the game
 * To use this class, first you have to initialize the manager by creating an instance,
 * then, use the play() with an input of the map type based on the final ints described
 * in this class.
 * 
 * The music will first play an intro file, then move into a repeated section of the music
 */
public class MusicManager extends SoundManager{
	
	// These are the final ints used to distinguish which map type the character is
	// currently traversing
	//
	// WARNING!! DO NOT CHANGE THESE NUMBERS
	// They correspond to the text file containing the filenames of the songs
	public final int VILLAGE = 0;
	public final int BURNT_VILLAGE = 1;
	public final int FIELD = 2;
	public final int FOREST = 3;
	public final int ELF_VILLAGE = 4;
	public final int DUNGEON = 5;
	public final int BATTLE = 6;
	public final int THEME = 7;

	// These two players are used to play the intro and repeats for a song
	private javax.media.Player musicPlayer = null;
	private MediaLocator musicLocator = null;
	private GainControl musicGain = null;
	
	// This vector holds the filenames of the songs
	private Vector<String> musicFileNames = null;
	private Vector<String> introFileNames = null;
	private Vector<URL> musicFileURL = null;
	private Vector<URL> musicIntroURL = null;
	
	// The current map type chosen in the play method
	private int mapType = 0;
	
	/**
	 * This constructor initializes all of the music files for the game
	 * 
	 * The files are stored as FileInputStreams in two vectors:
	 * repeatMusicInputStreams - holds the repeated parts of the songs
	 * introMusicInputStreams - holds the intro parts of the songs
	 * 
	 * THIS CONSTRUCTOR MUST RUN AT THE BEGINNING OF THE GAME!!
	 * This allows for the files to be loaded at any point during the game
	 */
	@SuppressWarnings("deprecation")
	public MusicManager() {
		
		// Initialize scanner to scan the filenames from the text file
		// Maybe check to see if files are .mp2? - SHOULD IMPLEMENT LATER
		File musicNames = new File("lists/main_files");
		File introNames = new File("lists/intro_files");
		Scanner scanMusicNames = null;
		Scanner scanIntroNames = null;
		try {
			scanMusicNames = new Scanner(musicNames);
			scanIntroNames = new Scanner(introNames);
		} catch (FileNotFoundException e1) {
			
		}
		
		// Initialize the musicFileNames vector
		musicFileNames = new Vector<String>();
		introFileNames = new Vector<String>();
		musicFileURL = new Vector<URL>();
		musicIntroURL = new Vector<URL>();
		
		// Scan all the filenames
		while (scanMusicNames.hasNext() && scanIntroNames.hasNext()) {
			String musicFile = scanMusicNames.nextLine();
			String introFile = scanIntroNames.nextLine();
			musicFileNames.add(musicFile);
			introFileNames.add(introFile);
			
			// Use these lines to test that the scanner is pulling the right filenames
//			System.out.println(musicFile);
//			System.out.println(introFile);
		}
		
		System.out.println(musicFileNames.get(0));
		
		for (int i=0; i<musicFileNames.size(); i++) {
			File f1 = new File(musicFileNames.get(i));
			System.out.println(f1.toString());
			File f2 = new File(introFileNames.get(i));
			System.out.println(f2.toString());
			try {
				musicFileURL.add(f1.toURL());
				musicIntroURL.add(f2.toURL());
			} catch (MalformedURLException e) {
				
			}
		}
		
	}
	
	/**
	 * This method begins to play the intro file based on the mapType passed in
	 * 
	 * PASS IN A MAP TYPE FROM THE LIST OF FINAL INTS
	 * IT MAY NOT WORK CORRECTLY OTHERWISE
	 */
	public void play(final int mapType){
		
		// Store the mapType
		this.mapType = mapType;
		
		// Initialize the music locator
		musicLocator = new MediaLocator(musicIntroURL.get(mapType));
		
		// Initialize the music player with the new music locator
		try {
			musicPlayer = Manager.createRealizedPlayer(musicLocator);
		} catch (NoPlayerException e) {
			e.printStackTrace();
		} catch (CannotRealizeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Set up the control listener for transition to repeated file
		// Set up gain control for fade if necessary
		musicPlayer.addControllerListener(new StopListener());
		musicGain = musicPlayer.getGainControl();
		
		// Set initial gain
		musicGain.setDB(0);
		
		// Start the music
		musicPlayer.start();
		
	}
	
	/**
	 * This method fades the music out before beginning the music for a new map type
	 */
	public void stop() {
		
		// Fade which ever player is playing until it stops
		Thread t = new Thread(fade());
		t.start();
		
	}
	
	/**
	 * This method is a runnable thread that actually gradually lowers the gain
	 * to fade out the currently playing song
	 */
	private Runnable fade() {
		
		// Set the original level
		float setLevel = 0;
		
		// Gently fade by .1 db until it reaches -20
		// Wait 20 ms in between each gain change
		while (setLevel > -50) {
			setLevel -= .5;
			musicGain.setDB(setLevel);
			try {
				Thread.sleep(40);
			}
			catch (Exception e) {
				
			}
		}
		
		// Once the music is soft enough, stop the music and close the player
		// allowing for the next player to be created and started for the
		// new map
		musicPlayer.stop();
		musicPlayer.close();
		return null;
	}
	
	/**
	 * This listener listens for the intro file to finish playing, then begins playing
	 * the repeated section of the music
	 */
	private class StopListener implements ControllerListener {

		/**
		 * This method catches the EndOfMediaEvent created when the intro music has
		 * finished playing
		 */
		public void controllerUpdate(ControllerEvent arg0) {
			if (arg0 instanceof EndOfMediaEvent) {
				
				// Added after test -- Need to test again!!
				// Close the current player
				musicPlayer.close();
				
				// Set up the new music locator for the repeated section of the
				// music corresponding with the current map type
				musicLocator = new MediaLocator(musicFileURL.get(mapType));
				
				// Initialize the new music player for the repeated part of the song
				try {
					musicPlayer = Manager.createRealizedPlayer(musicLocator);
				} catch (NoPlayerException e) {
					
				} catch (CannotRealizeException e) {
					
				} catch (IOException e) {
					
				}
				
				// Set up the control listener for transition to repeat
				// Set up gain control for fade if necessary
				musicPlayer.addControllerListener(new RepeatListener());
				musicGain = musicPlayer.getGainControl();
				
				musicPlayer.start();
				
			}
		}
		
	}
	
	/**
	 * This listener listens for the repeated section to finish playing and then
	 * restarts the repeated section
	 */
	private class RepeatListener implements ControllerListener {

		/**
		 * This method catches the created EndOfMediaEvent created when the repeated
		 * section finishes playing
		 */
		public void controllerUpdate(ControllerEvent arg0) {
			if (arg0 instanceof EndOfMediaEvent) {
				
				// Restart the file
				musicPlayer.start();
				
			}
			
		}
		
	}
	
}