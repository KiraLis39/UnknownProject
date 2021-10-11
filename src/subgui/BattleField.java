package subgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import engine.Cell;
import entitys.proto.Creature;
import entitys.Goblin;
import entitys.Hero;
import entitys.Troll;
import entitys.proto.iGameObjects;
import fox.FoxFontBuilder;
import fox.InputAction;
import items.Aids;
import items.Enpoison;
import items.Finish;
import items.Item;
import items.Tool;
import items.ToolExample;

import static fox.FoxFontBuilder.FONT;


@SuppressWarnings("serial")
public class BattleField extends JPanel {
	public static enum Moving {RIGHT , LEFT, UP, DOWN, BACK}
	
	private InputAction inAc = new InputAction();
	private Font bigFont = FoxFontBuilder.setFoxFont(FONT.CAMBRIA, 36, true);
	private Font medFont = FoxFontBuilder.setFoxFont(FONT.CAMBRIA, 28, true);
	private Font defaultFont = FoxFontBuilder.setFoxFont(FONT.ARIAL_NARROW, 14, false);
	
	private BufferedImage backgroundImage;
	private BufferedImage[] levelsBackgrounds;
	private String[][] levelsMassive;
	
	private static List<Cell> cells = new LinkedList<Cell>();
	private static List<Tool> playerTools = new LinkedList<Tool>();
	private static Cell lastCellMemory;
	private static Hero player;	
	
	private static int ROW_LENGHT, COL_LENGHT; // 8 x 5
	private static int FPS = 0, DFPS = 0, CELL_DIM;
	private static int currentLevelIndex = 0;
	private static int stepEnergyDecrease = 2, fightEnergyDecrease = 20;
	private static long timeStamp;	
	
	private static boolean isLevelComplete, isGameOver;
	
	
	public BattleField(int rowLenght, int colLenght, int cellDim) {
		ROW_LENGHT = rowLenght;
		COL_LENGHT = colLenght;
		CELL_DIM = cellDim;

		int cellIndex = 0;
		for (int y = 0; y < ROW_LENGHT; y++) {
			for (int x = 0; x < COL_LENGHT; x++) {
				cells.add(new Cell(cellIndex, new Rectangle(CELL_DIM * x, CELL_DIM * y, CELL_DIM, CELL_DIM), x, y));
				cellIndex++;
			}
		}
		
		initLevelsData();		
		setupInAc();
	}

	private void initLevelsData() {
		levelsMassive = new String[][] {
			{ // level 0:
				" ", "G,TR", " ", "G", " ", "E", "S", "T:0",
				" ", "G", " ", " ", "G", " ", "G", " ",
				"G", " ", "S", " ", "T:0", " ", "G", " ",
				"G", " ", "G", " ", "S", " ", "G,TR", "G",
				"S", "G", " ", "G", " ", "E", " ", "F"
			},
			{ // level 1:
				" ", " ", " ", "T:0", " ", "G", "E", "S",
				"G", " ", " ", " ", "G", " ", "G", " ",
				" ", "G", " ", "G", " ", "G", " ", " ",
				"T:0", " ", "G", " ", "G", " ", " ", "G",
				"S", " ", "E", "G", " ", " ", "G", "F"
			},
			{ // level 2:
				" ", " ", " ", "G", " ", " ", " ", "S",
				" ", "G", " ", "G", " ", "G", " ", "E",
				" ", "G", " ", "G", " ", "G", " ", "E",
				" ", "G", " ", "G", " ", "G", " ", "E",
				"T:0", "G", " ", " ", " ", "G", " ", "F"
			},
			{ // level 3:
				" ", "S", "S", "S", "S", "S", "S", "S",
				"S", "S", "S", "S", "S", "S", "S", "S",
				"S", "S", "S", "S", "S", "S", "S", "S",
				"S", "S", "S", "S", "S", "S", "S", "S",
				"S", "S", "S", "S", "S", "S", "S", "F"
			},
			{ // level 4:
				" ", " ", " ", " ", " ", " ", "G", "T:0",
				" ", " ", " ", " ", "G", " ", " ", "G",
				" ", " ", " ", "G", "T", "G", " ", " ",
				"G", " ", " ", " ", "G", " ", " ", " ",
				"T:0", "G", " ", " ", " ", " ", " ", "F"
			},
			{ // level 5:
				" ", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0",
				"T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0",
				"T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0",
				"T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0",
				"T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "T:0", "F"
			},
			{ // level 6:
				" ", "G", " ", "S", "G", " ", " ", " ",
				"G", " ", "G", " ", "T:0", " ", " ", " ",
				" ", "G", " ", "S", "G", "E", " ", " ",
				"G", " ", "G", " ", "T:0", " ", " ", " ",
				" ", "G", " ", "S", "G", " ", " ", "F"
			},
			{ // level 7:
				" ", " ", " ", " ", " ", " ", " ", " ",
				" ", " ", " ", " ", "G", " ", " ", " ",
				" ", " ", " ", "G", "E", " ", " ", " ",
				"G", " ", " ", "G", " ", " ", " ", " ",
				"T:0", " ", " ", " ", " ", " ", "E", "F"
			},
			{ // level 8:
				" ", " ", " ", "G", " ", " ", " ", "S",
				" ", " ", "G", " ", " ", " ", "S", " ",
				" ", "G", " ", " ", " ", "S", " ", "G",
				"G", " ", " ", " ", "S", " ", "G", " ",
				" ", " ", " ", "S", " ", "G", " ", "F"
			},
			{ // level 9:
				" ", " ", " ", " ", " ", " ", " ", " ",
				" ", " ", " ", " ", " ", " ", " ", " ",
				" ", " ", " ", " ", " ", " ", " ", " ",
				" ", " ", " ", " ", " ", " ", " ", " ",
				" ", " ", " ", " ", " ", " ", " ", "F"
			}
		};
		
		String bgExt = ".jpg";
		
		try {
			levelsBackgrounds = new BufferedImage[] {
					// 10 back images:
					ImageIO.read(new File("resources/images/bg00" + bgExt)),
					ImageIO.read(new File("resources/images/bg01" + bgExt)),
					ImageIO.read(new File("resources/images/bg02" + bgExt)),
					ImageIO.read(new File("resources/images/bg03" + bgExt)),
					ImageIO.read(new File("resources/images/bg04" + bgExt)),
					ImageIO.read(new File("resources/images/bg05" + bgExt)),
					ImageIO.read(new File("resources/images/bg06" + bgExt)),
					ImageIO.read(new File("resources/images/bg07" + bgExt)),
					ImageIO.read(new File("resources/images/bg08" + bgExt)),
					ImageIO.read(new File("resources/images/bg09" + bgExt))
			};
		} catch (IOException e) {e.printStackTrace();}
		
		try {
			playerTools.add(0, new ToolExample(ImageIO.read(new File("./resources/images/tools/sSword.png")), "StoneSword"));
		} catch (IOException e) {e.printStackTrace();}
	}

