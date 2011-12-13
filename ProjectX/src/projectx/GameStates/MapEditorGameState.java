package projectx.GameStates;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import projectx.Components.Game;
import projectx.Components.Texture;
import projectx.Maps.ItemContainer;
import projectx.Maps.ItemEffect;
import projectx.Maps.Map;
import projectx.Sprite.NPC;

public class MapEditorGameState extends GameState implements ActionListener {
	public int xTile = 0;
	public int yTile = 0;
	public Map map;
	Texture cursor;
	JFrame optionFrame, eventFrame;
	public int currentImage = 0;
	public boolean shift = false;
    JLabel imageLabel, layerLabel;
    JTextField mapWidthField, mapHeightField, mapNameField, versionField, itemNameField, npcNameField,
    	itemQuantityField, npcTextField;
    JButton resizeButton, newButton, openButton, saveButton, createItemButton, createNPCButton,
    	createSpawnButton, createTransitionButton;
    JComboBox genderSelect, itemSelect;
    final int BOTTOM = 0, MIDDLE = 1, UPPER = 2;
    int currentLayer = BOTTOM;
    ItemEffect[] effects;

	public MapEditorGameState(Game game) {
		super(game);
		map = new Map(game);
		map.drawExtras = true;
		cursor = new Texture(game, "MapTileCursor");
		//cursor.isBlinking = true;
		
		optionFrame = createOptionFrame();
		eventFrame = createEventFrame();
	}

	@Override
	public void update() {
		//map.update();
		cursor.update();
	}
	
	@Override
	public void draw() {
		map.draw();
		cursor.draw(xTile * 49, yTile * 49);
	}
	
