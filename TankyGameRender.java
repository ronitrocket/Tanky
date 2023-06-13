package Tanky;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

public class TankyGameRender extends JPanel {
	
	TankyGame tankyGame;
	
	int wallThickness = 5;
	
	public final boolean showHitboxes = true;
	
	public TankyGameRender(TankyGame tankyGame) {
		this.tankyGame = tankyGame;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.gray.darker());
		ArrayList<Rectangle> mazeWalls = tankyGame.getMazeWalls();
		for (Rectangle rect: mazeWalls) {
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
		
		g.setColor(Color.black);
		ArrayList<Bullet> bullets = tankyGame.getBullets();
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			g.fillOval(bullet.getHitbox().x, bullet.getHitbox().y, bullet.getHitbox().width, bullet.getHitbox().height);
		}
		
		ArrayList<Tank> tanks = tankyGame.getTanks();
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			g.setColor(tank.color);
			g.fillOval(tank.getHitbox().x, tank.getHitbox().y, tank.getHitbox().width, tank.getHitbox().height);
			
			g.setColor(tank.color.darker());
			g.fillOval(tank.getHitbox().x+tank.getHitbox().width/2-tank.getHitbox().width/4, tank.getHitbox().y+tank.getHitbox().height/2-tank.getHitbox().height/4, tank.getHitbox().width/2, tank.getHitbox().height/2);
			g.fillPolygon(getBarrelShape(tank));
		}
		
		if (showHitboxes) {
			g.setColor(Color.red);
			for (Rectangle rect: mazeWalls) {
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
			}
			
			for (int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				g.drawOval(bullet.getHitbox().x, bullet.getHitbox().y, bullet.getHitbox().width, bullet.getHitbox().height);
			}
			
			for (int i = 0; i < tanks.size(); i++) {
				Tank tank = tanks.get(i);
				g.drawOval(tank.getHitbox().x, tank.getHitbox().y, tank.getHitbox().width, tank.getHitbox().height);
				if (tank instanceof Nikolai) {
					Nikolai nikolai = (Nikolai) tank;
					g.drawLine((int) nikolai.leftWhisker.x1, (int) nikolai.leftWhisker.y1, (int) nikolai.leftWhisker.x2, (int) nikolai.leftWhisker.y2);
					g.drawLine((int) nikolai.rightWhisker.x1, (int) nikolai.rightWhisker.y1, (int) nikolai.rightWhisker.x2, (int) nikolai.rightWhisker.y2);
					g.drawLine((int) nikolai.raycast.x1, (int) nikolai.raycast.y1, (int) nikolai.raycast.x2, (int) nikolai.raycast.y2);
				}
			}
		}
	}
	
	public Polygon getBarrelShape(Tank tank) {
		Polygon polygon = new Polygon();
		Vector2D center = new Vector2D(tank.getHitbox().x+tank.getHitbox().width/2, tank.getHitbox().y+tank.getHitbox().height/2);
		Vector2D point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2), tank.getHitbox().y-tank.getHitbox().height/4);
		Vector2D rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2)+(tank.getHitbox().width*.25), tank.getHitbox().y-tank.getHitbox().height/4);
		rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2)+(tank.getHitbox().width*.25), tank.getHitbox().y+tank.getHitbox().height/2);
		rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		point = new Vector2D((int) (tank.getHitbox().x+(int)(tank.getHitbox().width*.5)-(tank.getHitbox().width*.25)/2), tank.getHitbox().y+tank.getHitbox().height/2);
		rotatedPoint = rotateAroundPoint(point, center, tank.getAngle()+90);
		polygon.addPoint((int)rotatedPoint.x, (int)rotatedPoint.y);
		
		return polygon;
	}
	
	public Vector2D rotateAroundPoint(Vector2D point, Vector2D center, double angle) {
		double x1 = point.x - center.x;
		double y1 = point.y - center.y;

		double x2 = x1 * Math.cos(Math.toRadians(angle)) - y1 * Math.sin(Math.toRadians(angle));
		double y2 = x1 * Math.sin(Math.toRadians(angle)) + y1 * Math.cos(Math.toRadians(angle));

		return new Vector2D(x2 + center.x, y2 + center.y);
	}
}
