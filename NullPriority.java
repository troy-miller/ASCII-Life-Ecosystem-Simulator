package lifesim.entities.priorities;

import lifesim.World;
import lifesim.entities.Entity;

public class NullPriority implements Priority {

	@Override
	public int[] lookForPriority(int ex, int ey, World world) {
		return null;
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
