package projectx.Maps;

import java.io.*;

import projectx.Components.Game;

public class SaveState implements Serializable 
{
	private static final long serialVersionUID = 2493200789533184106L;
	int playerX;
	int playerY;
	String mapName, mapVersion;
	String data;
	Game game;
	
	public SaveState(Game inputGame) 
	{
		game = inputGame;
		playerX = game.gameState.map.player.x;
		playerY = game.gameState.map.player.y;
		mapName = game.gameState.map.currentMap;
		mapVersion = game.gameState.map.currentVersion;
		
		data = playerX +"/"+ playerY +"/"+ mapName + "/"+ mapVersion;
	}
	
	public void writeSaveFile()
	{
		OutputStream file;
		try {
			file = new FileOutputStream("gameSave.save");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(data);
			output.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void openSaveFile()
	{
		try
		{
			InputStream file = new FileInputStream("gameSave.save");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream (buffer);
			
			String saveData = (String) input.readObject();			
			input.close();
			readSaveData(saveData);
			
		}catch(ClassNotFoundException ex){
		      System.out.println("Cannot read input, class not found");
		}catch(IOException ex){
		      System.out.println("Cannont read input, IOException");	
		}
	}
	
	private void readSaveData(String data)
	{
		String[] str = data.split("/");
		playerX = Integer.parseInt(str[0]);
		playerY = Integer.parseInt(str[1]);
		mapName = str[2];
		mapVersion = str[3];
		
		game.gameState.map.player.x = playerX;
		game.gameState.map.player.y = playerY;
		
		game.gameState.map.load(mapName, mapVersion);
	}
}
