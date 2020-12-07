package lifesim.screens;

import asciiPanel.AsciiPanel;
import lifesim.World;

import java.awt.event.KeyEvent;

public interface Screen {
	
	/**
	 * Screen interface allows for less cluttered code by having different
	 * types of screens that display different types of output to the user
	 * 
	 */
	
	//Abstract methods
	public void displayOutput(AsciiPanel terminal);
	
	public Screen respondToUserInput(KeyEvent e);
	
	public World getWorld();
}
