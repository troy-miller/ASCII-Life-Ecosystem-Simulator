package lifesim.entities;

import java.awt.Color;

import lifesim.World;

public class Entity {
	//------------------
	//Instance Variables
	//------------------
	private World world;
	
	private int x;
	private int y;
	
	private char glyph;
	private Color entityColor;
	private Color backColor;
	
	private int age;
	private float thirst;
	private float hunger;
	private int reproductionUrge;
	
	private boolean isAdult;
	private boolean isMale;
	
	private EntityFactory factory;
	
	private EntityBehavior behavior;
	
	//------------------
	//Constructor
	//------------------
	public Entity(World world, char glyph, Color entityColor, Color backColor) {
		this.world = world;
		this.glyph = glyph;
		this.entityColor = entityColor;
		this.backColor = backColor;
		
		age = 0;
		thirst = 0;
		hunger = 0;
		reproductionUrge = 0;
		
		isAdult = false;
		isMale = Math.random() < .5 ? true : false;
	}
	
	//------------------
	//Class Methods
	//------------------
	public void moveBy(int mx, int my) {
		behavior.onEnter(x + mx, y + my, world.getTile(x + mx, y + my));
	}
	
	public void update() {
		behavior.onUpdate();
		age ++;
		thirst += 1.5;
		hunger ++;
		if (isAdult) {
			if (reproductionUrge < behavior.getReproductionCap()) {
				reproductionUrge += 1.5;
			}
		}
		
		if (age >= behavior.getAgeCap()) {
			world.removeEntity(this);
			world.addAgeDeath();
		}
		else if (thirst >= behavior.getThirstCap()) {
			world.removeEntity(this);
			world.addThirstDeath();
		}
		else if (hunger >= behavior.getHungerCap()) {
			world.removeEntity(this);
			world.addFoodDeath();
		}
		else if (age >= behavior.getAdultAge() && !isAdult) {
			makeAdult();
		}
	}
	
	public boolean canEnter(int mx, int my) {
		return world.getTile(x + mx, y + my).isGrass();
	}
	
	public void eat(int mx, int my) {
		world.eat(x + mx, y + my);
		hunger -= 30;
		if (hunger < 0) {
			hunger = 0;
		}
	}
	
	public void drink() {
		thirst -= 30;
		if (thirst < 0) {
			thirst = 0;
		}
	}
	
	public void reproduce() {
		reproductionUrge = 0;
		
		if (!isMale()) {
			factory.newBabyBunny(x, y);
		}
	}
	
	//Getters and setters
	public char getGlyph() {
		return glyph;
	}
	
	public Color getEntityColor() {
		return entityColor;
	}
	
	public Color getBackColor() {
		return backColor;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setBehavior(EntityBehavior behavior) {
		this.behavior = behavior;
		behavior.setWorld(world);
	}
	
	public float getThirst() {
		return thirst;
	}
	
	public float getHunger() {
		return hunger;
	}
	
	public int getReproductionUrge() {
		return reproductionUrge;
	}
	
	public int getAge() {
		return age;
	}
	
	public EntityBehavior getBehavior() {
		return behavior;
	}
	
	
	public boolean isAdult() {
		return isAdult;
	}
	
	public void makeAdult() {
		isAdult = true;
		if (behavior.isBunny()) {
			glyph = 'B';
		}
	}
	
	public boolean isMale() {
		return isMale;
	}
	
	public EntityFactory getFactory() {
		return factory;
	}
	
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
}
