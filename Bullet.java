package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

//Bullet
//Final Programming Assignment - Tanky
/**
* Class that represents a bullet, and handles all updating of bullet position and behaviours
* 
* @author ronittaleti
*/
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class Bullet {

	// Instance Variables
	public Color color;
	public Tank owner;
	
	private static final int diameter = 1;
	private static final int diameterFake = 6;
	private final int lifeTime = 15;

	private Rectangle hitbox;
	private Rectangle dimension;
	
	private Vector2D position;
	private Vector2D direction;
	private double speed;
	
	private long timeSpawned;
	
	private AudioClip bulletBounce;
	private AudioClip bulletDeath;
	
	private boolean dead = false;
	
	private TankyGame tankyGame;

	/**
	 * Default constructor.
	 * 
	 * Creates a new bullet with the specified parameters.
	 * 
	 * @param tankyGame		A reference to the TankyGame model.
	 * @param position		A vector representation of the bullet's position.
	 * @param direction		A vector representation of the bullet's current direction
	 * @param speed 		The speed of the bullet
	 * @param color 		The color of the bullet
	 * @param owner 		The tank which owns the bullet
	 */
	public Bullet(TankyGame tankyGame, Vector2D position, Vector2D direction, double speed, Color color, Tank owner) {
		this.tankyGame = tankyGame;
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		this.hitbox = new Rectangle((int) position.x, (int) position.y, getDiameter(), getDiameter());
		this.dimension = new Rectangle((int) position.x, (int) position.y, diameterFake, diameterFake);
		this.color = color;
		this.owner = owner;
		
		bulletBounce = new AudioClip("resources/bulletBounce.wav");
		bulletBounce.setVolume(.3f);
		bulletDeath = new AudioClip("resources/bulletDeath.wav");
		bulletDeath.setVolume(.3f);
		
		timeSpawned = System.currentTimeMillis();
	}

	/**
	 * Updates the attributes of the bullet based on the time between frames (Delta time)
	 * 
	 * @param deltaTime		The deltaTime (time between frames).
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 * @param tanks 		An ArrayList of Tanks for the bullet to check collison with
	 */
	public void update(double deltaTime, ArrayList<Rectangle> mazeWalls, ArrayList<Tank> tanks) {
		
		// Upon spawning, if the bullet is in a wall, move it out of the wall
		moveToValidLocation(deltaTime, mazeWalls);
		
		// Kill the bullet if it has been alive for the specified lifeTime 
		if (System.currentTimeMillis()-timeSpawned > 1000*lifeTime) {
			tankyGame.audioHandler.playAudio(bulletDeath);
			dead = true;
		}

		// Set the positon of the bullet on the x direction, and check for collisions.
		setPosition(position.addVector(new Vector2D(direction.unit().x * speed * deltaTime, 0)));
		int debounce = 1000;
		if (isCollidingWall(mazeWalls)) {
			// Move the bullet out of the wall by moving in the opposite direction of the bullet's x direction.
			while (isCollidingWall(mazeWalls)) {
				setPosition(position.addVector(new Vector2D(-direction.unit().x * speed * deltaTime, 0)));
				if (debounce <= 0) { // Preventative measure if the bullet somehow never leaves a wall (shouldn't happen)
					break;
				}
				debounce--;
			}
			
			// Reverse the direction on the x axis to make the bullet bounce
			direction.x = -direction.x;
			tankyGame.audioHandler.playAudio(bulletBounce);
		}

		// Set the positon of the bullet on the y direction, and check for collisions.
		setPosition(position.addVector(new Vector2D(0, direction.unit().y * speed * deltaTime)));
		debounce = 1000;
		if (isCollidingWall(mazeWalls)) {
			// Move the bullet out of the wall by moving in the opposite direction of the bullet's y direction.
			while (isCollidingWall(mazeWalls)) {
				setPosition(position.addVector(new Vector2D(0, -direction.unit().y * speed*deltaTime)));
				if (debounce <= 0) { // Preventative measure if the bullet somehow never leaves a wall (shouldn't happen)
					break;
				}
				debounce--;
			}
			
			// Reverse the direction on the y axis to make the bullet bounce
			direction.y = -direction.y;
			tankyGame.audioHandler.playAudio(bulletBounce);
		}
		
		// Check for collision with tanks
		isCollidingTank(tanks);
	}
	
	/**
	 * Moves the bullet to a valid location (anywhere not inside a wall)
	 * 
	 * @param deltaTime		The deltaTime (time between frames).
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 */
	public void moveToValidLocation(double deltaTime, ArrayList<Rectangle> mazeWalls) {
		if (direction.unit().x <= direction.unit().y) {
			int debounce = 1000;
			if (isCollidingWall(mazeWalls)) {
				while (isCollidingWall(mazeWalls)) {
					setPosition(position.addVector(new Vector2D(-direction.unit().x * speed * deltaTime, 0)));
					if (debounce <= 0) {
						break;
					}
					debounce--;
				}
				direction.x = -direction.x;
			}
			
			debounce = 1000;
			if (isCollidingWall(mazeWalls)) {
				while (isCollidingWall(mazeWalls)) {
					setPosition(position.addVector(new Vector2D(0, -direction.unit().y * speed*deltaTime)));
					if (debounce <= 0) {
						break;
					}
					debounce--;
				}
				direction.y = -direction.y;
			}
		} else {
			int debounce = 1000;
			if (isCollidingWall(mazeWalls)) {
				while (isCollidingWall(mazeWalls)) {
					setPosition(position.addVector(new Vector2D(0, -direction.unit().y * speed*deltaTime)));
					if (debounce <= 0) {
						break;
					}
					debounce--;
				}
				direction.y = -direction.y;
			}
			
			debounce = 1000;
			if (isCollidingWall(mazeWalls)) {
				while (isCollidingWall(mazeWalls)) {
					setPosition(position.addVector(new Vector2D(-direction.unit().x * speed * deltaTime, 0)));
					if (debounce <= 0) {
						break;
					}
					debounce--;
				}
				direction.x = -direction.x;
			}
		}
	}
	
	/**
	 * Checks for collision between the bullet and any walls
	 * 
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 * 
	 * @return	True or false based on whether the bullet was intersecting a wall.
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
	 * Checks for collision between the bullet and any tanks, killing any colliding tanks
	 * 
	 * @param tanks 		An ArrayList of Tanks for the bullet to check collison with
	 */
	public void isCollidingTank(ArrayList<Tank> tanks) {
		for (Tank tank : tanks) {
			Ellipse2D.Double bulletHitbox = new Ellipse2D.Double(hitbox.x, hitbox.y, diameter, diameter);
			Ellipse2D.Double tankHitbox = new Ellipse2D.Double(tank.position.x, tank.position.y, Tank.diameter, Tank.diameter);
			if (circleIntersects(bulletHitbox, tankHitbox) && this.owner != tank) {
				tank.dead = true;
				dead = true;
 			}
		}
	}
	
	/**
	 * Checks for an intersection between two circles (used to get more accurate collisions)
	 * 
	 * @param circle1		The first circle that is experiencing an intersection
	 * @param circle2		The second circle that is experiencing an intersection
	 * 
	 * @return	True or false based on whether the circles were intersecting
	 */
	public boolean circleIntersects(Ellipse2D.Double circle1, Ellipse2D.Double circle2)
    {
        double distanceX = circle1.getCenterX() - circle2.getCenterX();
        double distanceY = circle1.getCenterY() - circle2.getCenterY();
        double radiusSum = circle2.width/2 + circle1.width/2;
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }
	
	/**
	 * Sets the position of the bullet.
	 * 
	 * @param position		A vector representation of the target position for the bullet
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
		hitbox.x = (int) position.x;
		hitbox.y = (int) position.y;
		dimension.x = (int) position.x-dimension.width/2;
		dimension.y = (int) position.y-dimension.height/2;
	}
	
	/**
	 * Returns the position of the bullet
	 * 
	 */
	public Vector2D getPosition() {
		return position;
	}
	
	/**
	 * Returns the direction of the bullet
	 * 
	 */
	public Vector2D getDirection() {
		return direction;
	}
	
	/**
	 * Returns the hitbox of the bullet
	 * 
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	/**
	 * Returns the dimension of the bullet
	 * 
	 */
	public Rectangle getDimension() {
		return dimension;
	}
	
	/**
	 * Returns whether the bullet is dead or not
	 * 
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Returns the diameter of the bullet
	 * 
	 */
	public static int getDiameter() {
		return diameter;
	}
}
