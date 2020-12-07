package lifesim.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import lifesim.ApplicationMain;
import lifesim.Tile;
import lifesim.World;
import lifesim.WorldBuilder;
import lifesim.entities.Entity;
import lifesim.entities.EntityFactory;

public class PlayScreen implements Screen {
	//------------------
	//Instance Variables
	//------------------
	private ApplicationMain main;
	
	private int numBunnies;
	private int initialBunnies;
	private int numPlants;
	private int initialPlants;
	private float plantSpawnRate;
	
	private int width;
	private int height;
	
	private World world;
	
	private boolean menuUp;
	private boolean defaultMenu;
	private boolean controlMenu;
	private boolean infoMenu;
	private boolean deathMenu;
	
	private boolean selectionMode;
	
	private EntityFactory factory;
	
	//------------------
	//Constructor
	//------------------
	public PlayScreen(int numBunnies, int numPlants, float plantSpawnRate,
			ApplicationMain main) {
		this.main = main;
		
		this.numBunnies = numBunnies;
		this.numPlants = numPlants;
		this.plantSpawnRate = plantSpawnRate;
		
		initialBunnies = numBunnies;
		initialPlants = numPlants;
		
		menuUp = false;
		defaultMenu = true;
		controlMenu = false;
		infoMenu = false;
		deathMenu = false;
		
		selectionMode = false;
		
		width = 80;
		height = 24;
		
		createWorld();
		
		factory = new EntityFactory(world);
		createEntities();
	}
	
	//------------------
	//Class Methods
	//------------------
	private void createWorld() {
		world = new WorldBuilder(width, height)
				.makePonds()
				.build();
		world.setPlantSpawnRate(plantSpawnRate);
	}
	
	private void createEntities() {
		//Make initial plants
		for (int i = 0; i < numPlants; i++) {
			world.addAtEmptyLocation(Tile.PLANT);
		}
		
		//Make initial bunnies
		for (int i = 0; i < numBunnies; i++) {
			factory.newBunny();
		}
	}
	
	public int getInitBunnies() {
		return initialBunnies;
	}
	
