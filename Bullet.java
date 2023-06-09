package Tanky;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Bullet {

	public static final int diameter = 6;
	private final int lifeTime = 15;

	private Rectangle hitbox;
	private Vector2D position;
	private Vector2D direction;
	private double speed;
	
	private long timeAlive;
	private long timeSpawned;
	
	private boolean dead = false;

	public Bullet(Vector2D position, Vector2D direction, double speed) {
		this.position = position;
		this.direction = direction;
		this.speed = speed;
		this.hitbox = new Rectangle((int) position.x, (int) position.y, diameter, diameter);
		timeSpawned = System.currentTimeMillis();
	}

	public void update(double deltaTime, ArrayList<Rectangle> mazeWalls, ArrayList<Tank> tanks) {
		
		timeAlive = System.currentTimeMillis()-timeSpawned;
		
		if (timeAlive > 1000*lifeTime) {
			dead = true;
		}

		setPosition(position.addVector(new Vector2D(direction.unit().x * speed * deltaTime, 0)));

		
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

		setPosition(position.addVector(new Vector2D(0, direction.unit().y * speed * deltaTime)));
		
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
		
		isCollidingTank(tanks);
	}
	
	public boolean isCollidingWall(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (this.hitbox.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	public void isCollidingTank(ArrayList<Tank> tanks) {
		for (Tank tank : tanks) {
			Ellipse2D.Double bulletHitbox = new Ellipse2D.Double(position.x, position.y, diameter, diameter);
			Ellipse2D.Double tankHitbox = new Ellipse2D.Double(tank.position.x, tank.position.y, tank.diameter, tank.diameter);
			if (circleIntersects(bulletHitbox, tankHitbox)) {
				tank.dead = true;
 			}
		}
	}
	
	public boolean circleIntersects(Ellipse2D.Double circle1, Ellipse2D.Double circle2)
    {
        double distanceX = circle1.getCenterX() - circle2.getCenterX();
        double distanceY = circle1.getCenterY() - circle2.getCenterY();
        double radiusSum = circle2.width/2 + circle1.width/2;
        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
    }
	
	public void setPosition(Vector2D position) {
		this.position = position;
		hitbox.x = (int) position.x;
		hitbox.y = (int) position.y;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public boolean isDead() {
		return dead;
	}
}
