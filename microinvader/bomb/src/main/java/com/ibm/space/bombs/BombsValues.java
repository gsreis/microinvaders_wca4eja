package com.ibm.space.bombs;

import java.io.Serializable;
import java.util.Vector;

//Collection of bombs 
@SuppressWarnings("serial")
class BombsValues implements Serializable
{
	private Vector<OneBomb> bombs;
	public Vector<OneBomb> getBombs() { return bombs; }	
	public void setBombs(Vector<OneBomb> bombs) { this.bombs = bombs; }
	
	public BombsValues() { bombs = new Vector<OneBomb>(); }
	
	private byte[] imageup = null;
	public byte[] getImageUp() { return imageup; }
	public void setImageUp(byte[] image) { this.imageup = image; }

	private byte[] imagedown = null;
	public byte[] getImageDown() { return imagedown; }
	public void setImageDown(byte[] image) { this.imagedown = image; }
	
	private byte[] blank = null;
	public byte[] getBlank() { return blank; }
	public void setBlank(byte[] blank) { this.blank = blank; }

	
	private boolean hasFinished = false;
	public boolean isHasFinished() { return hasFinished; }
	public void setHasFinished(boolean hasFinished) { this.hasFinished = hasFinished; }

}

