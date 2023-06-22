package Tanky;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TankyGUI extends JPanel {

	// INSTANCE VARIABLES
	public JFrame mainFrame;
	public TankyGame tankyGame;

	// MENU PANEL
	public JPanel menuPanel = new JPanel();
	private JButton menuExitButton = new JButton("Exit");
	private JButton playButton = new JButton("Play");
	private JCheckBox player2Option = new JCheckBox("Player 2 Enabled");
	private JCheckBox aiOption = new JCheckBox("Nikolai (AI) Enabled");
	private JLabel roundsLabel = new JLabel("Num. of Rounds (1-9) (must be odd): ");
	private JTextField roundsOption = new JTextField("5", 2);
	private JLabel diffLabel = new JLabel("                    Difficulty Settings                    ");
	private JCheckBox easyDiffOption = new JCheckBox("Easy");
	private JCheckBox hardDiffOption = new JCheckBox("Hard");
	private JCheckBox extremeDiffOption = new JCheckBox("Extreme");
	public TankyMenuController playController;

	// GAME PANEL
	public TankyGameRender gamePanel;
	private JButton gameExitButton = new JButton("Exit");
	public TankyGameController gameController;
	
	// WINNER PANEL
	public JPanel winnerPanel = new JPanel();
	private JButton winnerExitButton = new JButton("Exit");
	private JButton winnerPlayAgainButton = new JButton("Play Again!");
	private JButton winnerOpenFileButton = new JButton("Open Results!");
	public JLabel winnerText = new JLabel();
	public JLabel winnerScores = new JLabel();
	public TankyWinnerController winnerController;

	/**
	 * Default constructor.
	 * 
	 * Creates the game view
	 * 
	 * @param tankyGame		The TankyGame model linked to this view
	 * @param mainFrame		The JFrame containing this view
	 */
	public TankyGUI(TankyGame tankyGame, JFrame mainFrame) {
		super();
		this.tankyGame = tankyGame;
		this.mainFrame = mainFrame;
		this.setupView();
		this.registerControllers();
		this.update();
	}

	/**
	 * Sets up the view of the TankyGUI view
	 */
	private void setupView() {
		menuPanel.setLayout(new GridLayout(5, 1));
		menuPanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_MENU, tankyGame.Y_SIZE_MENU));
		
		JPanel menuButtons = new JPanel();
		menuButtons.add(menuExitButton);
		menuButtons.add(playButton);
		menuPanel.add(menuButtons);
		
		JPanel playerOptions = new JPanel();
		playerOptions.add(player2Option);
		playerOptions.add(aiOption);
		aiOption.setSelected(true);
		menuPanel.add(playerOptions);
		
		JPanel roundOptions = new JPanel();
		roundOptions.add(roundsLabel);
		roundOptions.add(roundsOption);
		menuPanel.add(roundOptions);
		
		JPanel diffOptions = new JPanel();
		diffOptions.add(diffLabel);
		diffOptions.add(easyDiffOption);
		diffOptions.add(hardDiffOption);
		diffOptions.add(extremeDiffOption);
		easyDiffOption.setSelected(true);
		menuPanel.add(diffOptions);
		this.add(menuPanel);
		
		
		gamePanel = new TankyGameRender(tankyGame);
		gamePanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_GAME, tankyGame.Y_SIZE_GAME));
		gamePanel.add(gameExitButton);
		this.add(gamePanel);
		
		winnerPanel.setLayout(new GridLayout(5, 1));
		winnerPanel.setPreferredSize(new Dimension(tankyGame.X_SIZE_WINNER, tankyGame.X_SIZE_WINNER));
		
		JPanel winnerButtons = new JPanel();
		winnerButtons.add(winnerExitButton);
		winnerButtons.add(winnerPlayAgainButton);
		winnerButtons.add(winnerOpenFileButton);
		winnerPanel.add(winnerButtons);
		
		JPanel winnerDisplay = new JPanel();
		winnerDisplay.add(winnerText);
		winnerPanel.add(winnerDisplay);
		
		JPanel winnerInfo = new JPanel();
		winnerInfo.add(winnerScores);
		winnerPanel.add(winnerInfo);
		this.add(winnerPanel);
		
		gamePanel.setVisible(false);
		winnerPanel.setVisible(false);
		
	}

	/**
	 * Registers all the controllers to the interactable portions of the TankyGUI view
	 */
	private void registerControllers() {
		 playController = new TankyMenuController(this, playButton, menuExitButton, player2Option, aiOption, easyDiffOption, hardDiffOption, extremeDiffOption, roundsOption);
		 playButton.addActionListener(playController);
		 menuExitButton.addActionListener(playController);
		 player2Option.addActionListener(playController);
		 aiOption.addActionListener(playController);
		 easyDiffOption.addActionListener(playController);
		 hardDiffOption.addActionListener(playController);
		 extremeDiffOption.addActionListener(playController);
		 roundsOption.addActionListener(playController);
		 
		 gameController = new TankyGameController(this, gameExitButton);
		 gameExitButton.addActionListener(gameController);
		 this.gamePanel.addKeyListener(gameController);
		 this.gamePanel.setFocusable(true);
		 
		 winnerController = new TankyWinnerController(this, winnerExitButton, winnerPlayAgainButton, winnerOpenFileButton);
		 winnerExitButton.addActionListener(winnerController);
		 winnerPlayAgainButton.addActionListener(winnerController);
		 winnerOpenFileButton.addActionListener(winnerController);
	}

	/**
	 * Updates the view with information from the TankyGame model
	 */
	public void update() {
		if (tankyGame.inGame) {
			gamePanel.repaint();
		}
	}
}
