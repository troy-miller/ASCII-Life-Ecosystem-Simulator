package lifesim.entities.priorities;

import java.util.ArrayList;

import lifesim.World;
import lifesim.entities.Entity;

public class HungerPriority implements Priority {
	//Constant
	private final int VIEW_RANGE = 15;
	
	//Inherited method
	@Override
	public int[] lookForPriority(int ex, int ey, World world) {
		ArrayList<int[]> foodDistances = new ArrayList<int[]>();
		
		//Find all water tiles in sight
		for (int nx = VIEW_RANGE * -1; nx < VIEW_RANGE + 1; nx++) {
			for (int ny = VIEW_RANGE * -1; ny < VIEW_RANGE + 1; ny++) {
				if (world.getTile(ex + nx, ey + ny).isPlant()) {
					int[] foodCoordPair = {
							nx,
							ny
					};
					foodDistances.add(foodCoordPair);
				}
			}
		}
		
		if (foodDistances.isEmpty()) {
			return null;
		}
		
		//Find nearest of visible water tiles
		else {
			int[] nearestFood;
			
			nearestFood = foodDistances.get(0);
			for (int i = 1; i < foodDistances.size(); i++) {
				//if (Math.abs(foodDistances.get(i)[0]) <= Math.abs(nearestFood[0]) && 
				//		Math.abs(foodDistances.get(i)[1]) <= Math.abs(nearestFood[1])) {
				//	nearestFood = foodDistances.get(i);
				//}
				
				if (Math.sqrt(Math.pow(foodDistances.get(i)[0], 2) + 
						Math.pow(foodDistances.get(i)[1], 2))
							<= 
					Math.sqrt(Math.pow(nearestFood[0], 2) + 
						Math.pow(nearestFood[1], 2))) {
					nearestFood = foodDistances.get(i);
				}
			}
			
			//Update nearest water so that it shows the world coordinates of the tile
			nearestFood[0] = nearestFood[0] + ex;
			nearestFood[1] = nearestFood[1] + ey;
			
			return nearestFood;
		}
	}
	
	
	@Override
	public boolean isThirst() {
		return false;
	}
	
	@Override
	public boolean isHunger() {
		return true;
	}
	
	@Override
	public boolean isReproduction() {
		return false;
	}
	
	@Override
	public Entity getEntity() {
		return null;
	}
	
	@Override
	public void setEntity(Entity entity) {
		//Do nothing
	}
	
	@Override
	public boolean isMale() {
		return false;
	}
}
