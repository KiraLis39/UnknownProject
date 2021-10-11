package entitys;

import entitys.proto.Creature;

import java.awt.image.BufferedImage;

public class Goblin extends Creature {
	
	public Goblin(BufferedImage image, String name, int health, int energy) {
		setID(1);
		setName(name);
		setImage(image);
		
		setHP(health);
		setEnergy(energy);
		setStrength(10);
	}
}
