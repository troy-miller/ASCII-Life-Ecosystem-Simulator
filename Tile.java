package lifesim;

import java.awt.Color;
import asciiPanel.AsciiPanel;

public enum Tile {
	//Tiles
	GRASS((char)32, AsciiPanel.black, AsciiPanel.green),
	WATER((char)176, AsciiPanel.black, AsciiPanel.brightBlue),
	PLANT((char)157, AsciiPanel.brightGreen, AsciiPanel.green),
	BOUNDS('X', AsciiPanel.brightBlack, AsciiPanel.black);
	
	//Variables
	private char glyph;
	private Color fontColor;
	private Color backColor;
	
	private boolean isFlagged;
	
	//Constructor
	Tile(char glyph, Color fontColor, Color backColor) {
		this.glyph = glyph;
		this.fontColor = fontColor;
		this.backColor = backColor;
		
		isFlagged = false;
	}
	
	//Methods
	public char getGlyph() {
		return glyph;
	}
	
	public Color getFontColor() {
		return fontColor;
	}
	
	public Color getBackColor() {
		return backColor;
	}
	
	public boolean isGrass() {
		return this == Tile.GRASS;
	}
	
	public boolean isPlant() {
		return this == Tile.PLANT;
	}
	
	public boolean isWater() {
		return this == Tile.WATER;
	}
	
	public void flag() {
		isFlagged = true;
	}
	
	public void unFlag() {
		isFlagged = false;
	}
	
	public boolean isFlagged() {
		return isFlagged;
	}
}
