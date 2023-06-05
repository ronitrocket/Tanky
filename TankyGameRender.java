package Tanky;

import java.awt.Graphics;

import javax.swing.JPanel;

public class TankyGameRender extends JPanel {
	
	TankyGUI tankyGUI;
	
	public TankyGameRender(TankyGUI tankyGUI) {
		this.tankyGUI = tankyGUI;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(tankyGUI.color);
		g.fillRect(0, 0, 1000, 750);
	}
}
