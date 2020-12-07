package lifesim.entities.priorities;

import lifesim.World;
import lifesim.entities.Entity;

public interface Priority {
	/**
	 * Serves as strategy design pattern:
	 * 
	 * - Takes in coordinates of the entity, and the
	 *   world it's in, finding the tiles that it can 
	 *   "see"
	 *   
	 * - Returns the coordinates of nearest tile that 
	 *   has been found that meets priority
	 */
	
	//Abstract methods
	public int[] lookForPriority(int ex, int ey, World world);
	
	public boolean isThirst();
	
	public boolean isHunger();
	
	public boolean isReproduction();
	
	public Entity getEntity();
	
	public void setEntity(Entity entity);
	
	public boolean isMale();
}
