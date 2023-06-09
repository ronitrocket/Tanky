package Tanky;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Player extends Tank {
	
	private boolean player1 = false;
	private boolean player2 = false;

	public Player(TankyGame tankyGame, Vector2D position, double angle, double speed, boolean isPlayer1) {
		super(tankyGame, position, angle, speed);
		setPlayer1(isPlayer1);
	}

	@Override
	public void update(double deltaTime, ArrayList<Rectangle> mazeWalls) {
		
		turn();
		
		if (isPlayer1()) {
			moveDir = tankyGame.tankyGUI.gameController.player1Up ? (tankyGame.tankyGUI.gameController.player1Down ? 0 : 1) : (tankyGame.tankyGUI.gameController.player1Down ? -1 : 0);
			turnDir = tankyGame.tankyGUI.gameController.player1Right ? (tankyGame.tankyGUI.gameController.player1Left ? 0 : 1) : (tankyGame.tankyGUI.gameController.player1Left ? -1 : 0);
		} else {
			turnDir = tankyGame.tankyGUI.gameController.player2Right ? (tankyGame.tankyGUI.gameController.player2Left ? 0 : 1) : (tankyGame.tankyGUI.gameController.player2Left ? -1 : 0);
			moveDir = tankyGame.tankyGUI.gameController.player2Up ? (tankyGame.tankyGUI.gameController.player2Down ? 0 : 1) : (tankyGame.tankyGUI.gameController.player2Down ? -1 : 0);
		}
		
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
	}

	public boolean isPlayer1() {
		return player1;
	}

	public void setPlayer1(boolean player1) {
		this.player1 = player1;
		this.player2 = !player1;
	}

	public boolean isPlayer2() {
		return player2;
	}
}
