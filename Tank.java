package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public abstract class Tank {

	protected static final int diameter = 24;
	protected final int lifeTime = 15;
	protected final double turnSpeed = 1;
	protected final double speed = .4;
	
	protected Color color;
	
	protected TankyGame tankyGame;

	protected Rectangle hitbox;
	protected Vector2D position;
	protected double angle;
	
	protected int moveDir = 0;
	protected int turnDir = 0;
	
	protected long timeAlive;
	protected long timeSpawned;
	
	protected boolean dead = false;
	
	public Tank(TankyGame tankyGame, Vector2D position, double angle) {
		this.tankyGame = tankyGame;
		this.position = position;
		this.angle = angle;
		this.hitbox = new Rectangle((int) position.x, (int) position.y, diameter, diameter);
		Random random = new Random(System.nanoTime());
		this.color = Color.getHSBColor(random.nextFloat(), .5f, 1);
		timeSpawned = System.currentTimeMillis();
	}
	
	public abstract void update(double deltaTime, ArrayList<Rectangle> mazeWalls, ArrayList<Tank> tanks);
	
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
	
	public void shoot() {
		Vector2D bulletPos = new Vector2D(position.x+hitbox.width/2-Bullet.diameter/2 + Math.cos(Math.toRadians(angle))*diameter/1.5, position.y+hitbox.height/2-Bullet.diameter/2 + Math.sin(Math.toRadians(angle))*diameter/1.5);
		tankyGame.spawnBullet(bulletPos, new Vector2D(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))), .5);
	}
 }
