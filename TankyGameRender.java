package Tanky;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class TankyGameRender extends JPanel {
	
	// Instance Variables
	TankyGame tankyGame;
	
	public final boolean showHitboxes = false; // Used for debugging
	
	/**
	 * Default constructor.
	 * 
	 * Creates the panel for the game for the games view
	 */
	public TankyGameRender(TankyGame tankyGame) {
		this.tankyGame = tankyGame;
	}
	
	/**
	 * Java built in paint component method
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		// Draw the maze walls
		g.setColor(Color.gray.darker());
		ArrayList<Rectangle> mazeWalls = tankyGame.getMazeWalls();
		for (Rectangle rect: mazeWalls) {
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
		
		// Draw the bullets
		ArrayList<Bullet> bullets = tankyGame.getBullets();
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			g.setColor(bullet.color.darker());
			g.fillOval(bullet.getDimension().x, bullet.getDimension().y, bullet.getDimension().width, bullet.getDimension().height);
		}
		
		// Draw the tanks
		ArrayList<Tank> tanks = tankyGame.getTanks();
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			g.setColor(tank.color);
			g.fillOval(tank.getHitbox().x, tank.getHitbox().y, tank.getHitbox().width, tank.getHitbox().height);
			
			g.setColor(tank.color.darker());
			g.fillOval(tank.getHitbox().x+tank.getHitbox().width/2-tank.getHitbox().width/4, tank.getHitbox().y+tank.getHitbox().height/2-tank.getHitbox().height/4, tank.getHitbox().width/2, tank.getHitbox().height/2);
			g.fillPolygon(getBarrelShape(tank));
		}
		
		// If we are in debug mode
		if (showHitboxes) {
			
			// Outline the maze walls
			g.setColor(Color.red);
			for (Rectangle rect: mazeWalls) {
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
			
			// Outline the bullets
			for (int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				g.drawOval(bullet.getHitbox().x, bullet.getHitbox().y, bullet.getHitbox().width, bullet.getHitbox().height);
				g.drawRect(bullet.getHitbox().x, bullet.getHitbox().y, bullet.getHitbox().width, bullet.getHitbox().height);
			}
			
			// Outline the tanks
			for (int i = 0; i < tanks.size(); i++) {
				Tank tank = tanks.get(i);
				g.drawOval(tank.getHitbox().x, tank.getHitbox().y, tank.getHitbox().width, tank.getHitbox().height);
				g.drawRect(tank.getHitbox().x, tank.getHitbox().y, tank.getHitbox().width, tank.getHitbox().height);
				// If the tank is an AI, draw additional information
				if (tank instanceof Nikolai) {
					Nikolai nikolai = (Nikolai) tank;
					
					// Draw the whiskers of the tank
					g.drawLine((int) nikolai.leftWhisker.x1, (int) nikolai.leftWhisker.y1, (int) nikolai.leftWhisker.x2, (int) nikolai.leftWhisker.y2);
					g.drawLine((int) nikolai.rightWhisker.x1, (int) nikolai.rightWhisker.y1, (int) nikolai.rightWhisker.x2, (int) nikolai.rightWhisker.y2);
				
					// Draw the lines that the tank is using to calculate shots
					g.setColor(Color.cyan.darker());
					for (int j = 0; j < nikolai.rayLines.size(); j++) {
						Line2D.Double line = nikolai.rayLines.get(j);
						g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
					}
					nikolai.rayLines = new ArrayList<Line2D.Double>();
					
					// Draw the normal that the tank is using to reflect shots
					g.setColor(Color.ORANGE.darker());
					for (int j = 0; j < nikolai.normalLines.size(); j++) {
						Line2D.Double line = nikolai.normalLines.get(j);
						g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
					}
					
					nikolai.normalLines = new ArrayList<Line2D.Double>();
				}
			}
		}
	}
	
	/**
	 * Generates a polygon that represents the tank's barrel (must be done since rotating Rectangle objects is not possible)
	 * 
	 * @param tank			The tank we are rendering
	 * 
	 * @return	polygon		The polygon representing the barrel shape
	 */
	public Polygon getBarrelShape(Tank tank) {
		Polygon polygon = new Polygon();
		Vector2D center = new Vector2D(tank.getHitbox().x+tank.getHitbox().width/2, tank.getHitbox().y+tank.getHitbox().height/2);
		
		// Point 1
		Vector2D point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2), tank.getHitbox().y-tank.getHitbox().height/4);
		Vector2D rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		
		// Point 2
		point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2)+(tank.getHitbox().width*.25), tank.getHitbox().y-tank.getHitbox().height/4);
		rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		
		// Point 3
		point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2)+(tank.getHitbox().width*.25), tank.getHitbox().y+tank.getHitbox().height/2);
		rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		
		// Point 4
		point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2), tank.getHitbox().y+tank.getHitbox().height/2);
		rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		
		return polygon;
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
}
