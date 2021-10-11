package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Finish extends Item {	
	int imageWidth, imageHeight;
	
	
	public Finish(BufferedImage iImage, String name) {
		setID(199);
		setName(name);
		
		setImage(iImage);
	}
	
	@Override
	public void draw(Graphics2D g2D, int x, int y) {
		// set images dim >= 32 pix:
		imageWidth = getImage().getWidth() < 32 ? 32 : getImage().getWidth();
		imageHeight = getImage().getHeight() < 32 ? 32 : getImage().getHeight();
		
		g2D.drawImage(getImage(), x - imageWidth / 2, y - imageHeight / 2, imageWidth, imageHeight, null);
	}
}