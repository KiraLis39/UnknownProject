package entitys;

import java.awt.image.BufferedImage;

public class Troll extends Creature {
	
	public Troll(BufferedImage image, String name, int health, int energy) {
		setID(2);
		setName(name);
		setImage(image);
		
		setHP(health);
		setEnergy(energy);
		setStrength(20);
	}
}
