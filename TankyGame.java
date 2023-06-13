package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class TankyGame implements Runnable {

	TankyGUI tankyGUI = null;
	
	boolean inGame = false;
	
	private Thread gameThread;
	
	// CONSTANTS
	public final int X_SIZE_MENU = 300;
	public final int Y_SIZE_MENU = 300;

	public final int mazeSizeX = 35;
	public final int mazeSizeY = 25;
	
	public final int pixPerCell = 30;
	
	public final int X_SIZE_GAME = pixPerCell*(mazeSizeX-1);
	public final int Y_SIZE_GAME = pixPerCell*(mazeSizeY);
	
	private final int wallThickness = 3;
	
	private final double roundCountdown = 5;
	
	
	private int rounds = 5;
	private long roundEndTimer = -1;
	
	public int numPlayers = 1;
	
	boolean extendedTimer = false;
	
	private Maze maze;
	
	private ArrayList<Rectangle> mazeWalls;
	private ArrayList<Tank> tanks;
	private ArrayList<Bullet> bullets;
	
	public TankyGame() {
		gameThread = new Thread(this);
		gameThread.start();
		mazeWalls = new ArrayList<Rectangle>();
		bullets = new ArrayList<Bullet>();
		tanks = new ArrayList<Tank>();
	}
	
	public void setView(TankyGUI currentGUI) {
		this.tankyGUI = currentGUI;
	}
	
	public void openMenuFrame()  {
        tankyGUI.mainFrame.setSize(X_SIZE_MENU, Y_SIZE_MENU); 
        tankyGUI.mainFrame.setLocationRelativeTo(null);  
        tankyGUI.gamePanel.setVisible(false);
        tankyGUI.menuPanel.setVisible(true);
	}
	
	public void openGameFrame()  {
		tankyGUI.mainFrame.setSize(X_SIZE_GAME, Y_SIZE_GAME);    
		tankyGUI.mainFrame.setLocationRelativeTo(null);  
		tankyGUI.gamePanel.setVisible(true);
		tankyGUI.menuPanel.setVisible(false);
	}
	
	public void initializeMaze() {
		maze = new Maze();
        maze.initializeMaze(mazeSizeX, mazeSizeY);
        generateMazeWalls(maze);
	}
	
	private void generateMazeWalls(Maze fromMaze) {
		int[][] maze = fromMaze.getMaze();
		int currX = 0, currY = 0;
		for (int i = 1; i < mazeSizeY-1; i += 2) {
			for (int j = 1; j < mazeSizeX-1; j += 2) {
				if (j+1 < mazeSizeX) {
					if (maze[i][j+1] == 1) {
						Rectangle rect = new Rectangle(((currX+1)*pixPerCell-wallThickness)*2, currY*pixPerCell*2, wallThickness*2, pixPerCell*2);
						mazeWalls.add(rect);
					}
				}
				if (i+1 < mazeSizeY) {
					if (maze[i+1][j] == 1) {
						Rectangle rect = new Rectangle(currX*pixPerCell*2, ((currY+1)*pixPerCell-wallThickness)*2, pixPerCell*2, wallThickness*2);
						mazeWalls.add(rect);
					}
				}
				if (j+1 < mazeSizeX && i+1 < mazeSizeY) {
					Rectangle rect = new Rectangle(((currX+1)*pixPerCell-wallThickness)*2, ((currY+1)*pixPerCell-wallThickness)*2, wallThickness*2, wallThickness*2);
					mazeWalls.add(rect);
				}
				currX++;
			}
			currX = 0;
			currY++;
		}
		Rectangle rect = new Rectangle(0, 0, X_SIZE_GAME, wallThickness*2);
		mazeWalls.add(rect);
		rect = new Rectangle(0, 0, wallThickness*2, Y_SIZE_GAME);
		mazeWalls.add(rect);
	}
	
	public void resetGame() {
		tankyGUI.gameController.resetKeys();
		mazeWalls = new ArrayList<Rectangle>();
		bullets = new ArrayList<Bullet>();
		tanks = new ArrayList<Tank>();
		initializeMaze();
		spawnPlayers();
		extendedTimer = false;
		roundEndTimer = -1;
	}
	
	public ArrayList<Rectangle> getMazeWalls() {
		return this.mazeWalls;
	}
	
	public ArrayList<Bullet> getBullets() {
		return this.bullets;
	}
	
	public ArrayList<Tank> getTanks() {
		return this.tanks;
	}
	
	public void spawnBullet(Vector2D position, Vector2D direction, double speed) {
		bullets.add(new Bullet(position, direction, speed));
	}
	
	public void spawnPlayer(Vector2D position, double angle, boolean isPlayer1) {
		tanks.add(new Player(this, position, angle, isPlayer1));
	}
	
	public void spawnNikolai(Vector2D position, double angle, boolean isPlayer1) {
		tanks.add(new Nikolai(this, position, angle, isPlayer1));
	}
	
	public void update(double deltaTime) {
		if (inGame) {
			
			if (tanks.size() <= 1) {
				
				if (roundEndTimer == -1) {
					roundEndTimer = System.currentTimeMillis();
				} else {
					long timeSinceRoundEnd = System.currentTimeMillis() - roundEndTimer;
					if (tanks.size() <= 0 && !extendedTimer) {
						extendedTimer = true;
						roundEndTimer = System.currentTimeMillis();
					}
					if (timeSinceRoundEnd > roundCountdown*1000 && inGame) {
						resetGame();
						rounds--;
						if (rounds <= 0) {
							inGame = false;
							openMenuFrame();
						}
						extendedTimer = false;
						roundEndTimer = -1;
					}
				}
			}
			
			for (int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				bullet.update(deltaTime, mazeWalls, tanks);
				if (bullet.isDead()) {
					bullets.remove(bullet);
				}
			}
			
			for (int i = 0; i < tanks.size(); i++) {
				Tank tank = tanks.get(i);
				tank.update(deltaTime, mazeWalls, tanks);
				if (tank.isDead()) {
					tanks.remove(tank);
				}
			}
		}
	}
	
	public void spawnPlayers() {
		Random random = new Random();
		for (int i = 0; i < numPlayers; i++) {
			Vector2D pos = new Vector2D(random.nextInt(X_SIZE_GAME-pixPerCell)+pixPerCell, random.nextInt(Y_SIZE_GAME-pixPerCell)+pixPerCell);
			pos = new Vector2D(pos.x-pos.x%pixPerCell, pos.y-pos.y%pixPerCell);
			spawnPlayer(pos, random.nextDouble()*360, i % 2 == 0);
		}
		Vector2D pos = new Vector2D(random.nextInt(X_SIZE_GAME-pixPerCell)+pixPerCell, random.nextInt(Y_SIZE_GAME-pixPerCell)+pixPerCell);
		pos = new Vector2D(pos.x-pos.x%pixPerCell, pos.y-pos.y%pixPerCell);
		spawnNikolai(pos, random.nextDouble()*360, true);
	}
	
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
	
	@Override
	public void run() {
		double drawInterval = 1000 / 200;
		double deltaTime = 0;
		long lastTime = System.currentTimeMillis();
		long currentTime;
		
		while (tankyGUI == null) {
			
		}

		while (true) {

			currentTime = System.currentTimeMillis();

			deltaTime += (currentTime - lastTime) / drawInterval;

			lastTime = currentTime;
			
			update(deltaTime);

			if (deltaTime >= 1) {
				tankyGUI.update();
				deltaTime--;
			}
		}
	}
}