	public void startLevel(int index) {
		currentLevelIndex = index;
		try{backgroundImage = levelsBackgrounds[index];
		} catch (Exception e) {/* IGNORE BG & DRAW GRAY BACK THAN */}
				
		int m = 0;
		for (Cell cell : cells) {
			cell.clear();
			
			String[] cellData = levelsMassive[index][m].split(",");			
			for (int i = 0; i < cellData.length; i++) {
				// creatures:
				try {cell.addCreature(cellData[i].equals("G") ? new Goblin(ImageIO.read(new File("./resources/images/goblin.png")), "Goblin", 30, 10) : null);
				} catch (IOException e) {e.printStackTrace();}
				
				try {cell.addCreature(cellData[i].equals("TR") ? new Troll(ImageIO.read(new File("./resources/images/troll.png")), "Troll", 10, 50) : null);
				} catch (IOException e) {e.printStackTrace();}
				
				// items;
				try {cell.addItem(cellData[i].equals("S") ? new Aids(ImageIO.read(new File("./resources/images/aids.png")), "Aid", 10) : null);
				} catch (IOException e) {e.printStackTrace();}
				
				try {cell.addItem(cellData[i].equals("F") ? new Finish(ImageIO.read(new File("./resources/images/finish.png")), "Finish") : null);
				} catch (IOException e) {e.printStackTrace();}
								
				try {cell.addItem(cellData[i].equals("E") ? new Enpoison(ImageIO.read(new File("./resources/images/energy.png")), "Enpoison", 10) : null);
				} catch (IOException e) {e.printStackTrace();}
				
				if (cellData[i].startsWith("T")) {
					String[] cellTools = cellData[i].split(":")[1].split(",");
					for (int j = 0; j < cellTools.length; j++) {cell.addItem(playerTools.get(Integer.parseInt(cellTools[j])));}
				}
			}
			
			m++;
		}
		
		if (player != null) {player.setAlive(false);}
		try {player = new Hero(ImageIO.read(new File("./resources/images/hero")), "Player", 100, 100);} catch (IOException e) {e.printStackTrace();}		
		addCreature(player);
	}
	
	public void addCreature(Creature c) {cells.get(0).addCreature(c);}
	
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		
		drawBackground(g2D);
		render(g2D);
		
		drawCells(g2D);
		drawGUI(g2D);
		drawFPS(g2D);
		
