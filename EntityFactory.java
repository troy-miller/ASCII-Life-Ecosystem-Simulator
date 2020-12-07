package lifesim.entities;

import asciiPanel.AsciiPanel;
import lifesim.World;

public class EntityFactory {
	
	/**
	 * By using the factory design pattern, we have a different object
	 * that handles creating our entities, rather than including it in
	 * our world or entity classes
	 * 
	 */
	
	
	//------------------
	//Instance Variables
	//------------------
	private World world;
	
	//------------------
	//Constructor
	//------------------
	public EntityFactory(World world) {
		this.world = world;
	}
	
	//------------------
	//Class Methods
	//------------------
	public Entity newBunny() {
		Entity bunny = new Entity(world, 'b', AsciiPanel.brightWhite, AsciiPanel.green);
		world.addAtEmptyLocation(bunny);
		bunny.setFactory(this);
		new BunnyBehavior(bunny);
		return bunny;
	}
	
	public Entity newBabyBunny(int motherXPos, int motherYPos) {
		//Make sure there is empty space for a baby
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 || y != 0) {
					if (world.getTile(motherXPos + x, motherYPos + y).isGrass() &&
							world.entityAt(motherXPos + x, motherYPos + y) == null) {
						break;
					}
					else {
						return null;
					}
				}
			}
		}
		Entity bunny = new Entity(world, 'b', AsciiPanel.brightWhite, AsciiPanel.green);
		world.addAtEmptyLocation(motherXPos - 1, motherXPos + 1, 
				motherYPos - 1, motherYPos + 1, bunny);
		bunny.setFactory(this);
		new BunnyBehavior(bunny);
		return bunny;
	}
}
