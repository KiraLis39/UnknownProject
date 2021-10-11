package subgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fox.builders.FoxFontBuilder.FONT;
import registry.Registry;


@SuppressWarnings("serial")
public class InfoPane extends JPanel {
	private JLabel labelCoordinates, labelHeroHP, labelHeroEnergy;
	private Font infoFont = Registry.ffb.setFoxFont(FONT.LEELAWADEE, 18, false);
	
	public InfoPane() {
		setPreferredSize(new Dimension(256, 0));
		setSize(new Dimension(256, 0));
		setBackground(Color.BLACK);
		
		setLayout(new GridLayout(20, 1, 3, 3));
		setBorder(new EmptyBorder(3, 6, 3, 6));
				
		add(new JLabel("<HTML><h1><b>Информация:<hr></HTML>") {{setForeground(Color.CYAN.darker()); setFont(infoFont);}});
		add(new JLabel("<HTML>Hero name: <b color='red'>" + BattleField.getPlayerName()) {{setForeground(Color.CYAN); setFont(infoFont);}});
		
		
		labelCoordinates = new JLabel("Coordinates: ") {{setForeground(Color.CYAN); setFont(infoFont);}};
		add(labelCoordinates);
		
		labelHeroHP = new JLabel("Hero`s HP: ") {{setForeground(Color.CYAN); setFont(infoFont);}};
		add(labelHeroHP);
		
		labelHeroEnergy = new JLabel("Hero`s EN: ") {{setForeground(Color.CYAN); setFont(infoFont);}};
		add(labelHeroEnergy);
	}

	public void update() {
		try {
			labelCoordinates.setText("<HTML>Coordinates: <b color='red'>" + BattleField.getPlayerCoordinates().x + "x" + BattleField.getPlayerCoordinates().y);
			String res = "";
			for (int i = 0; i < BattleField.getPlayerHP() / 10; i++) {
				res += "\u10F5";
			}
			labelHeroHP.setText("<HTML>Hero`s HP: <b color='red'>" + res);
			res = "";
			for (int i = 0; i < BattleField.getPlayerEN() / 10; i++) {
				res += "\u0EA7";
			}
			labelHeroEnergy.setText("<HTML>Hero`s EN: <b color='yellow'>" + res);
		} catch (Exception e) {/* IGNORE LOST INFO */}
	}
}
