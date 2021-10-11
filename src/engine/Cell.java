package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import entitys.Creature;
import entitys.Hero;
import entitys.iGameObjects;
import items.Item;
import subgui.BattleField;


public class Cell {
	private Map<Integer, Creature> cellCreaturesMap = new HashMap<Integer, Creature>();
	private Map<Integer, iGameObjects> cellItemsMap = new HashMap<Integer, iGameObjects>();
	private final Rectangle cellRect;
	private final int index;
	private int colIndex;
	private int rowIndex;
	
	public Cell(int index, Rectangle rect, int colIndex, int rowIndex) {
		this.index = index;
		this.colIndex = colIndex;
		this.rowIndex = rowIndex;
		cellRect = rect;
	}

	public void draw(Graphics2D g2D) {
		g2D.setColor(new Color(0.0f, 0.0f, 0.0f, 0.8f));
		g2D.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
		
		g2D.setColor(Color.GRAY);
		g2D.drawRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
		
		if (!cellCreaturesMap.isEmpty()) {
			int creaturePlaceIndex = 1;
			
			for (Entry<Integer, Creature> creatureEntry : cellCreaturesMap.entrySet()) {
				if (checkAlive(creatureEntry.getValue())) {
					
					if (creatureEntry.getValue() instanceof Hero) {creatureEntry.getValue().draw(g2D, cellRect.x + cellRect.width / 2, cellRect.y + cellRect.height / 2);
					} else {
						
						switch (creaturePlaceIndex) {
							case 1:		creatureEntry.getValue().draw(g2D, cellRect.x + cellRect.width / 3, cellRect.y + cellRect.height / 3);
								break;
							case 2:		creatureEntry.getValue().draw(g2D, cellRect.x + (int) (cellRect.getWidth() / 3D * 2D), cellRect.y + cellRect.height / 3);
								break;
							case 3:		creatureEntry.getValue().draw(g2D, cellRect.x + cellRect.width / 3, cellRect.y + (int) (cellRect.getHeight() / 3D * 2D));
								break;
							default:   creatureEntry.getValue().draw(g2D, cellRect.x + (int) (cellRect.getWidth() / 3D * 2D), cellRect.y + (int) (cellRect.getHeight() / 3D * 2D));
						}
						
						creaturePlaceIndex++;
					}					
				}				
			}
		}
		
		if (!cellItemsMap.isEmpty()) {
			for (Entry<Integer, iGameObjects> itemEntry : cellItemsMap.entrySet()) {
				itemEntry.getValue().draw(g2D, cellRect.x + cellRect.width / 2, 	cellRect.y + cellRect.height / 2);
			}
		}
	}

	private boolean checkAlive(Creature c) {
		if (c.isAlive()) {return true;}
		
		if (c instanceof Hero) {BattleField.setGameOver(true);}
		cellCreaturesMap.remove(c.getHash());
		return false;
	}

	public boolean contains(int creaturesHash) {
		if (cellCreaturesMap.isEmpty()) {return false;}
		
		for (Entry<Integer, Creature> element : cellCreaturesMap.entrySet()) {
//			System.out.println("Сравниваем очередной " + element.getKey() + " с искомым " + creaturesHash + " (cell index: " + getIndex() + ")");
			if (element.getKey() == creaturesHash) {return true;}
		}
		
		return false;
	}

	public void addCreature(Creature c) {
		if (c == null) {return;}
		cellCreaturesMap.put(c.getHash(), c);
	}
	
	public void addItem(iGameObjects item) {
		if (item == null) {return;}
		cellItemsMap.put(item.getHash(), item);
	}
	
	public Creature removeCreature(Creature c) {
		if (cellCreaturesMap.containsValue(c)) {
			Creature result = c;
			cellCreaturesMap.remove(c.getHash());
			return result;
		}
		return null;
	}
	
	public Item removeItem(Item item) {
		if (cellItemsMap.containsValue(item)) {
			Item result = item;
			cellItemsMap.remove(item.getHash());
			return result;
		}
		return null;
	}

	public int getIndex() {return index;}
	
	@Override
	public String toString() {return "Cell #" + index + ", rect: " + cellRect;}

	public int getColumnIndex() {return colIndex;}
	public int getRowIndex() {return rowIndex;}

	public ArrayList<iGameObjects> getContent() {
		ArrayList<iGameObjects> result = new ArrayList<iGameObjects>();
		
		for (Entry<Integer, Creature> creatureEntry : cellCreaturesMap.entrySet()) {
			result.add(creatureEntry.getValue());
		}
		
		for (Entry<Integer, iGameObjects> itemEntry : cellItemsMap.entrySet()) {
			result.add(itemEntry.getValue());
		}
		
		return result;
	}

	public void clear() {
		cellCreaturesMap.clear();
		cellItemsMap.clear();
	}
}