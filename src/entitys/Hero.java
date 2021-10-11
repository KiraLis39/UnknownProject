package entitys;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Hero extends Creature {
	
	public Hero(BufferedImage image, String name, int health, int energy) {				
		setID(0);
		setName(name);
		
		setImage(image);
		setHP(health);
		setEnergy(energy);
	}
	
	@Override
	public void draw(Graphics2D g2D, int x, int y) {		
//		g2D.setColor(Color.GREEN);
//		g2D.drawRect((int) (x - halfDim), (int) (y - halfDim), crDim, crDim);
		
		g2D.drawImage(getImage(), x - 40, y - 32, 80, 64, null);
	}

	public void takeDamage(int damage) {setHP(getHP() - damage);}
}