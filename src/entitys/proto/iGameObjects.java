package entitys.proto;

import java.awt.Graphics2D;

public interface iGameObjects {
	public int getHash();
	public void setHash(int hash);
	
	public void draw(Graphics2D g2D, int x, int y);
}
