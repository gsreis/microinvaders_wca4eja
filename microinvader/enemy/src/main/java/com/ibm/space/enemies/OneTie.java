package com.ibm.space.enemies;

import java.io.Serializable;

//One invader
@SuppressWarnings("serial")
class OneTie implements Serializable
{
	public OneTie(int id) { this.id = id; } 

	private int id; // 0-39 - 0-9 first line 10-19 second line - 20-29 third line - 30-39 fouth line
	public int getId() { return id; }
	
	private int x;
	public int getX() { return x; }
	public void setX(int x) { this.x = x; }
	
	private int y;	
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
	
	private boolean destroyed = false;
	public boolean isDestroyed() { return destroyed; }
	public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}
