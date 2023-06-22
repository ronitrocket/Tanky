package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

//Tank
//Final Programming Assignment - Tanky
/**
* Abstract class that represents a tank.
* 
* @author ronittaleti
*/
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public abstract class Tank {

	
	// Instance Variables
	protected static final int diameter = 24;
	protected final double turnSpeed = 1;
	protected final double speed = .2;

	protected Color color;
	
	protected TankyGame tankyGame;

	protected Rectangle hitbox;
	protected Vector2D position;
	protected double angle;
	
	protected int moveDir = 0;
	protected int turnDir = 0;
	
	protected long timeSpawned;
	
	protected long lastShotTime = 0;
	
	protected boolean dead = false;
	
	protected AudioClip tankFire;
	protected AudioClip tankDeath;
	
	protected ArrayList<Bullet> shotBullets;
	
	/**
	 * Default constructor.
	 * 
	 * Creates a new Tank with the specified parameters.
	 * 
	 * @param tankyGame		A reference to the TankyGame model.
	 * @param position		A vector representation of the tank's position.
	 * @param angle 		The angle the tank is facing.
	 * @param color 		The color of the tank.
	 */
	public Tank(TankyGame tankyGame, Vector2D position, double angle, Color color) {
		this.tankyGame = tankyGame;
		this.position = position;
		this.angle = angle;
		this.hitbox = new Rectangle((int) position.x, (int) position.y, diameter, diameter);
		this.color = color;
		this.shotBullets = new ArrayList<Bullet>();
		timeSpawned = System.currentTimeMillis();
		tankDeath = new AudioClip("resources/tankDeath.wav");
		tankDeath.setVolume(.3f);
		tankFire = new AudioClip("resources/tankFire.wav");
		tankFire.setVolume(.3f);
	}
	
	/**
	 * Abstract method that must be implemented to manage behaviour in all subclasses of Tank
	 */
	public abstract void update(double deltaTime, ArrayList<Rectangle> mazeWalls, ArrayList<Tank> tanks, ArrayList<Bullet> bullets);
	
	/**
	 * Moves the tank to a valid location if it is stuck in a wall.
	 * 
	 * @param deltaTime		The deltaTime (time between frames).
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 */
	public void moveToValidLocation(double deltaTime, ArrayList<Rectangle> mazeWalls) {
		// Set the positon of the tank on the x direction, and check for collisions.
		setPosition(position.addVector(new Vector2D(Math.cos(Math.toRadians(angle)) * speed * deltaTime * moveDir, 0)));
		int debounce = 1000;
		if (isCollidingWall(mazeWalls)) {
			while (isCollidingWall(mazeWalls)) {
				// Move backwards until we leave the wall
				setPosition(position.addVector(new Vector2D(-Math.cos(Math.toRadians(angle)) * speed * deltaTime * moveDir, 0)));
				if (debounce <= 0) { // Preventative measure if the tank somehow never leaves a wall (shouldn't happen)
					break;
				}
				debounce--;
			}
		}
		
		// Set the positon of the tank on the x direction, and check for collisions.
		setPosition(position.addVector(new Vector2D(0, Math.sin(Math.toRadians(angle)) * speed * deltaTime * moveDir)));
		debounce = 1000;
		if (isCollidingWall(mazeWalls)) {
			// Move backwards until we leave the wall
			while (isCollidingWall(mazeWalls)) {
				setPosition(position.addVector(new Vector2D(0, -Math.sin(Math.toRadians(angle)) * speed*deltaTime * moveDir)));
				if (debounce <= 0) { // Preventative measure if the tank somehow never leaves a wall (shouldn't happen)
					break;
				}
				debounce--;
			}
		}
	}
	
	/**
	 * Checks whether the Tank is colliding with a wall
	 * 
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 * 
	 * @return isColliding	Whether the tank is colliding with a wall or not
	 */
	public boolean isCollidingWall(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (this.hitbox.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	/**
	 * Sets the position of the Tank.
	 * 
	 * @param position		A vector representation of the target position for the tank
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
		hitbox.x = (int) position.x;
		hitbox.y = (int) position.y;
	}
	
	/**
	 * Turns the tank based on the value of turnDir
	 */
	public void turn() {
		this.angle += turnSpeed*turnDir;
		this.angle = ((this.angle%360) + 360)%360;
	}

	/**
	 * Returns the hitbox of the tank
	 * 
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	/**
	 * Returns the angle of the tank
	 * 
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Returns whether the tank is dead or not
	 * 
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Shoots a bullet from the tank
	 * 
	 * @return bullet 	The bullet that was shot
	 */
	public Bullet shoot() {
		if (shotBullets.size() < 10) {
			Vector2D bulletPos = new Vector2D(position.x+hitbox.width/2-Bullet.getDiameter()/2 + Math.cos(Math.toRadians(angle))*diameter/1.5, position.y+hitbox.height/2-Bullet.getDiameter()/2 + Math.sin(Math.toRadians(angle))*diameter/1.5);
			Bullet bullet = tankyGame.spawnBullet(bulletPos, new Vector2D(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle))), .5, this.color, this);
			shotBullets.add(bullet);
			tankyGame.audioHandler.playAudio(tankFire);
			return bullet;
		}
		return null;
	}
 }// end of class
