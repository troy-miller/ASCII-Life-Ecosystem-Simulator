package lifesim.entities;

import java.util.Random;
import java.util.ArrayList;

import lifesim.Node;
import lifesim.Tile;
import lifesim.World;
import lifesim.entities.priorities.*;

public class BunnyBehavior extends EntityBehavior {
	//------------------
	//Instance Variables
	//------------------
	private Random rand;
	private World world;
	private Priority priority;
	
	private final int VIEW_RANGE = 15;
	
	private final int THIRST_CAPACITY = 200;
	private final int HUNGER_CAPACITY = 200;
	private final int REPRODUCTION_CAPACITY = 100;
	
	private int ageCapacity;
	private int adultAge;
	
	//------------------
	//Constructor
	//------------------
	public BunnyBehavior(Entity entity) {
		super(entity);
		entity.setBehavior(this);
		
		setPriority(new HungerPriority());
		
		rand = new Random();
		
		ageCapacity = rand.nextInt(800) + 1600;
		adultAge = rand.nextInt(500) + 750;
	}
	
	//------------------
	//Class Methods
	//------------------
	public void setWorld(World world) {
		this.world = world;
	}
	
	@Override
	public boolean isBunny() {
		return true;
	}
	
	@Override
	public boolean isFox() {
		return false;
	}
	
	@Override
	public int getThirstCap() {
		return THIRST_CAPACITY;
	}
	
	@Override
	public int getHungerCap() {
		return HUNGER_CAPACITY;
	}
	
	@Override
	public int getAgeCap() {
		return ageCapacity;
	}
	
	@Override
	public int getReproductionCap() {
		return REPRODUCTION_CAPACITY;
	}
	
	@Override
	public int getAdultAge() {
		return adultAge;
	}
	
	@Override
	public Priority getPriority() {
		return priority;
	}
	
	@Override
	public void onEnter(int wx, int wy, Tile tile) {
		if (tile.isGrass() && world.entityAt(wx, wy) == null) {
			entity.setX(wx);
			entity.setY(wy);
		}
	}

	@Override
	public void onUpdate() {
		if (entity.getThirst() >= entity.getHunger() &&
				entity.getThirst() >= entity.getReproductionUrge()) {
			setPriority(new ThirstPriority());
		}
		else if (entity.getHunger() >= entity.getThirst() &&
				entity.getHunger() >= entity.getReproductionUrge()){
			setPriority(new HungerPriority());
		}
		else {
			setPriority(new ReproductionPriority());
			priority.setEntity(entity);
		}
		
		int[] priorityTile = priority.lookForPriority(entity.getX(), entity.getY(), world);
		if (priorityTile != null) {
			int[] moveDirection = aStarPathfind(priorityTile[0], priorityTile[1]);
			if (moveDirection == null) {
				//Move randomly
				int mx = rand.nextInt(3) - 1;
				int my = rand.nextInt(3) - 1;
				
				entity.moveBy(mx, my);
			}
			else {
				if (priority.isHunger()) {
					if (world.getTile(entity.getX() + moveDirection[0], 
							entity.getY() + moveDirection[1]) == Tile.PLANT) {
						entity.eat(moveDirection[0], moveDirection[1]);
					}
					else {
						entity.moveBy(moveDirection[0], moveDirection[1]);
					}
				}
				else if (priority.isThirst()) {
					if (world.getTile(entity.getX() + moveDirection[0], 
							entity.getY() + moveDirection[1]) == Tile.WATER) {
						entity.drink();
					}
					else {
						entity.moveBy(moveDirection[0], moveDirection[1]);
					}
				}
				else {
					if (world.entityAt(entity.getX() + moveDirection[0], 
							entity.getY() + moveDirection[1]) != null &&
							world.entityAt(entity.getX() + moveDirection[0], 
							entity.getY() + moveDirection[1]).isAdult()) {
						
						if (entity.isMale()) {
							if (!world.entityAt(entity.getX() + moveDirection[0],
									entity.getY() + moveDirection[1]).isMale()) {
								
								entity.reproduce();
							}
							else {
								entity.moveBy(moveDirection[0], moveDirection[1]);
							}
						}
						
						else {
							if (world.entityAt(entity.getX() + moveDirection[0],
									entity.getY() + moveDirection[1]).isMale()) {
								
								entity.reproduce();
								world.entityAt(entity.getX() + moveDirection[0], 
										entity.getY() + moveDirection[1]).reproduce();
							}
							else {
								entity.moveBy(moveDirection[0], moveDirection[1]);
							}
						}
					}
					else {
						entity.moveBy(moveDirection[0], moveDirection[1]);
					}
				}
			}
		}
		
		else {
			//Move randomly
			int mx = rand.nextInt(3) - 1;
			int my = rand.nextInt(3) - 1;
			
			entity.moveBy(mx, my);
		}
	}
	
