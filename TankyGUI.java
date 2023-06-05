package Tanky;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TankyGUI extends JPanel {

	// INSTANCE VARIABLES
	private JFrame mainFrame;

	// CONSTANTS
	public final int X_SIZE_MENU = 300;
	public final int Y_SIZE_MENU = 300;
	public final int X_SIZE_GAME = 1000;
	public final int Y_SIZE_GAME = 750;

	// MENU PANEL
	public JPanel menuPanel = new JPanel();
	private JButton playButton = new JButton("Play");
	private JButton menuExitButton = new JButton("Exit");

	// GAME PANEL
	public TankyGameRender gamePanel = new TankyGameRender(this);
	private JButton gameExitButton = new JButton("Exit");

	// move to TankyGame
	boolean inGame = false;

	// figure out where to put this
	public int hue = 0;
	public Color color = Color.getHSBColor((float) hue / 255, 1, 1);

	public TankyGUI(JFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
//          this.game.setGUI(this);
		this.setupView();
		this.registerControllers();
		this.update();
	}

	private void setupView() {
		menuPanel.setPreferredSize(new Dimension(X_SIZE_MENU, Y_SIZE_MENU));
		menuPanel.add(playButton);
		menuPanel.add(menuExitButton);
		this.add(menuPanel);
		gamePanel.setPreferredSize(new Dimension(X_SIZE_GAME, Y_SIZE_GAME));
		gamePanel.add(gameExitButton);
		this.add(gamePanel);
		gamePanel.setVisible(false);
	}

	private void registerControllers() {
		 TankyMenuController playController = new TankyMenuController(this, playButton, menuExitButton);
		 playButton.addActionListener(playController);
		 menuExitButton.addActionListener(playController);
		 
		 TankyGameController gameController = new TankyGameController(this, gameExitButton);
		 gameExitButton.addActionListener(gameController);
	}

	public void update() {
		if (inGame) {
			mainFrame.setSize(X_SIZE_GAME, Y_SIZE_GAME);
			hue++;
			if (hue == 256) {
				hue = 0;
			}
			color = Color.getHSBColor((float) hue / 255, 1, 1);
			gamePanel.repaint();
		} else {
			mainFrame.setSize(X_SIZE_MENU, Y_SIZE_MENU);
		}
	}
}
