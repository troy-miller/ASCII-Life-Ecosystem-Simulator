package lifesim.entities;

import lifesim.Tile;
import lifesim.World;
import lifesim.entities.priorities.Priority;

public class EntityBehavior {
	//------------------
	//Instance Variables
	//------------------
	protected Entity entity;
	
	//------------------
	//Constructor
	//------------------
	public EntityBehavior(Entity entity) {
		this.entity = entity;
		entity.setBehavior(this);
	}
	
	//------------------
	//Class Methods
	//------------------
	public void onEnter(int wx, int wy, Tile tile) {
		//Do nothing
	}
	
	public void onUpdate() {
		//Do nothing
	}
	
	public void setWorld(World world) {
		//Do nothing
	}
	
	public void setPriority(Priority newPriority) {
		//Do nothing
	}
	
	public int[] pathfind(int wx, int wy) {
		return null;
	}
	
	public int[] aStarPathfind(int goalX, int goalY) {
		return null;
	}
	
	public int getThirstCap() {
		return -1;
	}
	
	public int getHungerCap() {
		return -1;
	}
	
	public int getAgeCap() {
		return -1;
	}
	
	public int getReproductionCap() {
		return -1;
	}
	
	public int getAdultAge() {
		return -1;
	}
	
	public boolean isBunny() {
		return false;
	}
	
	public boolean isFox() {
		return false;
	}
	
	public Priority getPriority() {
		return null;
	}
}
