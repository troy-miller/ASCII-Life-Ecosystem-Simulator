package lifesim;

public class Node {
	//------------------
	//Instance Variables
	//------------------
	private int x;
	private int y;
	
	private int g;
	private int h;
	private int f;
	
	private Node parent;
	
	//------------------
	//Constructor
	//------------------
	public Node(int x, int y, Node parent) {
		this.x = x;
		this.y = y;
		
		this.parent = parent;
		
		g = 0;
		h = 0;
		f = 0;
	}
	
	//------------------
	//Class Methods
	//------------------
	
	//Getters and setters
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public int getG() {
		return g;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	
	public int getH() {
		return h;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	public int getF() {
		return f;
	}
	
	public void setF(int f) {
		this.f = f;
	}
}
