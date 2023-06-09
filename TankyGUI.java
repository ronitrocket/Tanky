package Tanky;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TankyGUI extends JPanel {

	// INSTANCE VARIABLES
	public JFrame mainFrame;
	public TankyGame tankyGame;

	// MENU PANEL
	public JPanel menuPanel = new JPanel();
	private JButton playButton = new JButton("Play");
	private JButton menuExitButton = new JButton("Exit");
	public TankyMenuController playController;

	// GAME PANEL
	public TankyGameRender gamePanel;
	private JButton gameExitButton = new JButton("Exit");
	public TankyGameController gameController;

	// move to TankyGame
	boolean inGame = false;
	
	Maze maze;

	public TankyGUI(TankyGame tankyGame, JFrame mainFrame) {
		super();
		this.tankyGame = tankyGame;
		this.mainFrame = mainFrame;
//          this.game.setGUI(this);
		this.setupView();
		this.registerControllers();
		this.update();
	}

	private void setupView() {
		menuPanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_MENU, tankyGame.Y_SIZE_MENU));
		menuPanel.add(playButton);
		menuPanel.add(menuExitButton);
		this.add(menuPanel);
		gamePanel = new TankyGameRender(tankyGame);
		gamePanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_GAME, tankyGame.Y_SIZE_GAME));
		gamePanel.add(gameExitButton);
		this.add(gamePanel);
		gamePanel.setVisible(false);
	}

	private void registerControllers() {
		 playController = new TankyMenuController(this, playButton, menuExitButton);
		 playButton.addActionListener(playController);
		 menuExitButton.addActionListener(playController);
		 
		 gameController = new TankyGameController(this, gameExitButton);
		 gameExitButton.addActionListener(gameController);
		 this.gamePanel.addKeyListener(gameController);
		 this.gamePanel.setFocusable(true);
	}

	public void update() {
		if (inGame) {
			gamePanel.repaint();
		}
	}
}
