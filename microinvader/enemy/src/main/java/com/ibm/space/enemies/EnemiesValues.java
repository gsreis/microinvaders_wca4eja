package com.ibm.space.enemies;

import java.io.Serializable;


//Collection of invaders 
@SuppressWarnings("serial")
class EnemiesValues implements Serializable
{
	private OneTie[] ties= new OneTie[40];
	public OneTie[] getTies() { return ties; }
	public void setTies(OneTie[] ties) { this.ties = ties; }
	public EnemiesValues()
	{
		for (int i = 0; i < ties.length; i++) {
			ties[i] = new OneTie(i);
			int tempx = i % 10; // x position
			int tempy = i / 10; // y position
			ties[i].setX(tempx + 5); // start from the middle of the grid
			ties[i].setY(tempy);
		}
	}
	private byte[] image = null;
	public byte[] getImage() { return image; }
	public void setImage(byte[] image) { this.image = image; }

	private byte[] blank = null;
	public byte[] getBlank() { return blank; }
	public void setBlank(byte[] blank) { this.blank = blank; }
	
	public boolean isFinished() { 
		for (int i = 0; i < ties.length; i++) {
			if (!ties[i].isDestroyed())
				return false;
		} 
		return true;
	}
}
