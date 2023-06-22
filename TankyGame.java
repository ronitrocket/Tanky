package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class TankyGame implements Runnable {

	// Instance Variables
	TankyGUI tankyGUI = null;

	boolean inGame = false;

	private Thread gameThread;

	public AudioHandler audioHandler;
	
	public final int X_SIZE_MENU = 300;
	public final int Y_SIZE_MENU = 300;

	public final int X_SIZE_WINNER = 500;
	public final int Y_SIZE_WINNER = 300;

	public final int mazeSizeX = 35;
	public final int mazeSizeY = 25;

	public final int pixPerCell = 30;

	public final int X_SIZE_GAME = pixPerCell * (mazeSizeX - 1);
	public final int Y_SIZE_GAME = pixPerCell * (mazeSizeY);

	private final int wallThickness = 3;

	private final double roundCountdown = 5;

	public int player1Wins = 0;
	public int player2Wins = 0;
	public int playerAIWins = 0;

	public String timeOfRoundStart = "";

	public int maxRounds = 5;
	public int roundsPassed = 0;
	private long roundEndTimer = -1;

	public int numPlayers = 1;
	public boolean aiEnabled = false;
	public int maxBounces = 1;

	boolean extendedTimer = false;

	private Maze maze;

	private ArrayList<Rectangle> mazeWalls;
	private ArrayList<Tank> tanks;
	private ArrayList<Bullet> bullets;

	private final Color[] tankColors = { Color.RED, Color.GREEN, Color.gray };

	private AudioClip music;

	private FileWriter fw;
	private PrintWriter out;

	/**
	 * Default constructor.
	 * 
	 * Creates the game model
	 */
	public TankyGame() {
		mazeWalls = new ArrayList<Rectangle>();
		bullets = new ArrayList<Bullet>();
		tanks = new ArrayList<Tank>();
		audioHandler = new AudioHandler();
		music = new AudioClip("resources/music.wav");
		music.setVolume(.1f);
		audioHandler.playAudio(music);
	}

	/**
	 * Starts the game loop thread
	 */
	public void startThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * Sets the view of the game model
	 */
	public void setView(TankyGUI currentGUI) {
		this.tankyGUI = currentGUI;
	}

	/**
	 * Opens the menu frame of the TankyGUI view
	 */
	public void openMenuFrame() {
		tankyGUI.mainFrame.setSize(X_SIZE_MENU, Y_SIZE_MENU);
		tankyGUI.mainFrame.setLocationRelativeTo(null);
		tankyGUI.gamePanel.setVisible(false);
		tankyGUI.winnerPanel.setVisible(false);
		tankyGUI.menuPanel.setVisible(true);
	}

	/**
	 * Opens the game frame of the TankyGUI view
	 */
	public void openGameFrame() {
		tankyGUI.mainFrame.setSize(X_SIZE_GAME, Y_SIZE_GAME);
		tankyGUI.mainFrame.setLocationRelativeTo(null);
		tankyGUI.gamePanel.setVisible(true);
		tankyGUI.winnerPanel.setVisible(false);
		tankyGUI.menuPanel.setVisible(false);
	}

	/**
	 * Opens the winner frame of the TankyGUI view and modifies the text accordingly with the winner
	 */
	public void openWinnerFrame() {
		tankyGUI.mainFrame.setSize(X_SIZE_WINNER, Y_SIZE_WINNER);
		tankyGUI.mainFrame.setLocationRelativeTo(null);
		tankyGUI.gamePanel.setVisible(false);
		tankyGUI.winnerPanel.setVisible(true);
		tankyGUI.menuPanel.setVisible(false);

		if (numPlayers == 1) {
			if (player1Wins > playerAIWins) {
				tankyGUI.winnerText.setText("Winner is: Player One!");
			} else if (player1Wins < playerAIWins) {
				tankyGUI.winnerText.setText("Winner is: Nikolai!");
			}
		} else {
			if (player1Wins > playerAIWins) {
				if (player1Wins > player2Wins) {
					tankyGUI.winnerText.setText("Winner is: Player One!");
				} else if (player1Wins < player2Wins) {
					tankyGUI.winnerText.setText("Winner is: Player Two!");
				} else {
					tankyGUI.winnerText.setText("Winner is: Player One and Player Two!");
				}
			} else if (player1Wins < playerAIWins) {
				if (playerAIWins > player2Wins) {
					tankyGUI.winnerText.setText("Winner is: Nikolai!");
				} else if (playerAIWins < player2Wins) {
					tankyGUI.winnerText.setText("Winner is: Player Two!");
				} else {
					tankyGUI.winnerText.setText("Winner is: Player Two and Nikolai!");
				}
			} else {
				if (player1Wins > player2Wins) {
					tankyGUI.winnerText.setText("Winner is: Player One and Nikolai!");
				} else {
					tankyGUI.winnerText.setText("Winners are: Player One, Player Two and Nikolai! (Tie)");
				}
			}
		}
		tankyGUI.winnerScores.setText("Scores | " + (numPlayers == 1
				? "Player One: " + player1Wins + ", Nikolai: " + playerAIWins
				: (aiEnabled)
						? "Player One: " + player1Wins + ", Player Two: " + player2Wins + ", Nikolai: " + playerAIWins
						: "Player One: " + player1Wins + ", Player Two: " + player2Wins));
		out.println(tankyGUI.winnerText.getText());
		out.println(tankyGUI.winnerScores.getText());
		out.close();
	}

	/**
	 * Initializes the maze for the game map
	 */
	public void initializeMaze() {
		maze = new Maze(mazeSizeX, mazeSizeY);
		generateMazeWalls(maze);
	}

	/**
	 * Converts the maze from an Integer array to Rectangles representing the walls
	 * 
	 * @param fromMaze		The Maze object
	 */
	private void generateMazeWalls(Maze fromMaze) {
		int[][] maze = fromMaze.getMaze();
		int currX = 0, currY = 0;
		for (int i = 1; i < mazeSizeY - 1; i += 2) {
			for (int j = 1; j < mazeSizeX - 1; j += 2) {
				// Creates thin vertical rectangles if there is a wall at that position
				if (j + 1 < mazeSizeX) {
					if (maze[i][j + 1] == 1) {
						Rectangle rect = new Rectangle(((currX + 1) * pixPerCell - wallThickness) * 2,
								currY * pixPerCell * 2, wallThickness * 2, pixPerCell * 2);
						mazeWalls.add(rect);
					}
				}
				// Creates thin horizontal rectangles if there is a wall at that position
				if (i + 1 < mazeSizeY) {
					if (maze[i + 1][j] == 1) {
						Rectangle rect = new Rectangle(currX * pixPerCell * 2,
								((currY + 1) * pixPerCell - wallThickness) * 2, pixPerCell * 2, wallThickness * 2);
						mazeWalls.add(rect);
					}
				}
				// Creates a small square to fill in the gaps between the other 2 wall types
				if (j + 2 < mazeSizeX && i + 2 < mazeSizeY) {
					if (maze[i + 1][j + 2] == 1 || maze[i + 2][j + 1] == 1) {
						Rectangle rect = new Rectangle(((currX + 1) * pixPerCell - wallThickness) * 2,
								((currY + 1) * pixPerCell - wallThickness) * 2, wallThickness * 2, wallThickness * 2);
						mazeWalls.add(rect);
					}
				}
				currX++;
			}
			currX = 0;
			currY++;
		}
		// Create walls for the top and left sides of the screens
		Rectangle rect = new Rectangle(0, 0, X_SIZE_GAME, wallThickness * 2);
		mazeWalls.add(rect);
		rect = new Rectangle(0, 0, wallThickness * 2, Y_SIZE_GAME);
		mazeWalls.add(rect);
	}

	/**
	 * Starts a game of Tanky
	 */
	public void startGame() {
		timeOfRoundStart = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		try {
			fw = new FileWriter(timeOfRoundStart, true);
			out = new PrintWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inGame = true;
		roundsPassed = 0;
		player1Wins = 0;
		player2Wins = 0;
		playerAIWins = 0;
		openGameFrame();
		resetRound();
	}

	/**
	 * Resets a game of Tanky for a new round
	 */
	public void resetRound() {
		tankyGUI.gameController.resetKeys();
		mazeWalls = new ArrayList<Rectangle>();
		bullets = new ArrayList<Bullet>();
		tanks = new ArrayList<Tank>();
		initializeMaze();
		spawnPlayers();
		extendedTimer = false;
		roundEndTimer = -1;
	}

	/**
	 * Gets the walls of the maze as Rectangles
	 * 
	 * @return mazeWalls		An ArrayList of all the walls
	 */
	public ArrayList<Rectangle> getMazeWalls() {
		return this.mazeWalls;
	}

	/**
	 * Gets the active bullets
	 * 
	 * @return bullets		An ArrayList of all the bullets
	 */
	public ArrayList<Bullet> getBullets() {
		return this.bullets;
	}

	/**
	 * Gets the active tanks
	 * 
	 * @return tanks		An ArrayList of all the tanks
	 */
	public ArrayList<Tank> getTanks() {
		return this.tanks;
	}

	/**
	 * Spawns a bullet into the map
	 * 
	 * @param position		A vector representation of the bullet's position.
	 * @param direction		A vector representation of the bullet's current direction
	 * @param speed 		The speed of the bullet
	 * @param color 		The color of the bullet
	 * @param owner 		The tank which owns the bullet
	 * 
	 * @return Bullet		The bullet that was spawned
	 */
	public Bullet spawnBullet(Vector2D position, Vector2D direction, double speed, Color color, Tank owner) {
		Bullet bullet = new Bullet(this, position, direction, speed, color, owner);
		bullets.add(bullet);
		return bullet;
	}

	/**
	 * Spawns a player into the map
	 * 
	 * @param tankyGame		A reference to the TankyGame model.
	 * @param position		A vector representation of the tank's position.
	 * @param angle 		The angle the tank is facing.
	 * @param isPlayer1 	Whether this player is player 1 or not
	 * @param color 		The color of the tank.
	 *
	 */
	public void spawnPlayer(Vector2D position, double angle, boolean isPlayer1, Color color) {
		tanks.add(new Player(this, position, angle, isPlayer1, color));
	}

	/**
	 * Spawns a Nikolai (AI) into the map
	 * 
	 * @param tankyGame		A reference to the TankyGame model.
	 * @param position		A vector representation of the tank's position.
	 * @param angle 		The angle the tank is facing.
	 * @param color 		The color of the tank.
	 * @param maxBounces 	The maximum amount of bounces the AI can calculate.
	 *
	 */
	public void spawnNikolai(Vector2D position, double angle, boolean isPlayer1, Color color, int maxBounces) {
		tanks.add(new Nikolai(this, position, angle, color, maxBounces));
	}

	/**
	 * Updates the attributes of the tanks and bullets
	 * 
	 * @param deltaTime		The deltaTime (time between frames).
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 * @param tanks 		An ArrayList of Tanks for the AI to choose a target from
	 * @param bullets 		An ArrayList of Bullets for the AI to check collison with
	 */
	public void update(double deltaTime) {
		if (inGame) {

			// If there is one tank or less, enter the round end stage of the game
			if (tanks.size() <= 1) {
				// If we haven't started the round end timer, set it to the current time
				if (roundEndTimer == -1) {
					roundEndTimer = System.currentTimeMillis();
				} else {
					// Get the time since the last tank died
					long timeSinceRoundEnd = System.currentTimeMillis() - roundEndTimer;
					// If the last tank left alive died, extend the timer by resetting the round end timer
					if (tanks.size() <= 0 && !extendedTimer) {
						extendedTimer = true;
						roundEndTimer = System.currentTimeMillis();
					}
					// If the time since the last tank died is more than the tankCountdown
					// and we are still in game in case the player exited the game prematurely
					if (timeSinceRoundEnd > roundCountdown * 1000 && inGame) {
						// If there was a tank left alive, then count the round as valid and increase the number of rounds passed,
						// as well as incrementing the correct player's win count
						if (!tanks.isEmpty()) {
							roundsPassed++;
							Tank tank = tanks.get(0); // Gets the last tank
							// If the tank is a player, check which player it is and increment their wins, else increment
							// the AI's wins
							if (tank instanceof Player) {
								Player player = (Player) tank;
								if (player.isPlayer1()) {
									out.println("Round " + roundsPassed + " of " + maxRounds + ": Player One Wins!");
									player1Wins++;
								}
								if (player.isPlayer2()) {
									out.println("Round " + roundsPassed + " of " + maxRounds + ": Player Two Wins!");
									player2Wins++;
								}
							} else if (tank instanceof Nikolai) {
								out.println("Round " + roundsPassed + " of " + maxRounds + ": Nikolai Wins!");
								playerAIWins++;
							}
						}
						// Reset the round
						resetRound();
						// If we have reached the max rounds as defined by the player, end the game and display results.
						if (roundsPassed >= maxRounds) {
							inGame = false;
							openWinnerFrame();
						}
						// Reset the timer variables
						extendedTimer = false;
						roundEndTimer = -1;
					}
				}
			}

			// Update the bullets, removing them if they have died
			for (int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				bullet.update(deltaTime, mazeWalls, tanks);
				if (bullet.isDead()) {
					bullets.remove(bullet);
				}
			}

			// Update the tanks, removing them if they have died
			for (int i = 0; i < tanks.size(); i++) {
				Tank tank = tanks.get(i);
				tank.update(deltaTime, mazeWalls, tanks, bullets);
				if (tank.isDead()) {
					audioHandler.playAudio(tank.tankDeath);
					tanks.remove(tank);
				}
			}
		}
	}

	/**
	 * Spawns the players, based on numPlayers and aiEnabled
	 */
	public void spawnPlayers() {
		Random random = new Random();
		for (int i = 0; i < numPlayers; i++) {
			Vector2D pos = new Vector2D(random.nextInt(X_SIZE_GAME - pixPerCell * 2) + pixPerCell,
					random.nextInt(Y_SIZE_GAME - pixPerCell * 2) + pixPerCell);
			pos = new Vector2D(pos.x, pos.y - pos.y % pixPerCell);
			spawnPlayer(pos, random.nextDouble() * 360, i % 2 == 0, tankColors[i]);
		}
		if (aiEnabled) {
			Vector2D pos = new Vector2D(random.nextInt(X_SIZE_GAME - pixPerCell * 2) + pixPerCell,
					random.nextInt(Y_SIZE_GAME - pixPerCell * 2) + pixPerCell);
			pos = new Vector2D(pos.x - pos.x % pixPerCell, pos.y - pos.y % pixPerCell);
			spawnNikolai(pos, random.nextDouble() * 360, true, tankColors[2], maxBounces);
		}
	}

	/**
	 * Returns the tank designated as Player 1
	 */
	public Player getPlayer1() {
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if (tank instanceof Player) {
				Player player = (Player) tank;
				if (player.isPlayer1()) {
					return player;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the tank designated as Player 2
	 */
	public Player getPlayer2() {
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if (tank instanceof Player) {
				Player player = (Player) tank;
				if (player.isPlayer2()) {
					return player;
				}
			}
		}
		return null;
	}

	/**
	 * Runnable's built in run() method that is called when the thread with this
	 * object as a parameter is called.
	 */
	@Override
	public void run() {
		// Variables for the game loop
		double drawInterval = 1000 / 200; // Draw interval is 1 frame every 5 ms (1000/200)
		double deltaTime = 0;
		long lastTime = System.currentTimeMillis();
		long currentTime;

		while (true) {

			currentTime = System.currentTimeMillis();

			deltaTime += (currentTime - lastTime) / drawInterval; // Change in time

			lastTime = currentTime;

			// One frame has passed when deltaTime = 1 assuming there is absolutely no lag
			// If deltaTime is >= 1, update the model and view
			if (deltaTime >= 1) {
				update(deltaTime);
				tankyGUI.update();
				deltaTime--;
			}
		}
	}
}
