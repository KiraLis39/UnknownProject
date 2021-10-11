package entitys;

//import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public abstract class Creature implements iGameObjects {
	private int ID, hash, hp, energy, strength;
	private static String name;
	private BufferedImage image;

	private boolean isAlive = true;	
	
	
	public void draw(Graphics2D g2D, int x, int y) {
		if (image == null) {
			System.out.println("Creature image is NULL " + getName() + " (ID: " + getID() + ")");
			return;
		}

//		g2D.setColor(Color.GREEN);
//		g2D.drawRect((int) (x - halfDim), (int) (y - halfDim), crDim, crDim);
		g2D.drawImage(image, x - 16, y - 16, 32, 32, null);
	}
	
	public int getHP() {return hp;}
	public void setHP(int hp) {
		this.hp = hp;
		if (this.hp > 100) {this.hp = 100;}
		if (this.hp <= 0) {setAlive(false);}
	}
	
	public int getEnergy() {return energy;}
	public void setEnergy(int en) {energy = en;}
	
	@Override
	public int getHash() {return hash;}
	@Override
	public void setHash(int hash) {this.hash = hash;}
	
	public int getID() {return ID;}
	public void setID(int iD) {ID = iD;}
	
	public String getName() {return name;}
	public void setName(String _name) {
		name = _name;
		hash = getID() + (int) System.currentTimeMillis() + getName().toCharArray().length;
	}	
	
	public BufferedImage getImage() {return image;}
	public void setImage(BufferedImage _image) {image = _image;}
	
	public void setAlive(boolean alive) {isAlive = alive;}
	public boolean isAlive() {return isAlive;}

	public void setStrength(int _strength) {strength = _strength;}
	public int getStrength() {return strength;}
}