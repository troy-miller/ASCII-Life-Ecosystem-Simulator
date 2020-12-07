package lifesim;

import java.awt.Color;
import java.util.ArrayList;

import lifesim.entities.Entity;

public class World {
	//------------------
	//Instance Variables
	//------------------
	private Tile[][] tiles;
	private int width;
	private int height;
	
	private float plantSpawnRate;
	
	private int ageDeaths;
	private int thirstDeaths;
	private int foodDeaths;
	
	private ArrayList<Entity> entities;
	private int selectedEntityIndex;
	
	//------------------
	//Constructor
	//------------------
	public World(Tile[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		
		ageDeaths = 0;
		thirstDeaths = 0;
		foodDeaths = 0;
		
		entities = new ArrayList<Entity>();
		selectedEntityIndex = 0;
	}
	
	//------------------
	//Class Methods
	//------------------
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return Tile.BOUNDS;
		}
		else {
			return tiles[x][y];
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public char getGlyph(int x, int y) {
		return getTile(x, y).getGlyph();
	}
	
	public Color getFontColor(int x, int y) {
		return getTile(x, y).getFontColor();
	}
	
	public Color getBackColor(int x, int y) {
		return getTile(x, y).getBackColor();
	}
	
	public Entity entityAt(int x, int y) {
		for (Entity e : entities) {
			if (e.getX() == x && e.getY() == y) {
				return e;
			}
		}
		return null;
	}
	
	public void addAtEmptyLocation(Entity entity) {
		int x = 0;
		int y = 0;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		}
		while (!getTile(x, y).isGrass() || entityAt(x, y) != null);
		
		entity.setX(x);
		entity.setY(y);
		
		entities.add(entity);
	}
	
	public void addAtEmptyLocation(Tile tile) {
		int x = 0;
		int y = 0;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		}
		while (!getTile(x, y).isGrass() || entityAt(x, y) != null);
		
		tiles[x][y] = tile;
	}
	
	public void addAtEmptyLocation(int xMin, int xMax, int yMin, int yMax, Entity entity) {
		int x = xMin;
		int y = yMin;
		
		do {
			x = (int)(Math.random() * (xMax - xMin)) + xMin;
			y = (int)(Math.random() * (yMax - yMin)) + yMin;
		}
		while (!getTile(x, y).isGrass() || entityAt(x, y) != null);
		
		entity.setX(x);
		entity.setY(y);
		
		entities.add(entity);
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public void removeEntity(Entity entity) {
		if (entities.indexOf(entity) == selectedEntityIndex) {
			selectedEntityIndex = 0;
		}
		if (selectedEntityIndex == entities.size() - 1) {
			selectedEntityIndex--;
		}
		entities.remove(entity);
	}
	
	public void update() {
		ArrayList<Entity> entitiesToUpdate = new ArrayList<Entity>(entities);
		
		for (Entity e : entitiesToUpdate) {
			e.update();
		}
		
		if (Math.random() < plantSpawnRate) {
			addAtEmptyLocation(Tile.PLANT);
		}
	}
	
	public void eat(int x, int y) {
		tiles[x][y] = Tile.GRASS;
	}
	
	public void setPlantSpawnRate(float plantSpawnRate) {
		this.plantSpawnRate = plantSpawnRate;
	}
	
	public int countFlags() {
		int flagCount = 0;
		
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[0].length; y++) {
				if (getTile(x, y).isFlagged()) {
					flagCount++;
				}
			}
		}
		
		return flagCount;
	}
	
	public int getAgeDeaths() {
		return ageDeaths;
	}
	
	public void addAgeDeath() {
		ageDeaths++;
	}
	
	public void resetAgeDeaths() {
		ageDeaths = 0;
	}
	
	public int getThirstDeaths() {
		return thirstDeaths;
	}
	
	public void addThirstDeath() {
		thirstDeaths++;
	}
	
	public void resetThirstDeaths() {
		thirstDeaths = 0;
	}
	
	public int getFoodDeaths() {
		return foodDeaths;
	}
	
	public void addFoodDeath() {
		foodDeaths++;
	}
	
	public void resetFoodDeaths() {
		foodDeaths = 0;
	}
	
	public boolean entitiesDead() {
		return entities.isEmpty();
	}
	
	public Entity getSelectedEntity() {
		if (!entities.isEmpty()) {
			return entities.get(selectedEntityIndex);
		}
		return null;
	}
	
	public void incrementSelectedEntity() {
		selectedEntityIndex = (selectedEntityIndex + 1) % entities.size();
	}
	
	public void decrementSelectedEntity() {
		selectedEntityIndex = 
				selectedEntityIndex == 0 ? entities.size() - 1 : selectedEntityIndex - 1;
	}
}
