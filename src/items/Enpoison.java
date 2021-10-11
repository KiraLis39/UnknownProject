package items;

import java.awt.image.BufferedImage;

public class Enpoison extends Item {
	
	public Enpoison(BufferedImage iImage, String name, int hp) {
		setID(102);
		setName(name);
		
		setImage(iImage);
		setHP(hp);
	}
}
