package Tanky;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Nikolai extends Tank {
	
	public Line2D.Double leftWhisker;
	public Line2D.Double rightWhisker;
	public Line2D.Double raycast;
	
	private long lastShotTime = 0;
	
	private boolean isShooting = false;

	public Nikolai(TankyGame tankyGame, Vector2D position, double angle, boolean isPlayer1) {
		super(tankyGame, position, angle);
		leftWhisker = new Line2D.Double();
		rightWhisker = new Line2D.Double();
		raycast = new Line2D.Double();
	}

	@Override
	public void update(double deltaTime, ArrayList<Rectangle> mazeWalls, ArrayList<Tank> tanks) {
		
		if (isShooting) {
			moveDir = 0;
		} else {
			moveDir = 1;
		}
		
		setColliderPos();
		
		setPosition(position.addVector(new Vector2D(Math.cos(Math.toRadians(angle)) * speed * deltaTime * moveDir, 0)));

		int debounce = 1000;
		if (isCollidingWall(mazeWalls)) {
			while (isCollidingWall(mazeWalls)) {
				setPosition(position.addVector(new Vector2D(-Math.cos(Math.toRadians(angle)) * speed * deltaTime * moveDir, 0)));
				if (debounce <= 0) {
					break;
				}
				debounce--;
			}
		}

		setPosition(position.addVector(new Vector2D(0, Math.sin(Math.toRadians(angle)) * speed * deltaTime * moveDir)));
		
		debounce = 1000;
		if (isCollidingWall(mazeWalls)) {
			while (isCollidingWall(mazeWalls)) {
				setPosition(position.addVector(new Vector2D(0, -Math.sin(Math.toRadians(angle)) * speed*deltaTime * moveDir)));
				if (debounce <= 0) {
					break;
				}
				debounce--;
			}
		}
		
		turn();
		
		double lowestDist = Double.MAX_VALUE;
		Tank closestTank = null;
		for (Tank tank : tanks) {
			if (position.dist(tank.position) < lowestDist && tank != this) {
				lowestDist = position.dist(tank.position);
				closestTank = tank;
			}
		}
		
		
		if (closestTank != null) {
			Vector2D fromDirVector = new Vector2D(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
			Vector2D toDirVector = closestTank.position.subtractVector(this.position);
			double angle = Math.toDegrees(Math.atan2(fromDirVector.x*toDirVector.y - fromDirVector.y*toDirVector.x, fromDirVector.x*toDirVector.x + fromDirVector.y*toDirVector.y));
			//System.out.println(Math.toDegrees(angle));
			
			double weight = 0;
			
			weight += (angle/180);
			
			System.out.print(weight + "    ");
			
			updateRaycast(closestTank.position);
			
			if (isLeftWhiskerColliding(mazeWalls) && isRaycastColliding(mazeWalls)) {
				weight += 2;
			} else if (isRightWhiskerColliding(mazeWalls) && isRaycastColliding(mazeWalls)) {
				weight -= 2;
			}
			
			System.out.println(weight + "    ");
			
			if (weight > 0) {
				turnDir = 1;
			} else {
				turnDir = -1;
			}
			
			if (Math.abs(angle) < 5 && !isRaycastColliding(mazeWalls)) {
				isShooting = true;
				if (System.currentTimeMillis() - lastShotTime > 1000) {
					lastShotTime = System.currentTimeMillis();
					shoot();
				}
			} else {
				isShooting = false;
			}
		}
	}
	
	public boolean isLeftWhiskerColliding(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (leftWhisker.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	public boolean isRightWhiskerColliding(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (rightWhisker.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	public boolean isRaycastColliding(ArrayList<Rectangle> mazeWalls) {
		for (Rectangle wall : mazeWalls) {
			if (raycast.intersects(wall)) {
				return true;
 			}
		}
		return false;
	}
	
	private void updateRaycast(Vector2D targetPos) {
		raycast = new Line2D.Double(position.x+hitbox.width/2, position.y+hitbox.height/2, targetPos.x+hitbox.width/2, targetPos.y+hitbox.height/2);
	}
	
	private void setColliderPos() {
		Vector2D center = new Vector2D(getHitbox().x+getHitbox().width/2, getHitbox().y+getHitbox().height/2);
		
		Vector2D point1 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)), getHitbox().y+getHitbox().height/2);
		Vector2D point2 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)-(int)(getHitbox().width*1.15)), getHitbox().y-getHitbox().height/1.5);
		Vector2D rotatedPoint1 = rotateAroundPoint(point1, center, getAngle()+90);
		Vector2D rotatedPoint2 = rotateAroundPoint(point2, center, getAngle()+90);
		
		leftWhisker = new Line2D.Double(rotatedPoint1.x, rotatedPoint1.y, rotatedPoint2.x, rotatedPoint2.y);
		
		point1 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)), getHitbox().y+getHitbox().height/2);
		point2 = new Vector2D((int) (getHitbox().x+(int)(getHitbox().width/2)+(int)(getHitbox().width*1.15)), getHitbox().y-getHitbox().height/1.5);
		rotatedPoint1 = rotateAroundPoint(point1, center, getAngle()+90);
		rotatedPoint2 = rotateAroundPoint(point2, center, getAngle()+90);
		
		rightWhisker = new Line2D.Double(rotatedPoint1.x, rotatedPoint1.y, rotatedPoint2.x, rotatedPoint2.y);
	}
	
	public Vector2D rotateAroundPoint(Vector2D point, Vector2D center, double angle) {
		double x1 = point.x - center.x;
		double y1 = point.y - center.y;

		double x2 = x1 * Math.cos(Math.toRadians(angle)) - y1 * Math.sin(Math.toRadians(angle));
		double y2 = x1 * Math.sin(Math.toRadians(angle)) + y1 * Math.cos(Math.toRadians(angle));

		return new Vector2D(x2 + center.x, y2 + center.y);
	}
}
