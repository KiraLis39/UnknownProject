package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import subgui.BattleField;
import subgui.InfoPane;


@SuppressWarnings("serial")
public class BaseGameFrame extends JFrame implements ComponentListener {
	private int GAME_WIDTH = 1024;
	
	public BaseGameFrame() {
		setTitle("The UnKnown game. @KiraLis39, 2020");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setResizable(false);
		
		BattleField gf = new BattleField(5, 8, GAME_WIDTH / 8);
		gf.startLevel(0);

		InfoPane rightInfoPane = new InfoPane();		
		add(gf, BorderLayout.CENTER);
		add(rightInfoPane, BorderLayout.EAST);
		
		addComponentListener(this);
		
		setMinimumSize(new Dimension(GAME_WIDTH + rightInfoPane.getWidth() + 17, GAME_WIDTH / 8 * 5  + 39));
		setLocationRelativeTo(null);
		setVisible(true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					repaint();
					rightInfoPane.update();
					try {Thread.sleep(32);} catch (InterruptedException e) {/* IGNORE SLEEP */}
				}
			}
		}).start();
	}

	public void componentResized(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
}