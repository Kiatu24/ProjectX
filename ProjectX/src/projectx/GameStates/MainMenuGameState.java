package projectx.GameStates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import projectx.Components.Game;
import projectx.Components.Texture;
import projectx.Maps.Map;


public class MainMenuGameState extends GameState implements ActionListener
{
	Map map;
	Texture background;
	Texture title;
	Texture newGame, load, options; 
	Texture arrowR, arrowL, arrowRbright, arrowLbright;
	int width = game.frame.getWidth();
	int height = game.frame.getHeight();
	int selection = 1;
	boolean rArrowPressed = false, lArrowPressed = false;
	
	public MainMenuGameState(Game game) 
	{
		super(game);
		map = new Map(game);
		background = new Texture(game, "MainMenuBackground");
		title = new Texture(game, "MainMenuTitle");
		newGame = new Texture(game, "newgame");
		load = new Texture(game, "load");
		options = new Texture(game, "options");
		arrowR = new Texture(game, "arrowright");
		arrowL = new Texture(game, "arrowleft");
		arrowRbright = new Texture(game, "arrowright1");
		arrowLbright = new Texture(game, "arrowleft1");
		
		game.music.play(game.music.THEME);
	}

	@Override
	public void update() 
	{
	}
	
	@Override
	public void draw()
	{
		map.draw();
		background.draw();
		title.draw();
		arrowR.draw((int)(width/2+325), (int)(.75*height));
		arrowL.draw((int)(width/2-556), (int)(.75*height));
		
		if(rArrowPressed)
			arrowRbright.draw((int)(width/2+325), (int)(.75*height));
		if(lArrowPressed)
			arrowLbright.draw((int)(width/2-556), (int)(.75*height));
		
		if(selection == 1)
			newGame.draw((int)(width/2-(newGame.width/2)), (int)(.75*height));
		else if(selection == 2)
			load.draw((int)(width/2-(newGame.width/2)), (int)(.75*height));
		else 
			options.draw((int)(width/2-(newGame.width/2)), (int)(.75*height));
	}
	
	@Override
	public void handleKeyPressed(KeyEvent key) 
	{
		if(key.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if(selection < 3)
				selection++;
			else
				selection = 1;
			rArrowPressed = true;
		}
		
		if(key.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if(selection > 0)
				selection--;
			else
				selection = 2;
			lArrowPressed = true;
		}
		
		if(key.getKeyCode() == KeyEvent.VK_ENTER)
			goTo(selection);
	}
	
	@Override
	public void handleKeyReleased(KeyEvent key)
	{
		if(key.getKeyCode() == KeyEvent.VK_RIGHT)
			rArrowPressed = false;
		if(key.getKeyCode() == KeyEvent.VK_LEFT)
			lArrowPressed = false;
	}

	//TODO: need to finish this method 
	private void goTo(int sel)
	{
		if(sel == 1) {
			game.music.stop();
			game.gameState = new OverworldGameState(game);
		}
		else if(sel == 2)
			System.out.println("Game should be loaded");
		else
			System.out.println("Should be in options");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0){}
}