package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

//Nikolai
//Final Programming Assignment - Tanky
/**
* Class that represents an AI player, and includes all relevant code for it's behaviour
* 
* @author ronittaleti
*/
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class Nikolai extends Tank {
	
	// Instance Variables
	private final int spawnMovementDelay = 2;
	
	public Line2D.Double leftWhisker;
	public Line2D.Double rightWhisker;
	
	ArrayList<Line2D.Double> rayLines = new ArrayList<Line2D.Double>(); // For debugging
	ArrayList<Line2D.Double> normalLines = new ArrayList<Line2D.Double>(); // For debugging
	
	private boolean isShooting = false;
	
	private long runningStart = 0;
	
	private int maxBounces;
	
	private double currShotAngle;
	
	/**
	 * Default constructor.
	 * 
	 * Creates a new AI with the specified parameters.
	 * 
	 * @param tankyGame		A reference to the TankyGame model.
	 * @param position		A vector representation of the tank's position.
	 * @param angle 		The angle the tank is facing.
	 * @param color 		The color of the tank.
	 * @param maxBounces 	The maximum amount of bounces the AI can calculate.
	 */
	public Nikolai(TankyGame tankyGame, Vector2D position, double angle, Color color, int maxBounces) {
		super(tankyGame, position, angle, color);
		leftWhisker = new Line2D.Double();
		rightWhisker = new Line2D.Double();
		timeSpawned = System.currentTimeMillis();
		this.maxBounces = maxBounces;
	}

	/**
	 * Updates the attributes of the tank based on the time between frames (Delta time)
	 * 
	 * @param deltaTime		The deltaTime (time between frames).
	 * @param mazeWalls		An ArrayList of Rectangles which represent the maze walls
	 * @param tanks 		An ArrayList of Tanks for the AI to choose a target from
	 * @param bullets 		An ArrayList of Bullets for the AI to check collison with
	 */
	@Override
	public void update(double deltaTime, ArrayList<Rectangle> mazeWalls, ArrayList<Tank> tanks, ArrayList<Bullet> bullets) {
		
		// Upon spawning, if the tank is in a wall, move it out of the wall
		moveToValidLocation(deltaTime, mazeWalls);
		
		// Only move after a small delay (in case the AI spawns beside a player
		if (System.currentTimeMillis()-timeSpawned > 1000*spawnMovementDelay) {
			// Move forward if the time since the last shot or the last time the AI ran from a bullet was more than 1 second,
			// OR if the AI is not in the process of shooting
			if ((System.currentTimeMillis() - lastShotTime > 1000 && System.currentTimeMillis() - runningStart > 1000) || !isShooting) {
				moveDir = 1;
			} else {
				moveDir = 0;
			}
			
			// Stop tracking shot bullets after they die
			for (int i = 0; i < shotBullets.size(); i++) {
				Bullet bullet = shotBullets.get(i);
				if (bullet.isDead()) {
					shotBullets.remove(bullet);
				}
			}
			
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
	
			// Set the positon of the tank on the y direction, and check for collisions.
			setPosition(position.addVector(new Vector2D(0, Math.sin(Math.toRadians(angle)) * speed * deltaTime * moveDir)));
			debounce = 1000;
			if (isCollidingWall(mazeWalls)) {
				while (isCollidingWall(mazeWalls)) {
					// Move backwards until we leave the wall
					setPosition(position.addVector(new Vector2D(0, -Math.sin(Math.toRadians(angle)) * speed * deltaTime * moveDir)));
					if (debounce <= 0) { // Preventative measure if the tank somehow never leaves a wall (shouldn't happen)
						break;
					}
					debounce--;
				}
			}
			
			// Set the position of the whiskers used to check for walls
			setColliderPos();
			
			// Turn the tank based on the value of turnDir
			turn();
			
			// Get the closest tank
			double lowestDist = Double.MAX_VALUE;
			Tank closestTank = null;
			for (Tank tank : tanks) {
				if (position.dist(tank.position) < lowestDist && tank != this) {
					lowestDist = position.dist(tank.position);
					closestTank = tank;
				}
			}
			
			// If there is another tank it can target
			if (closestTank != null) {
				
				// Get the (signed) angle to turn towards
				Vector2D fromDirVector = new Vector2D(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
				Vector2D toDirVector = shotBullets.size() < 10 ? closestTank.position.subtractVector(this.position) : closestTank.position.subtractVector(this.position).scalarMultiply(-1);
				double angle = Math.toDegrees(Math.atan2(fromDirVector.x*toDirVector.y - fromDirVector.y*toDirVector.x, fromDirVector.x*toDirVector.x + fromDirVector.y*toDirVector.y));
				
				// Weight that controls whether the tank turns left or right (negative means left, positive is right)
				double weight = 0;
				
				// Add weight based on the sign of the angle also weighting more heavily if the angle is a bigger value
				weight += (angle/Math.abs(angle))*Math.pow(.1*(2), (Math.abs(angle)));
				
				// Add or subtract weight based on whether the left or right whiskers are touching walls
				if (isLeftWhiskerColliding(mazeWalls)) {
					weight += 1;
				} else if (isRightWhiskerColliding(mazeWalls)) {
					weight -= 1;
				}
				
				// Check if a bullet is approaching, else try and calculate a shot towards the player
				Vector2D bulletApproachDir = isBulletApproachingTank(mazeWalls, bullets);
				if (bulletApproachDir != null) {
					// If the bullet approach direction doesn't have a value of infinity, then try to turn away from the bullet,
					// ELSE move backwards.
					if (bulletApproachDir.x != Double.POSITIVE_INFINITY) {
						isShooting = false;
						fromDirVector = new Vector2D(Math.cos(Math.toRadians(this.angle)), Math.sin(Math.toRadians(this.angle)));
						toDirVector = bulletApproachDir;
						angle = Math.toDegrees(Math.atan2(fromDirVector.x*toDirVector.y - fromDirVector.y*toDirVector.x, fromDirVector.x*toDirVector.x + fromDirVector.y*toDirVector.y));
						if (angle > 0) {
							turnDir = 1;
						} else {
							turnDir = -1;
						}
						if (Math.abs(angle) < 0.001) {
							moveDir = 1;
						}
					} else {
						turnDir = 0;
						moveDir = -1;
						runningStart = System.currentTimeMillis(); // Start running
					}
				} else {
					// Get the shot angle, if it exists, towards the player
					double shotAngle = Double.NaN;
					if (shotBullets.size() < 10) {
						shotAngle = simulateBullets(mazeWalls, closestTank.hitbox);
					}
					
					// If the shot angle does exist, or the AI is in the process of shooting, and the AI hasn't been running recently,
					// AND it has bullets, then start shooting
					if ((!Double.isNaN(shotAngle) || isShooting) && System.currentTimeMillis()-runningStart > 500 && shotBullets.size() < 10) {
						// If we just started shooting, then set the current shot angle to the one that was found
						// (this is to make the AI finish a shot before trying to turn towards the next calculated shot)
						if (!isShooting) {
							currShotAngle = shotAngle;
						}
						
						// Mark that we in the process of shooting
						isShooting = true;
						
						// Get the angle to turn towards to fire the shot
						fromDirVector = new Vector2D(Math.cos(Math.toRadians(this.angle)), Math.sin(Math.toRadians(this.angle)));
						toDirVector = new Vector2D(Math.cos(currShotAngle), Math.sin(currShotAngle));
						angle = Math.toDegrees(Math.atan2(fromDirVector.x*toDirVector.y - fromDirVector.y*toDirVector.x, fromDirVector.x*toDirVector.x + fromDirVector.y*toDirVector.y));
						
						// Make sure we aren't moving
						moveDir = 0;
						turnDir = 0;
						
						// Turn towards the angle we are going to shoot at
						if (angle > 0) {
							turnDir = 1;
						} else {
							turnDir = -1;
						}
						
						// If we are facing the angle, then fire shots every half a second, and mark that this shot has finished
						if (Math.abs(angle) < 0.001) {
							if (System.currentTimeMillis() - lastShotTime > 500) {
								lastShotTime = System.currentTimeMillis();
								shoot();
								isShooting = false;
							}
						}
					} else {
						// If we aren't running, turn left and right as it normally would if it wasn't in the presence of a player
						if (System.currentTimeMillis()-runningStart > 500) {
							if (weight > 0) {
								turnDir = 1;
							} else {
								turnDir = -1;
							}
						}
					}
				}
			} else {
				// Mark that we aren't shooting (since there are no tanks anymore), and make sure that we still try and dodge bullets
				// if any are still left on the board
				isShooting = false;
				turnDir = 0;
				moveDir = 0;
				Vector2D bulletApproachDir = isBulletApproachingTank(mazeWalls, bullets);
				if (bulletApproachDir != null) {
					isShooting = false;
					Vector2D fromDirVector = new Vector2D(Math.cos(Math.toRadians(this.angle)), Math.sin(Math.toRadians(this.angle)));
					Vector2D toDirVector = bulletApproachDir;
					double angle = Math.toDegrees(Math.atan2(fromDirVector.x*toDirVector.y - fromDirVector.y*toDirVector.x, fromDirVector.x*toDirVector.x + fromDirVector.y*toDirVector.y));
					if (angle > 0) {
						turnDir = 1;
					} else {
						turnDir = -1;
					}
					//System.out.println(angle);
					if (Math.abs(angle) < 0.001) {
						moveDir = 1;
					}
				}
				turn();
				setPosition(position.addVector(new Vector2D(Math.cos(Math.toRadians(angle)) * speed * deltaTime * moveDir, 0)));
			}
		}
	}
	
	/**
	 * Simulates bullets to find an angle at which the AI can shoot to (hopefully) get a kill
	 * 
	 * @param mazeWalls			An ArrayList of Rectangles which represent the maze walls
	 * @param targetHitbox 		The hitbox of the target tank to check for bullet collisions
	 * 
	 * @return angleFound		The angle to fire at to hit the target tank assuming it didn't move since the shot was fired.
	 */
	public double simulateBullets(ArrayList<Rectangle> mazeWalls, Rectangle targetHitbox) {
		// Convert the walls of the maze from rectangles to individual lines
		// This must be done since the raycast only works with lines and not Rectangles
		ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();
		for (Rectangle wall : mazeWalls) {
			lines.add(new Line2D.Double(wall.x, wall.y, wall.x, wall.y + wall.height));
			lines.add(new Line2D.Double(wall.x + wall.width, wall.y, wall.x, wall.y));
			lines.add(new Line2D.Double(wall.x, wall.y + wall.height, wall.x + wall.width, wall.y + wall.height));
			lines.add(new Line2D.Double(wall.x + wall.width, wall.y + wall.height, wall.x + wall.width,  wall.y));
		}
		// Look at various angles around the tank, with 10 degrees inbetween each angle we look at
		double startAngle = this.angle-160;
		double angleFound = Double.NaN;
		for (int i = 0; i < 320; i++) {
			double angle = Math.toRadians(startAngle + i * 10);
			// Get a line that represents the shot
			Line2D.Double shotLine = new Line2D.Double(position.x+hitbox.width/2, position.y+hitbox.height/2, position.x+hitbox.width/2 + Math.cos(angle) * 5000, position.y+hitbox.height/2 + Math.sin(angle) * 5000);
			
			// If the shot lands a hit, then return the angle we found.
			if (checkForTankCollision(shotLine, lines, 0, maxBounces, targetHitbox, new Vector2D(Math.cos(angle), Math.sin(angle)), null)) {
				angleFound = angle;
				break;
			}
		}
		
		return angleFound;
	}
	
	/**
	 * Checks if a bullet is approaching the AI
	 * 
	 * @param mazeWalls			An ArrayList of Rectangles which represent the maze walls
	 * @param bullets 			An ArrayList of Bullets for the AI to check collisions from
	 * 
	 * @return normal			The angle to fire at to hit the target tank assuming it didn't move since the shot was fired.
	 */
	private Vector2D isBulletApproachingTank(ArrayList<Rectangle> mazeWalls, ArrayList<Bullet> bullets) {
		// Convert the walls of the maze from rectangles to individual lines
		// This must be done since the raycast only works with lines and not Rectangles
		ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();
		for (Rectangle wall : mazeWalls) {
			lines.add(new Line2D.Double(wall.x, wall.y, wall.x, wall.y + wall.height));
			lines.add(new Line2D.Double(wall.x + wall.width, wall.y, wall.x, wall.y));
			lines.add(new Line2D.Double(wall.x, wall.y + wall.height, wall.x + wall.width, wall.y + wall.height));
			lines.add(new Line2D.Double(wall.x + wall.width, wall.y + wall.height, wall.x + wall.width,  wall.y));
		}
		// For each bullet fired
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			// If the AI is not the owner of the bullet
			if (bullet.owner != this) {
				// Get the bullet's path as an ArrayList of lines (each line is one segment of the path)
				ArrayList<Line2D.Double> bulletPath = new ArrayList<Line2D.Double>();
				Line2D.Double initialShotLine = new Line2D.Double(bullet.getPosition().x, bullet.getPosition().y, bullet.getPosition().x + bullet.getDirection().x * 5000, bullet.getPosition().y + bullet.getDirection().y * 5000);
				getBulletPath(initialShotLine, lines, 0, 3, bullet.getDirection(), null, bulletPath);
				// For each line in the path
				for (Line2D.Double clippedLine : bulletPath) {
					// Get the normals of the line
					Vector2D normal1 = new Vector2D((clippedLine.y1 - clippedLine.y2), (clippedLine.x2 - clippedLine.x1)).unit(); 
					Vector2D normal2 = new Vector2D(-(clippedLine.y1 - clippedLine.y2), -(clippedLine.x2 - clippedLine.x1)).unit(); 
					// If the line intersects the tank directly
					if (clippedLine.intersects(new Rectangle((int)(hitbox.x), (int)(hitbox.y), hitbox.width, hitbox.height))) {
						System.out.println("sus");
						boolean intersected1 = false;
						boolean intersected2 = false;
						// Check if either normal line intersects a wall
						for (Line2D.Double line : lines) {
							Line2D.Double normalLine1 = new Line2D.Double(hitbox.getCenterX(), hitbox.getCenterY(), hitbox.getCenterX() + normal1.scalarMultiply(10).x, hitbox.getCenterY() + normal1.scalarMultiply(10).y);
							Line2D.Double normalLine2 = new Line2D.Double(hitbox.getCenterX(), hitbox.getCenterY(), hitbox.getCenterX() + normal2.scalarMultiply(10).x, hitbox.getCenterY() + normal2.scalarMultiply(10).y);
							if (normalLine1.intersectsLine(line)) {
								intersected1 = true;
							}
							if (normalLine2.intersectsLine(line)) {
								intersected2 = true;
							}
							if (intersected1 && intersected2) {
								break;
							}
						}
						// If we didn't intersect with the first normal, return that, else return the second normal if we didnt intersect that either.
						if (!intersected1) {
							return normal1;
						}
						if (!intersected2) {
							return normal2;
						}
					}
					// If we would intersect the bullet by moving forward, return infinity (represents that we should move backwards)
					if (clippedLine.intersectsLine(new Line2D.Double(hitbox.getCenterX(), hitbox.getCenterY(), (int)(hitbox.getCenterX() + Math.cos(Math.toRadians(this.angle))*60), (int)(hitbox.getCenterY() + Math.sin(Math.toRadians(this.angle))*60))) && bullet.getPosition().dist(this.position) > 90) {
						return new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the path of a given bullet
	 * 
	 * @param shotLine			The line along which the bullet will travel
	 * @param lines 			An ArrayList of lines for the AI to check an intersection with the shotLine
	 * @param numBounces 		The current amount of bounces calculated
	 * @param maxBounces 		The maximum amount of bounces we will calculate
	 * @param shotDir 			The direction of the shot
	 * @param prevDetectedLine 	The line that we detected on the last bounce (must be null on the first call)
	 * @param bulletPath	 	The ArrayList that keeps track of the bullet's path
	 */
	private void getBulletPath(Line2D.Double shotLine, ArrayList<Line2D.Double> lines, int numBounces, int maxBounces, Vector2D shotDir, Line2D.Double prevDetectedLine, ArrayList<Line2D.Double> bulletPath) {
		numBounces++;
		if (numBounces <= maxBounces) {
			float minDist = Float.MAX_VALUE;
			Line2D.Double detectedLine = null;
			for (Line2D.Double line : lines) {
				float dist = getRaycast(shotLine, line);
				if (dist != -1 && dist < minDist && line != prevDetectedLine) {
					minDist = dist;
					detectedLine = line;
				}
			}
			if (detectedLine != null) {
				//double len = Math.sqrt(Math.pow((detectedLine.y1 - detectedLine.y2),2) + Math.pow((detectedLine.x2 - detectedLine.x1),2));
				Vector2D normal = new Vector2D((detectedLine.y1 - detectedLine.y2), (detectedLine.x2 - detectedLine.x1)).unit(); 
				Vector2D shotVector = new Vector2D(position.x+hitbox.width/2 + shotDir.x * 5000, position.y+hitbox.height/2 + shotDir.y * 5000);
				double dot = Vector2D.dot(shotVector, normal);
				Vector2D newDir = shotVector.subtractVector(normal.scalarMultiply(2).scalarMultiply(dot));
				Line2D.Double newShotLine = new Line2D.Double(shotLine.x1 + shotDir.x * minDist, shotLine.y1 + shotDir.y * minDist, position.x+hitbox.width/2 + newDir.x * 5000, position.y+hitbox.height/2 + newDir.y * 5000);
				//rayLines.add(newShotLine);				
				Line2D.Double clippedLine = new Line2D.Double(shotLine.x1, shotLine.y1, shotLine.x1 + shotDir.x * minDist, shotLine.y1 + shotDir.y * minDist);
				Line2D.Double normalLine = new Line2D.Double(clippedLine.x2, clippedLine.y2, clippedLine.x2 + normal.x * 10, clippedLine.y2 + normal.y * 10);
				normalLines.add(normalLine);
				rayLines.add(clippedLine);
				bulletPath.add(clippedLine);
				getBulletPath(newShotLine, lines, numBounces, maxBounces, newDir.unit(), detectedLine, bulletPath);
			}
		}
	}
	
	/**
	 * Checks whether shooting along a given angle will hit the target tank
	 * 
	 * @param shotLine			The line along which the bullet will travel
	 * @param lines 			An ArrayList of lines for the AI to check an intersection with the shotLine
	 * @param numBounces 		The current amount of bounces calculated
	 * @param maxBounces 		The maximum amount of bounces we will calculate
	 * @param targetHitbox	 	The Rectangle that represents the current target's hitbox
	 * @param shotDir 			The direction of the shot
	 * @param prevDetectedLine 	The line that we detected on the last bounce (must be null on the first call)
	 * 
	 * @return hasHit			True or false depending on whether we hit a tank or not
	 */
	private boolean checkForTankCollision(Line2D.Double shotLine, ArrayList<Line2D.Double> lines, int numBounces, int maxBounces, Rectangle targetHitbox, Vector2D shotDir, Line2D.Double prevDetectedLine) {
		numBounces++;
		if (numBounces <= maxBounces) {
			float minDist = Float.MAX_VALUE;
			Line2D.Double detectedLine = null;
			for (Line2D.Double line : lines) {
				float dist = getRaycast(shotLine, line);
				if (dist != -1 && dist < minDist && line != prevDetectedLine) {
					minDist = dist;
					detectedLine = line;
				}
			}
			if (detectedLine != null && minDist > 60) {
				//double len = Math.sqrt(Math.pow((detectedLine.y1 - detectedLine.y2),2) + Math.pow((detectedLine.x2 - detectedLine.x1),2));
				Vector2D normal = new Vector2D((detectedLine.y1 - detectedLine.y2), (detectedLine.x2 - detectedLine.x1)).unit(); 
				Vector2D shotVector = new Vector2D(position.x+hitbox.width/2 + shotDir.x * 5000, position.y+hitbox.height/2 + shotDir.y * 5000);
				double dot = Vector2D.dot(shotVector, normal);
				Vector2D newDir = shotVector.subtractVector(normal.scalarMultiply(2).scalarMultiply(dot));
				Line2D.Double newShotLine = new Line2D.Double(shotLine.x1 + shotDir.x * minDist, shotLine.y1 + shotDir.y * minDist, position.x+hitbox.width/2 + newDir.x * 5000, position.y+hitbox.height/2 + newDir.y * 5000);
				//rayLines.add(newShotLine);				
				Line2D.Double clippedLine = new Line2D.Double(shotLine.x1, shotLine.y1, shotLine.x1 + shotDir.x * minDist, shotLine.y1 + shotDir.y * minDist);
				Line2D.Double normalLine = new Line2D.Double(clippedLine.x2, clippedLine.y2, clippedLine.x2 + normal.x * 10, clippedLine.y2 + normal.y * 10);
				normalLines.add(normalLine);
				rayLines.add(clippedLine);
				if (clippedLine.intersects(targetHitbox)) {
					return true;
				}
				if (checkForTankCollision(newShotLine, lines, numBounces, maxBounces, targetHitbox, newDir.unit(), detectedLine)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets distance between two points
	 * 
	 * @param x1		X-Coord of point 1
	 * @param y1		Y-Coord of point 1
	 * @param x2		X-Coord of point 2
	 * @param y2		Y-Coord of point 2
	 *
	 * @return dist		The distance between the points	
	 */
	public static float dist(float x1, float y1, float x2, float y2) {
	    return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	/**
	 * Returns the distance between the start of a line and it's POI with another line
	 * 
	 * @param line1		The first line
	 * @param line2		The second line
	 *
	 * @return dist		The distance between the start of line1 and it's POI with line2
	 */
	public static float getRaycast(Line2D.Double line1, Line2D.Double line2) {
		float p0_x = (float) line1.x1;
		float p0_y = (float) line1.y1;
		float p1_x = (float) line1.x2;
		float p1_y = (float) line1.y2;
		
		float p2_x = (float) line2.x1;
		float p2_y = (float) line2.y1;
		float p3_x = (float) line2.x2;
		float p3_y = (float) line2.y2;
		
	    float s1_x, s1_y, s2_x, s2_y;
	    s1_x = p1_x - p0_x;
	    s1_y = p1_y - p0_y;
	    s2_x = p3_x - p2_x;
	    s2_y = p3_y - p2_y;

	    float s, t;
	    s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
	    t = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
	        // Collision detected
	        float x = p0_x + (t * s1_x);
	        float y = p0_y + (t * s1_y);

	        return dist(p0_x, p0_y, x, y);
	    }

	    return -1; // No collision
	}
	
	/**
	 * Checks if the left whisker of the tank is colliding
	 * 
	 * @param mazeWalls			An ArrayList of Rectangles which represent the maze walls
	 *
	 * @return isColliding		True or false depending on whether the whisker was colliding or not.
	 */
	public boolean isLeftWhiskerColliding(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (leftWhisker.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	/**
	 * Checks if the right whisker of the tank is colliding
	 * 
	 * @param mazeWalls			An ArrayList of Rectangles which represent the maze walls
	 *
	 * @return isColliding		True or false depending on whether the whisker was colliding or not.
	 */
	public boolean isRightWhiskerColliding(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (rightWhisker.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	/**
	 * Sets the position of the whiskers relative to the tank
	 */
	private void setColliderPos() {
		Vector2D center = new Vector2D(getHitbox().x+getHitbox().width/2, getHitbox().y+getHitbox().height/2);
		
		Vector2D point1 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)), getHitbox().y+getHitbox().height/2);
		Vector2D point2 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)-(int)(getHitbox().width*1.2)), getHitbox().y-getHitbox().height/1.5);
		Vector2D rotatedPoint1 = rotateAroundPoint(point1, center, getAngle()+90);
		Vector2D rotatedPoint2 = rotateAroundPoint(point2, center, getAngle()+90);
		
		leftWhisker = new Line2D.Double(rotatedPoint1.x, rotatedPoint1.y, rotatedPoint2.x, rotatedPoint2.y);
		
		point1 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)), getHitbox().y+getHitbox().height/2);
		point2 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)+(int)(getHitbox().width*1.2)), getHitbox().y-getHitbox().height/1.5);
		rotatedPoint1 = rotateAroundPoint(point1, center, getAngle()+90);
		rotatedPoint2 = rotateAroundPoint(point2, center, getAngle()+90);
		
		rightWhisker = new Line2D.Double(rotatedPoint1.x, rotatedPoint1.y, rotatedPoint2.x, rotatedPoint2.y);
	}
	
	/**
	 * Helper function to rotate a point around another.
	 * 
	 * @param point 		The point that will be rotated
	 * @param center		The center of rotation
	 * @param angle 		The angle in degrees we will be rotating
	 * 
	 * @return newPoint		The new point after rotation
	 */
	public Vector2D rotateAroundPoint(Vector2D point, Vector2D center, double angle) {
		double x1 = point.x - center.x;
		double y1 = point.y - center.y;

		double x2 = x1 * Math.cos(Math.toRadians(angle)) - y1 * Math.sin(Math.toRadians(angle));
		double y2 = x1 * Math.sin(Math.toRadians(angle)) + y1 * Math.cos(Math.toRadians(angle));

		return new Vector2D(x2 + center.x, y2 + center.y);
	}
}// end of class
