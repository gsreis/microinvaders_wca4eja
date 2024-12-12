package com.ibm.space.bombs;

import java.io.Serializable;

@SuppressWarnings("serial")
class OneBomb implements Serializable
{
	public OneBomb(int id, int x, int y, boolean fromPlayer) { this.fromPlayer = fromPlayer; this.x = x; this.y = y; this.id = id; } 

	private int id; 
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	private boolean fromPlayer = false; // true from luke, false from dart
	public boolean isFromPlayer() { return fromPlayer; }
	public void setFromPlayer(boolean fromPlayer) { this.fromPlayer = fromPlayer; }
	
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
