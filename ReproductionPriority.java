package lifesim.entities.priorities;

import java.util.ArrayList;

import lifesim.World;
import lifesim.entities.Entity;

public class ReproductionPriority implements Priority {
	//Connected entity
	private Entity entity;
	
	//Constant
	private final int VIEW_RANGE = 15;
		
	//Inherited method
	@Override
	public int[] lookForPriority(int ex, int ey, World world) {
		ArrayList<int[]> bunnyDistances = new ArrayList<int[]>();
		
		//Find all water tiles in sight
		for (int nx = VIEW_RANGE * -1; nx < VIEW_RANGE + 1; nx++) {
			for (int ny = VIEW_RANGE * -1; ny < VIEW_RANGE + 1; ny++) {
				if (world.entityAt(ex + nx, ey + ny) != null &&
						world.entityAt(ex + nx, ey + ny).isAdult()) {
					
					//Make sure entity is only looking for its opposite gender
					if (entity.isMale()) {
						if (!world.entityAt(ex + nx, ey + ny).isMale()) {
							int[] bunnyCoordPair = {
									nx,
									ny
							};
							bunnyDistances.add(bunnyCoordPair);
						}
					}
					else {
						if (world.entityAt(ex + nx, ey + ny).isMale()) {
							int[] bunnyCoordPair = {
									nx,
									ny
							};
							bunnyDistances.add(bunnyCoordPair);
						}
					}
				}
			}
		}
		
		if (bunnyDistances.isEmpty()) {
			return null;
		}
		
		//Find nearest of visible water tiles
		else {
			int[] nearestBunny;
			
			nearestBunny = bunnyDistances.get(0);
			for (int i = 1; i < bunnyDistances.size(); i++) {
				//if (Math.abs(bunnyDistances.get(i)[0]) <= Math.abs(nearestBunny[0]) && 
				//		Math.abs(bunnyDistances.get(i)[1]) <= Math.abs(nearestBunny[1])) {
				//	nearestBunny = bunnyDistances.get(i);
				//}
				
				if (Math.sqrt(Math.pow(bunnyDistances.get(i)[0], 2) + 
						Math.pow(bunnyDistances.get(i)[1], 2))
							<= 
					Math.sqrt(Math.pow(nearestBunny[0], 2) + 
						Math.pow(nearestBunny[1], 2))) {
					nearestBunny = bunnyDistances.get(i);
				}
			}
			
			//Update nearest water so that it shows the world coordinates of the tile
			nearestBunny[0] = nearestBunny[0] + ex;
			nearestBunny[1] = nearestBunny[1] + ey;
			
			return nearestBunny;
		}
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public boolean isMale() {
		return false;
	}
	
	@Override
	public boolean isThirst() {
		return false;
	}
	
	@Override
	public boolean isHunger() {
		return false;
	}

	@Override
	public boolean isReproduction() {
		return true;
	}
}
