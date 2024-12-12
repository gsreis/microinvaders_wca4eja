package com.ibm.space.player;

import java.io.Serializable;

@SuppressWarnings("serial")
class PlayerValues implements Serializable
{
	private int x = 0; // from 0 to 19
	public int getX() { return x; }
	public void setX(int x) { this.x = x; }


	private int y = 19; // fixed 19
	public int getY() { return y; }

	private boolean destroyed = false;
	public boolean isDestroyed() { return destroyed; }
	public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
	
	private byte[] image = null;
	public byte[] getImage() { return image; }
	public void setImage(byte[] image) { this.image = image; }
	
	private byte[] blank = null;
	public byte[] getBlank() { return blank; }
	public void setBlank(byte[] blank) { this.blank = blank; }
}
