package Tanky;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TankyGUI extends JPanel {

	// INSTANCE VARIABLES
	public JFrame mainFrame;
	public TankyGame tankyGame;

	// MENU PANEL
	public JPanel menuPanel = new JPanel();
	private JButton playButton = new JButton("Play");
	private JButton menuExitButton = new JButton("Exit");
	private JCheckBox player2Option = new JCheckBox("Player 2 Enabled");
	public TankyMenuController playController;

	// GAME PANEL
	public TankyGameRender gamePanel;
	private JButton gameExitButton = new JButton("Exit");
	public TankyGameController gameController;

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
		menuPanel.setLayout(new BorderLayout());
		menuPanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_MENU, tankyGame.Y_SIZE_MENU));
		JPanel menuButtons = new JPanel();
		menuButtons.add(playButton);
		menuButtons.add(menuExitButton);
		menuPanel.add(menuButtons, BorderLayout.NORTH);
		JPanel menuOptions = new JPanel();
		menuOptions.add(player2Option);
		menuPanel.add(menuOptions, BorderLayout.CENTER);
		this.add(menuPanel);
		
		gamePanel = new TankyGameRender(tankyGame);
		gamePanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_GAME, tankyGame.Y_SIZE_GAME));
		gamePanel.add(gameExitButton);
		this.add(gamePanel);
		
		gamePanel.setVisible(false);
	}

	private void registerControllers() {
		 playController = new TankyMenuController(this, playButton, menuExitButton, player2Option);
		 playButton.addActionListener(playController);
		 menuExitButton.addActionListener(playController);
		 
		 gameController = new TankyGameController(this, gameExitButton);
		 gameExitButton.addActionListener(gameController);
		 this.gamePanel.addKeyListener(gameController);
		 this.gamePanel.setFocusable(true);
	}

	public void update() {
		if (tankyGame.inGame) {
			gamePanel.repaint();
		}
	}
}
