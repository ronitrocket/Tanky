package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class TankyGame {

	TankyGUI tankyGUI;
	
	boolean inGame = false;
	
	public int hue = 0;
	public Color color = Color.getHSBColor((float) hue / 255, 1, 1);
	
	Maze maze;
	
	// CONSTANTS
	public final int X_SIZE_MENU = 300;
	public final int Y_SIZE_MENU = 300;

	public final int mazeSizeX = 35;
	public final int mazeSizeY = 25;
	
	public final int pixPerCell = 30;
	
	public final int X_SIZE_GAME = pixPerCell*(mazeSizeX-1);
	public final int Y_SIZE_GAME = pixPerCell*(mazeSizeY);
	
	private final int wallThickness = 2;
	
	private ArrayList<Rectangle> mazeWalls;
	private ArrayList<Tank> tanks;
	private ArrayList<Bullet> bullets;
	
	public void setView(TankyGUI currentGUI) {
		this.tankyGUI = currentGUI;
		mazeWalls = new ArrayList<Rectangle>();
		bullets = new ArrayList<Bullet>();
		tanks = new ArrayList<Tank>();
	}
	
	public void generateMazeWalls(Maze fromMaze) {
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
		mazeWalls = new ArrayList<Rectangle>();
		bullets = new ArrayList<Bullet>();
		tanks = new ArrayList<Tank>();
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
	
	public void update(double deltaTime) {
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			bullet.update(deltaTime, mazeWalls, tanks);
			if (bullet.isDead()) {
				bullets.remove(bullet);
			}
		}
		
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			tank.update(deltaTime, mazeWalls);
			if (tank.isDead()) {
				tanks.remove(tank);
			}
		}
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
}
