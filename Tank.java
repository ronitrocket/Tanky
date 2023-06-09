package Tanky;

import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Tank {

	protected final int diameter = 32;
	protected final int lifeTime = 15;
	protected final double turnSpeed = 1;
	
	protected TankyGame tankyGame;

	protected Rectangle hitbox;
	protected Vector2D position;
	protected double angle;
	protected double speed;
	
	protected int moveDir = 0;
	protected int turnDir = 0;
	
	protected long timeAlive;
	protected long timeSpawned;
	
	protected boolean dead = false;
	
	public Tank(TankyGame tankyGame, Vector2D position, double angle, double speed) {
		this.tankyGame = tankyGame;
		this.position = position;
		this.angle = angle;
		this.speed = speed;
		this.hitbox = new Rectangle((int) position.x, (int) position.y, diameter, diameter);
		timeSpawned = System.currentTimeMillis();
	}
	
	public abstract void update(double deltaTime, ArrayList<Rectangle> mazeWalls);
	
	public boolean isCollidingWall(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (this.hitbox.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	public void setPosition(Vector2D position) {
		this.position = position;
		hitbox.x = (int) position.x;
		hitbox.y = (int) position.y;
	}
	
	public void turn() {
		this.angle += turnSpeed*turnDir;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public boolean isDead() {
		return dead;
	}
}
