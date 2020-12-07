package lifesim.screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import lifesim.ApplicationMain;
import lifesim.World;

public class StartScreen implements Screen {
	//------------------
	//Instance Variables
	//------------------
	private ApplicationMain main;
	
	int numBunnies;
	int numPlants;
	float plantSpawnRate;
	int selector;
	
	//------------------
	//Constructor
	//------------------
	public StartScreen(int numBunnies, int numPlants, float plantSpawnRate, 
			ApplicationMain main) {
		this.main = main;
		
		this.numBunnies = numBunnies;
		this.numPlants = numPlants;
		this.plantSpawnRate = plantSpawnRate;
		selector = 0;
	}
	
	//------------------
	//Class Methods
	//------------------
	@Override
	public World getWorld() {
		return null;
	}

	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("CONFIGURE SIMULATION SETTINGS:", 2);
		terminal.writeCenter("-- How many bunnies? --", 8);
		if (selector == 0) {
			terminal.writeCenter("--> << " + numBunnies + " >>", 10);
			terminal.writeCenter("-- How many plants? --", 12);
			terminal.writeCenter("<< " + numPlants + " >>", 14);
			terminal.writeCenter("-- Plant spawn rate? --", 16);
			terminal.writeCenter("<< " + Math.round(plantSpawnRate*100) + "% >>", 18);
			terminal.writeCenter("press [enter] to confirm", 24);
		}
		else if (selector == 1) {
			terminal.writeCenter("<< " + numBunnies + " >>", 10);
			terminal.writeCenter("-- How many plants? --", 12);
			terminal.writeCenter("--> << " + numPlants + " >>", 14);
			terminal.writeCenter("-- Plant spawn rate? --", 16);
			terminal.writeCenter("<< " + Math.round(plantSpawnRate*100) + "% >>", 18);
			terminal.writeCenter("press [enter] to confirm", 24);
		}
		else {
			terminal.writeCenter("<< " + numBunnies + " >>", 10);
			terminal.writeCenter("-- How many plants? --", 12);
			terminal.writeCenter("<< " + numPlants + " >>", 14);
			terminal.writeCenter("-- Plant spawn rate? --", 16);
			terminal.writeCenter("--> << " + Math.round(plantSpawnRate*100) + "% >>", 18);
			terminal.writeCenter("press [enter] to confirm", 24);
		}
	}

	@Override
	public Screen respondToUserInput(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			main.resetDelay();
			main.resetTimePassed();
			return new PlayScreen(numBunnies, numPlants, plantSpawnRate, main);
		case KeyEvent.VK_LEFT:
			if (selector == 0) {
				if (numBunnies > 1) {
					numBunnies--;
				}
			}
			else if (selector == 1) {
				if (numPlants > 1) {
					numPlants--;
				}
			}
			else {
				if (plantSpawnRate > 0.05) {
					plantSpawnRate -= 0.05;
				}
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (selector == 0) {
				if (numBunnies < 150) {
					numBunnies++;
				}
			}
			else if (selector == 1) {
				if (numPlants < 150) {
					numPlants++;
				}
			}
			else {
				if (plantSpawnRate < 1.0) {
					plantSpawnRate += 0.05;
				}
			}
			break;
		case KeyEvent.VK_UP:
			if (selector > 0) {
				selector--;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (selector < 2) {
				selector++;
			}
		}
		
		return this;
	}
}
