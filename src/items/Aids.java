package items;

import java.awt.image.BufferedImage;


public class Aids extends Item {

	public Aids(BufferedImage iImage, String name, int hp) {
		setID(100);
		setName(name);
		
		setImage(iImage);
		setHP(hp);
	}
}
