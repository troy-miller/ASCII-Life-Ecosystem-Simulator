package lifesim;

public class WorldBuilder {
	//------------------
	//Instance Variables
	//------------------
	private Tile[][] tiles;
	private int width;
	private int height;
	
	//------------------
	//Constructor
	//------------------
	public WorldBuilder(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
	}
	
	//------------------
	//Class Methods
	//------------------
	public World build() {
		return new World(tiles);
	}
	
	public WorldBuilder randomizeTiles() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = Math.random() <= 0.58 ? Tile.GRASS : Tile.WATER;
			}
		}
		return this;
	}
	
	public WorldBuilder smooth(int times) {
		Tile[][] tiles2 = new Tile[width][height];
		
		for (int time = 0; time < times; time++) {
			//Cycle through all tiles
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int grass = 0;
					int water = 0;
					//Check neighboring tiles
					for (int nx = -1; nx < 2; nx++) {
						for (int ny = -1; ny < 2; ny++) {
							//Skip iteration if neighbor is out of bounds
							if ((x + nx) < 0 || (x + nx) >= width || (y + ny) < 0
									|| (y + ny) >= height)
								continue;
							
							if (tiles[x + nx][y + ny] == Tile.GRASS) {
								grass++;
							}
							else {
								water++;
							}
						}
					}
					tiles2[x][y] = grass >= water ? Tile.GRASS : Tile.WATER;
				}
			}
			tiles = tiles2;
		}
		return this;
	}
	
	public WorldBuilder makePonds() {
		return this.randomizeTiles().smooth(8);
	}
}