		g2D.dispose();
	}
	
	private void render(Graphics2D g2D) {
//		image.getScaledInstance(1024, 768, 2);
//		g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.0f));
		
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		g2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//		g2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		
//		g2D.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_BASE);
//		g2D.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
//		g2D.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_SIZE_FIT);
	}

	private void drawBackground(Graphics2D g2D) {
		if (backgroundImage == null) {
			g2D.setColor(Color.DARK_GRAY);
			g2D.fillRect(0, 0, getWidth(), getHeight());
		} else {g2D.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);}
	}

	private void drawCells(Graphics2D g2D) {
		for (Cell cell : cells) {cell.draw(g2D);}
	}

	private void drawGUI(Graphics2D g2D) {
		String lc;
		
		if (isLevelComplete) {
			lc ="LEVEL " + currentLevelIndex + " COMPLETE!";
			g2D.setFont(bigFont);
			g2D.setColor(Color.BLACK);
			g2D.drawString(lc, getWidth() / 2 - (int) (FoxFontBuilder.getStringBounds(g2D, lc).getWidth() / 2D) - 3, getHeight() / 2 + 3);
			g2D.setColor(Color.GREEN);
			g2D.drawString(lc, getWidth() / 2 - (int) (FoxFontBuilder.getStringBounds(g2D, lc).getWidth() / 2D), getHeight() / 2);
			
			lc = currentLevelIndex < levelsMassive.length - 1 ? "press ENTER to continue" : "Thanks for playing! =^_^=";
			g2D.setFont(medFont);
			g2D.setColor(Color.BLACK);
			g2D.drawString(lc, getWidth() / 2 - (int) (FoxFontBuilder.getStringBounds(g2D, lc).getWidth() / 2) - 3, getHeight() / 2 + 33);
			g2D.setColor(Color.GREEN);
			g2D.drawString(lc, getWidth() / 2 - (int) (FoxFontBuilder.getStringBounds(g2D, lc).getWidth() / 2), getHeight() / 2 + 30);
		}
		
		if (isGameOver) {
			lc = "ITS ALL YOUR FAIL...";
			g2D.setFont(bigFont);
			g2D.setColor(Color.BLACK);
			g2D.drawString(lc, getWidth() / 2 - (int) (FoxFontBuilder.getStringBounds(g2D, lc).getWidth() / 2) - 3, getHeight() / 2 + 3);
			g2D.setColor(Color.RED);
			g2D.drawString(lc, getWidth() / 2 - (int) (FoxFontBuilder.getStringBounds(g2D, lc).getWidth() / 2), getHeight() / 2);
		}
	}

	private void drawFPS(Graphics2D g2D) {
		if(System.currentTimeMillis() - timeStamp >= 1000) {
			timeStamp = System.currentTimeMillis();
			DFPS = FPS;
			FPS = 0;
		} else {FPS++;}
		
		g2D.setFont(defaultFont);
		g2D.setColor(Color.WHITE);
		g2D.drawString("FPS: " + DFPS, 15, 27);
	}
	
	public void move(Creature creature, Moving m) {
		if (isGameOver || isLevelComplete) {return;}

		if (creature instanceof Hero && creature.getEnergy() < 2) {
			try {
				int req = JOptionPane.showConfirmDialog(null, 
						"<HTML>Сил больше нет! Герой падает на землю!<br>Проснуться?", "Беспокойный сон:", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon(ImageIO.read(new File("./resources/images/goblin.png"))));
				
				if (req == 0) {startLevel(currentLevelIndex);}
			} catch (HeadlessException e) {e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();}

			return;
		}
		
		Cell nextCell = null;
		Cell srcCell = getCell(creature);
		if (srcCell == null) {System.err.println("move(): srcCell is NULL?.. Check it!"); return;}
		
		switch (m) {
			case RIGHT: 	
				if (srcCell.getColumnIndex() < COL_LENGHT - 1) {nextCell = cells.get(srcCell.getIndex() + 1);}
				if (nextCell != null) {
					lastCellMemory = srcCell;
					if (creature instanceof Hero) {creature.setEnergy(creature.getEnergy() - stepEnergyDecrease);}
					nextCell.addCreature(srcCell.removeCreature(creature));
					checkCell(nextCell);
				}
				break;
			case LEFT: 	
				if (srcCell.getColumnIndex() > 0) {nextCell = cells.get(srcCell.getIndex() - 1);}
				if (nextCell != null) {
					lastCellMemory = srcCell;
					if (creature instanceof Hero) {creature.setEnergy(creature.getEnergy() - stepEnergyDecrease);}
					nextCell.addCreature(srcCell.removeCreature(creature));
					checkCell(nextCell);
				}
				break;
			case UP: 	
				if (srcCell.getRowIndex() > 0) {nextCell = cells.get(srcCell.getIndex() - COL_LENGHT);}
				if (nextCell != null) {
					lastCellMemory = srcCell;
					if (creature instanceof Hero) {creature.setEnergy(creature.getEnergy() - stepEnergyDecrease);}
					nextCell.addCreature(srcCell.removeCreature(creature));
					checkCell(nextCell);
				}
				break;
			case DOWN: 
				if (srcCell.getRowIndex() < ROW_LENGHT - 1) {nextCell = cells.get(srcCell.getIndex() + COL_LENGHT);}
				if (nextCell != null) {
					lastCellMemory = srcCell;
					if (creature instanceof Hero) {creature.setEnergy(creature.getEnergy() - stepEnergyDecrease);}
					nextCell.addCreature(srcCell.removeCreature(creature));
					checkCell(nextCell);
				}
				break;
			default: //BACK
				nextCell = lastCellMemory;
				lastCellMemory = srcCell;
				if (creature instanceof Hero) {creature.setEnergy(creature.getEnergy() + stepEnergyDecrease);}
				nextCell.addCreature(srcCell.removeCreature(creature));
				checkCell(nextCell);
		}
	}

	public void checkCell(Cell currentCell) {
		for (iGameObjects gObject : currentCell.getContent()) {
			if (gObject instanceof Finish) {setLevelComplete(true);}
			if (gObject instanceof Aids) {player.setHP(player.getHP() + currentCell.removeItem(((Aids) gObject)).getHP());}
			if (gObject instanceof Enpoison) {player.setEnergy(player.getEnergy() + currentCell.removeItem(((Enpoison) gObject)).getHP());}
			if (gObject instanceof Tool) {
				System.out.println("Obtained " + ((Tool) gObject).getName());
				playerTools.add((Tool) gObject);
				currentCell.removeItem((Item) gObject);
			}
			
			if (gObject instanceof Goblin) {
				int req = JOptionPane.showConfirmDialog(null, 
						"<HTML>О, нет! Гоблин на пути!<br>Стоит ли лезть в драку?", "Приготовься к бою!", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon(((Goblin) gObject).getImage()));
				
				if (req == 1) {move(player, Moving.BACK);
				} else {
					player.takeDamage(((Creature) gObject).getStrength());
					player.setEnergy(player.getEnergy() - fightEnergyDecrease);
					currentCell.removeCreature((Creature) gObject);
				}
			}
			if (gObject instanceof Troll) {
				int req = JOptionPane.showConfirmDialog(null, 
						"<HTML>Злой тролль преградил путь!<br>В атаку?", "Приготовься к бою!", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, new ImageIcon(((Troll) gObject).getImage()));
				
				if (req == 1) {move(player, Moving.BACK);
				} else {
					player.takeDamage(((Creature) gObject).getStrength());
					player.setEnergy(player.getEnergy() - fightEnergyDecrease);
					currentCell.removeCreature((Creature) gObject);
				}
			}
		}
		
		
	}
	
	public static Cell getCell(Creature creature) {
		for (Cell c : cells) {
			if (c.contains(creature.getHash())) {return c;}
		}
		
		System.err.println("Не удалось получить координаты существа " + creature.getName());
		return null;
	}

	
	private void setupInAc() {
		inAc.add("BATTLEFIELD", this);
		
		inAc.set("BATTLEFIELD", "move_right", KeyEvent.VK_RIGHT, 0, new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {move(player, BattleField.Moving.RIGHT);}
		});
		
		inAc.set("BATTLEFIELD", "move_left", KeyEvent.VK_LEFT, 0, new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {move(player, BattleField.Moving.LEFT);}
		});
		
		inAc.set("BATTLEFIELD", "move_up", KeyEvent.VK_UP, 0, new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {move(player, BattleField.Moving.UP);}
		});
		
		inAc.set("BATTLEFIELD", "move_down", KeyEvent.VK_DOWN, 0, new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {move(player, BattleField.Moving.DOWN);}
		});
		
		inAc.set("BATTLEFIELD", "next_level", KeyEvent.VK_ENTER, 0, new AbstractAction() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isLevelComplete) {
					if (currentLevelIndex < levelsMassive.length - 1) {
						isLevelComplete = false;
						startLevel(currentLevelIndex + 1);
					}
				}
			}
		});		
	}
		
	public static boolean isGameOver() {return isGameOver;}
	public static void setGameOver(boolean _isGameOver) {isGameOver = _isGameOver;}

	public void setLevelComplete(boolean _isLevelComplete) {isLevelComplete = _isLevelComplete;}

	public static String getPlayerName() {return player.getName();}
	public static Point getPlayerCoordinates() {return new Point(getCell(player).getColumnIndex(), getCell(player).getRowIndex());}
	public static int getPlayerHP() {return player.getHP();}
	public static int getPlayerEN() {return player.getEnergy();}
}