	@Override
	public void setPriority(Priority newPriority) {
		this.priority = newPriority;
	}
	
	//My rudimental path finding algorithm
	@Override
	public int[] pathfind(int wx, int wy) {
		int[] movementDirection = new int[2];
		
		//Get x direction movement
		if (wx == entity.getX()) {
			movementDirection[0] = 0;
		}
		else if (wx > entity.getX()) {
			movementDirection[0] = 1;
		}
		else {
			movementDirection[0] = -1;
		}
		
		//Get y direction movement
		if (wy == entity.getY()) {
			movementDirection[1] = 0;
		}
		else if (wy > entity.getY()) {
			movementDirection[1] = 1;
		}
		else {
			movementDirection[1] = -1;
		}
		
		//Handle obstacles
		int plannedX = entity.getX() + movementDirection[0];
		int plannedY = entity.getY() + movementDirection[1];
		
		if (priority.isThirst()) {
			if (world.getTile(plannedX, plannedY).isPlant() || 
					world.entityAt(plannedX, plannedY) != null) {
				return null;
			}
			else {
				return movementDirection;
			}
		}
		
		else {
			if (world.getTile(plannedX, plannedY).isWater() ||
					world.entityAt(plannedX, plannedY) != null) {
				return null;
			}
			else {
				return movementDirection;
			}
		}
	}
	
	//Optimized A* path finding algorithm
	@Override
	public int[] aStarPathfind(int goalX, int goalY) {
		Node startNode = new Node(entity.getX(), entity.getY(), null);
		Node goalNode = new Node(goalX, goalY, null);
		
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		
		openList.add(startNode);

		while (!openList.isEmpty()) {
			
			//Check here for if openList is spiraling out of control because apparently
			//that happens and I don't know how to fix it
			
			if (openList.size() > 100) {
				break;
			}
			
			Node currentNode = openList.get(0);
			
			//world.getTile(currentNode.getX(), currentNode.getY()).flag();
			
			for (Node node : openList) {
				if (node.getF() < currentNode.getF()) {
					currentNode = node;
				}
			}
			
			openList.remove(currentNode);
			closedList.add(currentNode);
			
			if (currentNode.getX() == goalNode.getX() &&
					currentNode.getY() == goalNode.getY()) {
				
				Node nextPos = currentNode;
				while (nextPos.getParent() != null && nextPos.getParent() != startNode) {
					//world.getTile(nextPos.getX(), nextPos.getY()).flag();
					nextPos = nextPos.getParent();
				}
				
				int[] movementDirection = new int[] {
					nextPos.getX() - entity.getX(),
					nextPos.getY() - entity.getY()
				};
				
				return movementDirection;
			}
			
			
			ArrayList<Node> children = new ArrayList<Node>();
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (x != 0 || y != 0) {
						if (!(currentNode.getX() + x > entity.getX() + VIEW_RANGE ||
								currentNode.getY() + y > entity.getY() + VIEW_RANGE ||
								currentNode.getX() + x < entity.getX() - VIEW_RANGE ||
								currentNode.getY() + y < entity.getY() - VIEW_RANGE ||
								
								currentNode.getX() + x > world.getWidth() ||
								currentNode.getY() + y > world.getHeight() ||
								currentNode.getX() + x < 0 ||
								currentNode.getY() + y < 0)) {
							
							if (!(goalNode.getX() == currentNode.getX() + x && 
									goalNode.getY() == currentNode.getY() + y)) {
								if (world.getTile(currentNode.getX() + x, currentNode.getY() + y).isGrass() &&
										world.entityAt(currentNode.getX() + x, currentNode.getY() + y) == null) {
									children.add(new Node(currentNode.getX() + x, currentNode.getY() + y,
											currentNode));
								}
							}
							else {
								children.add(new Node(currentNode.getX() + x, currentNode.getY() + y, 
										currentNode));
							}
						}
					}
				}
			}
			
			
			process: for (Node child : children) {
				for (Node closedNode : closedList) {
					if (child.getX() == closedNode.getX() && 
							child.getY() == closedNode.getY()) {
						continue process;
					}
				}
				
				child.setG(currentNode.getG() + 1);
				//child.setH(Math.max(Math.abs(child.getX() - goalNode.getX()), 
				//		Math.abs(child.getY() - goalNode.getY())));
				child.setH((int)(Math.pow(child.getX() - goalNode.getX(), 2) + 
						Math.pow(child.getY() - goalNode.getY(), 2)));
				child.setF(child.getG() + child.getH());
				
				for (Node openNode : openList) {
					if (child.getX() == openNode.getX() &&
							child.getY() == openNode.getY() &&
							child.getG() > openNode.getG()) {
						continue process;
					}
				}
				
				openList.add(child);
			}
		}
		
		return null;
	}
}
