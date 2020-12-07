package lifesim.entities.priorities;

import lifesim.World;
import lifesim.entities.Entity;

import java.util.ArrayList;

public class ThirstPriority implements Priority {
	//Constant
	private final int VIEW_RANGE = 15;
	
	//Inherited method
	@Override
	public int[] lookForPriority(int ex, int ey, World world) {
		ArrayList<int[]> waterDistances = new ArrayList<int[]>();
		
		//Find all water tiles in sight
		for (int nx = VIEW_RANGE * -1; nx < VIEW_RANGE + 1; nx++) {
			for (int ny = VIEW_RANGE * -1; ny < VIEW_RANGE + 1; ny++) {
				if (world.getTile(ex + nx, ey + ny).isWater()) {
					int[] waterCoordPair = {
							nx,
							ny
					};
					waterDistances.add(waterCoordPair);
				}
			}
		}
		
		if (waterDistances.isEmpty()) {
			return null;
		}
		
		//Find nearest of visible water tiles
		else {
			int[] nearestWater;
			
			nearestWater = waterDistances.get(0);
			for (int i = 1; i < waterDistances.size(); i++) {
				//if (Math.abs(waterDistances.get(i)[0]) <= Math.abs(nearestWater[0]) && 
				//		Math.abs(waterDistances.get(i)[1]) <= Math.abs(nearestWater[1])) {
				//	nearestWater = waterDistances.get(i);
				//}
				
				if (Math.sqrt(Math.pow(waterDistances.get(i)[0], 2) + 
						Math.pow(waterDistances.get(i)[1], 2))
							<= 
					Math.sqrt(Math.pow(nearestWater[0], 2) + 
						Math.pow(nearestWater[1], 2))) {
					nearestWater = waterDistances.get(i);
				}
			}
			
			//Update nearest water so that it shows the world coordinates of the tile
			nearestWater[0] = nearestWater[0] + ex;
			nearestWater[1] = nearestWater[1] + ey;
			
			return nearestWater;
		}
	}
	
	
	@Override
	public boolean isThirst() {
		return true;
	}
	
	@Override
	public boolean isHunger() {
		return false;
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
