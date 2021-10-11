package items;

import java.awt.image.BufferedImage;


public class ToolExample extends Tool {

	public ToolExample(BufferedImage image, String name) {
		setID(300);
		setName(name);		
		setImage(image);
	}
}
