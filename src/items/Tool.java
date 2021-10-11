package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entitys.proto.iGameObjects;


public abstract class Tool implements iGameObjects {
	public int ID, hash;
	public String name;
	public BufferedImage image;
	
	
	public void draw(Graphics2D g2D, int x, int y) {
		g2D.drawImage(getImage(), x - getImage().getWidth() / 2, y - getImage().getHeight() / 2, null);
	}
	
	public int getID() {return ID;}
	public void setID(int iD) {ID = iD;}
	
	public String getName() {return name;}
	public void setName(String _name) {
		name = _name;
		hash = getID() + (int) System.currentTimeMillis() + getName().toCharArray().length;
	}
	
	public int getHash() {return hash;}
	public void setHash(int hash) {this.hash = hash;}
	
	public BufferedImage getImage() {return image;}
	public void setImage(BufferedImage image) {this.image = image;}
}