	public int getInitPlants() {
		return initialPlants;
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Entity possibleEntity = world.entityAt(x, y);
				
				if (possibleEntity != null) {
					if (selectionMode) {
						if (world.getSelectedEntity() == possibleEntity) {
							terminal.write(possibleEntity.getGlyph(), x, y,
									possibleEntity.getEntityColor(), AsciiPanel.brightRed);
						}
						else {
							terminal.write(possibleEntity.getGlyph(), x, y,
									possibleEntity.getEntityColor(), 
									possibleEntity.getBackColor());
						}
					}
					else {
						terminal.write(possibleEntity.getGlyph(), x, y,
								possibleEntity.getEntityColor(), 
								possibleEntity.getBackColor());
					}
				}
	
				else {
					terminal.write(world.getGlyph(x, y), x, y, world.getFontColor(x, y), 
							world.getBackColor(x, y));
				}
			}
		}
		
		
		//Menu display
		if (menuUp) {
			for (int x = 5; x < width - 5; x++) {
				for (int y = 2; y < height - 2; y++) {
					if (x == 5 || x == width - 6 || y == 2 || y == height - 3) {
						terminal.write('+', x, y, AsciiPanel.brightBlack, AsciiPanel.black);
					}
					else {
						terminal.write((char)32, x, y, AsciiPanel.black, AsciiPanel.black);
					}
				}
			}
			
			//Main menu
			if (defaultMenu) {
				terminal.writeCenter("-- Menu --", 4);
				
				terminal.writeCenter("- Press [c] to access the controls", 10);
				terminal.writeCenter("- Press [i] to access the info menu", 15);
			}
			
			//Control menu
			else if (controlMenu) {
				terminal.writeCenter("-- Controls --", 4);
				
				terminal.write("- [space]: Start/Stop the simulation", 15, 7);
				terminal.write("- [left/right]: Speed up/Slow down simulation", 15, 10);
				terminal.write("- [backspace]: Exit to new simulation configuration", 15, 13);
				terminal.write("- [s]: Enter selection mode", 15, 16);
				terminal.write("- [up/down]: Change selection in selection mode", 15, 19);
			}
			
			//Info menu
			else if (infoMenu) {
				terminal.writeCenter("-- Info Menu --", 4);
				
				terminal.writeCenter("- Press [d] to look at death info", 10);
			}
			//Death menu
			else if (deathMenu) {
				terminal.writeCenter("-- Deaths --", 4);
				
				terminal.writeCenter("- Old age: " + world.getAgeDeaths(), 10);
				terminal.writeCenter("- Dehydration: " + world.getThirstDeaths(), 13);
				terminal.writeCenter("- Starvation: " + world.getFoodDeaths(), 16);
			}
		}
		
		
		//Selection mode
		if (selectionMode) {
			Entity selectedEntity = world.getSelectedEntity();
			if (selectedEntity != null) {
				for (int x = width - 20; x < width; x++) {
					for (int y = 0; y < 16; y++) {
						if (y == 0 || y == 15 || x == width - 20 || x == width - 1) {
							terminal.write((char)32, x, y,
									AsciiPanel.black, AsciiPanel.brightRed);
						}
						else {
							terminal.write((char)32, x, y);
						}
					}
					
					terminal.write("Selection Mode", width - 17, 2);
					
					//Display entity information
					
					//Gender
					if (selectedEntity.isMale()) {
						terminal.write("- Gender: Male", width - 17, 4);
					}
					else {
						terminal.write("- Gender: Female", width - 18, 4);
					}
					//Age
					if (selectedEntity.getAge() < 1000) {
						terminal.write("- Age: " + selectedEntity.getAge() + 
								"/" + selectedEntity.getBehavior().getAgeCap(), 
								width - 17, 6);
					}
					else {
						terminal.write("- Age: " + selectedEntity.getAge() + 
								"/" + selectedEntity.getBehavior().getAgeCap(), 
								width - 18, 6);
					}
					//Thirst
					if (selectedEntity.getThirst() < 100) {
						if (selectedEntity.getBehavior().getPriority().isThirst()) {
							terminal.write("- Thirst: " + (int)selectedEntity.getThirst() +
									"/" + selectedEntity.getBehavior().getThirstCap(), 
									width - 18, 8, AsciiPanel.brightYellow);
						}
						else {
							terminal.write("- Thirst: " + (int)selectedEntity.getThirst() +
									"/" + selectedEntity.getBehavior().getThirstCap(), 
									width - 18, 8);
						}
					}
					else {
						if (selectedEntity.getBehavior().getPriority().isThirst()) {
							terminal.write("- Thirst: " + (int)selectedEntity.getThirst() +
									"/" + selectedEntity.getBehavior().getThirstCap(), 
									width - 19, 8, AsciiPanel.brightYellow);
						}
						else {
							terminal.write("- Thirst: " + (int)selectedEntity.getThirst() +
									"/" + selectedEntity.getBehavior().getThirstCap(), 
									width - 19, 8);
						}
					}
					//Hunger
					if (selectedEntity.getHunger() < 100) {
						if (selectedEntity.getBehavior().getPriority().isHunger()) {
							terminal.write("- Hunger: " + (int)selectedEntity.getHunger() + 
									"/" + selectedEntity.getBehavior().getHungerCap(), 
									width - 18, 10, AsciiPanel.brightYellow);
						}
						else {
							terminal.write("- Hunger: " + (int)selectedEntity.getHunger() + 
									"/" + selectedEntity.getBehavior().getHungerCap(), 
									width - 18, 10);
						}
					}
					else {
						if (selectedEntity.getBehavior().getPriority().isHunger()) {
							terminal.write("- Hunger: " + (int)selectedEntity.getHunger() + 
									"/" + selectedEntity.getBehavior().getHungerCap(), 
									width - 19, 10, AsciiPanel.brightYellow);
						}
						else {
							terminal.write("- Hunger: " + (int)selectedEntity.getHunger() + 
									"/" + selectedEntity.getBehavior().getHungerCap(), 
									width - 19, 10);
						}
					}
					//Reproduction urge
					if (selectedEntity.getBehavior().getPriority().isReproduction()) {
						terminal.write("- Reproductive", width - 17, 12, 
								AsciiPanel.brightYellow);
						if (selectedEntity.getReproductionUrge() < 100) {
							terminal.write("urge: " + selectedEntity.getReproductionUrge() + 
									"/" + selectedEntity.getBehavior().getReproductionCap(),
									width - 15, 13, AsciiPanel.brightYellow);
						}
						else {
							terminal.write("urge: " + selectedEntity.getReproductionUrge() + 
									"/" + selectedEntity.getBehavior().getReproductionCap(),
									width - 16, 13, AsciiPanel.brightYellow);
						}
					}
					else {
						terminal.write("- Reproductive", width - 17, 12);
						if (selectedEntity.getReproductionUrge() < 100) {
							terminal.write("urge: " + selectedEntity.getReproductionUrge() + 
									"/" + selectedEntity.getBehavior().getReproductionCap(),
									width - 15, 13);
						}
						else {
							terminal.write("urge: " + selectedEntity.getReproductionUrge() + 
									"/" + selectedEntity.getBehavior().getReproductionCap(),
									width - 16, 13);
						}
					}
				}
			}
		}
		
		
		//Info at the bottom of the screen
		terminal.write("Press [escape] to open the menu", 3, 25);
		
		for (int y = 24; y < 27; y++) {
			for (int x = 37; x < 55; x++) {
				terminal.write((char)32, x, y, AsciiPanel.black, AsciiPanel.brightBlack);
			}
			for (int x = 55; x < width; x++) {
				terminal.write((char)32, x, y, AsciiPanel.black, AsciiPanel.cyan);
			}
		}
		
		if (numBunnies < 10) {
			terminal.write("Bunnies: " + world.getEntities().size(), 41, 25, 
					AsciiPanel.black, AsciiPanel.brightBlack);
		}
		else if (numBunnies < 100) {
			terminal.write("Bunnies: " + world.getEntities().size(), 40, 25, 
					AsciiPanel.black, AsciiPanel.brightBlack);
		}
		else {
			terminal.write("Bunnies: " + world.getEntities().size(), 39, 25,
					AsciiPanel.black, AsciiPanel.brightBlack);
		}
	
		
		if (main.getTimePassed() < 100) {
			terminal.write("Time Passed: " + main.getTimePassed(), 60, 25, 
					AsciiPanel.brightWhite, AsciiPanel.cyan);
		}
		else if (main.getTimePassed() < 10000) {
			terminal.write("Time Passed: " + main.getTimePassed(), 59, 25,
					AsciiPanel.brightWhite, AsciiPanel.cyan);
		}
		else {
			terminal.write("Time Passed: " + main.getTimePassed(), 58, 25,
					AsciiPanel.brightWhite, AsciiPanel.cyan);
		}
	}

	@Override
	public Screen respondToUserInput(KeyEvent e) {
		switch(e.getKeyCode()) {
			
		//Menu keys	
		case KeyEvent.VK_ESCAPE:
			if (menuUp) {
				if (defaultMenu) {
					menuUp = false;
				}
				else if (deathMenu) {
					defaultMenu = false;
					controlMenu = false;
					infoMenu = true;
					deathMenu = false;
				}
				else {
					defaultMenu = true;
					controlMenu = false;
					infoMenu = false;
					deathMenu = false;
				}
			}
			else if (!selectionMode) {
				menuUp = true;
			}
			break;
		case KeyEvent.VK_C:
			if (menuUp && defaultMenu) {
				defaultMenu = false;
				controlMenu = true;
				infoMenu = false;
				deathMenu = false;
			}
			break;
		case KeyEvent.VK_I:
			if (menuUp && (defaultMenu || deathMenu)) {
				defaultMenu = false;
				controlMenu = false;
				infoMenu = true;
				deathMenu = false;
			}
			break;
		case KeyEvent.VK_D:
			if (menuUp && infoMenu) {
				infoMenu = false;
				deathMenu = true;
			}
			break;
			
		//Selection mode
		case KeyEvent.VK_S:
			if (!menuUp) {
				selectionMode = !selectionMode;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (selectionMode && !menuUp) {
				world.decrementSelectedEntity();
			}
			break;
		case KeyEvent.VK_UP:
			if (selectionMode && !menuUp) {
				world.incrementSelectedEntity();
			}
			break;
			
		//Others	
		case KeyEvent.VK_BACK_SPACE:
			if (!menuUp) {
				main.stopTimer();
				
				world.resetAgeDeaths();
				world.resetThirstDeaths();
				world.resetFoodDeaths();
				
				return new StartScreen(numBunnies, numPlants, plantSpawnRate, main);
			}
			break;
		case KeyEvent.VK_SPACE:
			if (!menuUp) {
				if (main.isTimerRunning()) {
					main.stopTimer();
				}
				else {
					main.startTimer();
				}
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (!menuUp) {
				if (main.getDelay() > 10) {
					main.speedUpTimer();
				}
			}
			break;
		case KeyEvent.VK_LEFT:
			if (!menuUp) {
				if (main.getDelay() < 1000) {
					main.slowDownTimer();
				}
			}
			break;
		}
		
		return this;
	}
}