	@Override
	public void handleKeyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_UP) {
			if (yTile > 0) yTile--;
		}
		if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			if (yTile < map.height - 1) yTile++;
		}
		if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (shift) {
				if (currentImage >= map.images.length - 1) {
					currentImage = 0;
				}
				else {
					currentImage++;
				}
				setImageIcon();
			}
			else {
				if (xTile < map.width - 1) xTile++;
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			if (shift) {
				if (currentImage <= 0) {
					currentImage = map.images.length - 1;
				}
				else {
					currentImage--;
				}
				setImageIcon();
			}
			else {
				if (xTile > 0) xTile--;
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_F1) {
			optionFrame.setVisible(true);
		}
		if (key.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if (key.getKeyCode() == KeyEvent.VK_SPACE) {
			setCurrentTile(currentImage);
		}
		if (key.getKeyCode() == KeyEvent.VK_C) {
			if (getCurrentTile() != -1) {
				currentImage = getCurrentTile();
				setImageIcon();
			}
		}
		if (key.getKeyCode() == KeyEvent.VK_D) {
			setCurrentTile(-1);
		}
		if (key.getKeyCode() == KeyEvent.VK_1) {
			currentLayer = BOTTOM;
			setLayerLabel();
		}
		if (key.getKeyCode() == KeyEvent.VK_2) {
			currentLayer = MIDDLE;
			setLayerLabel();
		}
		if (key.getKeyCode() == KeyEvent.VK_3) {
			currentLayer = UPPER;
			setLayerLabel();
		}
	}
	
	@Override
	public void handleKeyReleased(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		}
	}
	
	private JFrame createOptionFrame() {
		JFrame frame = new JFrame("Options");
		int width = 300;
		frame.setLocation(game.frame.getWidth() - width - 10, 20);
		frame.setSize(width, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.addKeyListener(game);
		frame.setLayout(new GridLayout(3, 1));
		frame.setFocusable(true);
		frame.setResizable(false);
		
		JPanel helpPanel = new JPanel();
		helpPanel.setLayout(new GridLayout(4, 1));
		
		layerLabel = new JLabel("Bottom");
		ImageIcon imageIcon = new ImageIcon(map.images[currentImage].image);
		imageLabel = new JLabel(imageIcon);
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 2));
		
		imagePanel.add(layerLabel);
		imagePanel.add(imageLabel);
		
		JPanel resizePanel = new JPanel();
		resizePanel.setLayout(new GridLayout(3, 3));
		JPanel widthPanel = new JPanel();
		JPanel heightPanel = new JPanel();
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new GridLayout(1, 4));
		
		mapWidthField = new JTextField("10");
		mapWidthField.setColumns(3);
		mapHeightField = new JTextField("10");
		mapHeightField.setColumns(3);
		mapNameField = new JTextField();
		mapNameField.setColumns(8);
		versionField = new JTextField("1");
		versionField.setColumns(3);
		
		resizeButton = new JButton("Resize");
		resizeButton.addActionListener(this);
		newButton = new JButton("New");
		newButton.addActionListener(this);
		openButton = new JButton("Open");
		openButton.addActionListener(this);
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		helpPanel.add(new JLabel("1=Bottom 2=Middle 3=Event 4=Upper"));
		helpPanel.add(new JLabel("C=Copy D=Delete Space=Place"));
		helpPanel.add(new JLabel());
		helpPanel.add(namePanel);
		
		namePanel.add(new JLabel("Name:"));
		namePanel.add(mapNameField);
		namePanel.add(new JLabel("Version"));
		namePanel.add(versionField);
		
		widthPanel.add(new JLabel("Width:"));
		widthPanel.add(mapWidthField);
		
		heightPanel.add(new JLabel("Height:"));
		heightPanel.add(mapHeightField);
		
		resizePanel.add(newButton);
		resizePanel.add(openButton);
		resizePanel.add(saveButton);
		resizePanel.add(new JLabel());
		resizePanel.add(new JLabel());
		resizePanel.add(new JLabel());
		resizePanel.add(widthPanel);
		resizePanel.add(heightPanel);
		resizePanel.add(resizeButton);
		
		frame.add(helpPanel);
		frame.add(imagePanel);
		frame.add(resizePanel);
		
		frame.setVisible(true);
		game.frame.toFront();
		return frame;
	}
	
	private JFrame createEventFrame() {
		JFrame frame = new JFrame("Events");
		int width = 300;
		frame.setLocation(game.frame.getWidth() - width - 10, 320);
		frame.setSize(width, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.addKeyListener(game);
		frame.setLayout(new GridLayout(7, 1));
		frame.setFocusable(true);
		frame.setResizable(false);
		
		JPanel npcPanel = new JPanel();
		npcNameField = new JTextField();
		npcNameField.setColumns(8);
		genderSelect = new JComboBox(new String[] {"Male", "Female"});
		createNPCButton = new JButton("Add NPC");
		createNPCButton.addActionListener(this);
		npcPanel.add(npcNameField);
		npcPanel.add(genderSelect);
		npcPanel.add(createNPCButton);
		
		JPanel itemPanel = new JPanel();
		itemNameField = new JTextField();
		itemNameField.setColumns(8);
		itemQuantityField = new JTextField();
		itemQuantityField.setColumns(4);
		createItemButton = new JButton("Add Item");
		createItemButton.addActionListener(this);
		itemPanel.add(itemNameField);
		itemPanel.add(itemQuantityField);
		itemPanel.add(createItemButton);
		
		JPanel otherPanel = new JPanel();
		createSpawnButton = new JButton("Add Spawn Point");
		createSpawnButton.addActionListener(this);
		createTransitionButton = new JButton("Create Transition");
		createTransitionButton.addActionListener(this);
		otherPanel.add(createSpawnButton);
		otherPanel.add(createTransitionButton);
		
		JPanel npcLabelPanel = new JPanel(new GridLayout(1, 5));
		npcLabelPanel.add(new JLabel("NPC:"));
		npcLabelPanel.add(new JLabel("Name"));
		npcLabelPanel.add(new JLabel("Gender"));
		npcLabelPanel.add(new JLabel());
		npcLabelPanel.add(new JLabel());
		
		JPanel itemLabelPanel = new JPanel(new GridLayout(1, 5));
		itemLabelPanel.add(new JLabel("Item:"));
		itemLabelPanel.add(new JLabel("Name"));
		itemLabelPanel.add(new JLabel("Quantity"));
		itemLabelPanel.add(new JLabel());
		itemLabelPanel.add(new JLabel());
		
		JPanel npcTextPanel = new JPanel();
		npcTextField = new JTextField(20);
		npcTextPanel.add(new JLabel("Text:"));
		npcTextPanel.add(npcTextField);
		
		JPanel itemEffectPanel = new JPanel();
		effects = ItemEffect.values();
		String[] itemNames = new String[effects.length];
		for (int i = 0; i < effects.length; i++) {
			itemNames[i] = effects[i].name();
		}
		itemSelect = new JComboBox(itemNames);
		itemEffectPanel.add(new JLabel("Effect:"));
		itemEffectPanel.add(itemSelect);
		
		frame.add(npcLabelPanel);
		frame.add(npcPanel);
		frame.add(npcTextPanel);
		frame.add(itemLabelPanel);
		frame.add(itemPanel);
		frame.add(itemEffectPanel);
		frame.add(otherPanel);
		
		frame.setVisible(true);
		game.frame.toFront();
		return frame;
	}
	
	private int getCurrentTile() {
		if (currentLayer == BOTTOM) {
			return map.bottomLayer[xTile][yTile];
		}
		else if (currentLayer == MIDDLE) {
			return map.middleLayer[xTile][yTile];
		}
		else if (currentLayer == UPPER) {
			return map.upperLayer[xTile][yTile];
		}
		else {
			return -1;
		}
	}
	
	private void setCurrentTile(int newTile) {
		if (currentLayer == BOTTOM) {
			map.bottomLayer[xTile][yTile] = newTile;
		}
		else if (currentLayer == MIDDLE) {
			map.middleLayer[xTile][yTile] = newTile;
		}
		else if (currentLayer == UPPER) {
			map.upperLayer[xTile][yTile] = newTile;
		}
	}
	
	private void setImageIcon() {
		imageLabel.setIcon(new ImageIcon(map.images[currentImage].image));
		imageLabel.repaint();
	}
	
	private void setLayerLabel() {
		String text = "";
		if (currentLayer == BOTTOM) {
			text = "Bottom";
		}
		else if (currentLayer == MIDDLE) {
			text = "Middle";
		}
		else if (currentLayer == UPPER) {
			text = "Upper";
		}
		layerLabel.setText(text);
		layerLabel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newButton) {
			map.clear();
		}
		else if (e.getSource() == openButton) {
			String name = mapNameField.getText().trim().replace(' ', '-');
			map.load(name, versionField.getText());
		}
		else if (e.getSource() == saveButton) {
			saveMap(mapNameField.getText(), versionField.getText());
		}
		else if (e.getSource() == resizeButton) {
			map.setSize(Integer.parseInt(mapWidthField.getText()), Integer.parseInt(mapHeightField.getText()));
			xTile = 0;
			yTile = 0;
		}
		else if (e.getSource() == createNPCButton) {
			addNPC(new NPC(game, npcNameField.getText(), "Male"), npcTextField.getText());
		}
		else if (e.getSource() == createItemButton) {
			addItem(new ItemContainer(game, itemNameField.getText(), Integer.parseInt(itemQuantityField.getText()), effects[itemSelect.getSelectedIndex()]));
		}
		else if (e.getSource() == createSpawnButton) {
			addSpawnPoint();
		}
		else if (e.getSource() == createTransitionButton) {
			addTransition();
		}
		
		game.frame.toFront();
	}
	
	private void saveMap(String name, String version) {
		name = name.trim().replace(' ', '-');
		
		try {
			BufferedWriter bout = new BufferedWriter(new FileWriter("maps/" + name + "_bottom_" + version));
			BufferedWriter mout = new BufferedWriter(new FileWriter("maps/" + name + "_middle_" + version));
			BufferedWriter uout = new BufferedWriter(new FileWriter("maps/" + name + "_upper_" + version));
			
			bout.write(map.width + " " + map.height);
			mout.write(map.width + " " + map.height);
			uout.write(map.width + " " + map.height);
			for (int x = 0; x < map.width; x++) {
				String bline = "";
				String mline = "";
				String uline = "";
				for (int y = 0; y < map.height; y++) {
					bline += map.bottomLayer[x][y] + " ";
					mline += map.middleLayer[x][y] + " ";
					uline += map.upperLayer[x][y] + " ";
				}
				
				bout.write("\n" + bline);
				mout.write("\n" + mline);
				uout.write("\n" + uline);
			}
			
			bout.close();
			mout.close();
			uout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		saveEvent(name, version);
	}
	
	private void saveEvent(String name, String version) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("maps/" + name + "_event_" + version));
			
			out.write(map.npcs.length + "/" + map.containers.length + "/" + 
					map.spawnPoints.length + "/" + map.transitions.length);
			for (int i = 0; i < map.npcs.length; i++) {
				NPC n = map.npcs[i];
				String line = "0/" + n.name + "/" + n.x + "/" + n.y + "/" + n.text;
				
				out.write("\n" + line);
			}
			
			for (int i = 0; i < map.containers.length; i++) {
				ItemContainer n = map.containers[i];
				String line = "1/" + n.itemName + "/" + n.quantity + "/" + n.effect.name() + "/" + n.x + "/" + n.y;
				
				out.write("\n" + line);
			}
			
			for (int i = 0; i < map.spawnPoints.length; i++) {
				Texture n = map.spawnPoints[i];
				String line = "2/" + n.x + "/" + n.y;
				
				out.write("\n" + line);
			}
			
			for (int i = 0; i < map.transitions.length; i++) {
				Texture n = map.transitions[i];
				String line = "3/" + n.x + "/" + n.y;
				
				out.write("\n" + line);
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addNPC(NPC npc, String text) {
		NPC[] old = map.npcs.clone();
		map.npcs = new NPC[old.length + 1];
		
		for (int i = 0; i < old.length; i++) {
			map.npcs[i] = old[i];
		}
		
		npc.x = xTile * 49 - 35;
		npc.y = yTile * 49 - 35;
		npc.text = text;
		map.npcs[old.length] = npc;
		for (int i = 0; i < map.npcs.length; i++) {
			map.allSprites.add(map.npcs[i]);
		}
	}
	
	private void addItem(ItemContainer item) {
		ItemContainer[] old = map.containers.clone();
		map.containers = new ItemContainer[old.length + 1];
		
		for (int i = 0; i < old.length; i++) {
			map.containers[i] = old[i];
		}
		
		item.x = xTile * 49 + 13;
		item.y = yTile * 49 + 25;
		map.containers[old.length] = item;
	}
	
	private void addSpawnPoint() {
		Texture[] old = map.spawnPoints;
		map.spawnPoints = new Texture[old.length + 1];
		
		for (int i = 0; i < old.length; i++) {
			map.spawnPoints[i] = old[i];
		}
		
		Texture spawn = new Texture(game, "SpawnPoint");
		spawn.x = xTile * 49;
		spawn.y = yTile * 49;
		map.spawnPoints[old.length] = spawn;
	}
	
	private void addTransition() {
		Texture[] old = map.transitions;
		map.transitions = new Texture[old.length + 1];
		
		for (int i = 0; i < old.length; i++) {
			map.transitions[i] = old[i];
		}
		
		Texture trans = new Texture(game, "MapTransition");
		trans.x = xTile * 49;
		trans.y = yTile * 49;
		map.transitions[old.length] = trans;
	}
}
