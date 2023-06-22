package Tanky;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Player extends Tank {
	
	// Instance Variables
	private boolean player1 = false;
	private boolean player2 = false;

	/**
	 * Default constructor.
	 * 
	 * Creates a new Player with the specified parameters.
	 * 
	 * @param tankyGame		A reference to the TankyGame model.
	 * @param position		A vector representation of the tank's position.
	 * @param angle 		The angle the tank is facing.
	 * @param isPlayer1 	Whether this player is player 1 or not
	 * @param color 		The color of the tank.
	 */
	public Player(TankyGame tankyGame, Vector2D position, double angle, boolean isPlayer1, Color color) {
		super(tankyGame, position, angle, color);
		setPlayer1(isPlayer1);
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
		
		// Turn the tank based on the value of turnDir
		turn();
		
		// Link movement to the correct keys based on whether this tank is player 1 or not
		if (isPlayer1()) {
			moveDir = tankyGame.tankyGUI.gameController.player1Up ? (tankyGame.tankyGUI.gameController.player1Down ? 0 : 1) : (tankyGame.tankyGUI.gameController.player1Down ? -1 : 0);
			turnDir = tankyGame.tankyGUI.gameController.player1Right ? (tankyGame.tankyGUI.gameController.player1Left ? 0 : 1) : (tankyGame.tankyGUI.gameController.player1Left ? -1 : 0);
		} else {
			turnDir = tankyGame.tankyGUI.gameController.player2Right ? (tankyGame.tankyGUI.gameController.player2Left ? 0 : 1) : (tankyGame.tankyGUI.gameController.player2Left ? -1 : 0);
			moveDir = tankyGame.tankyGUI.gameController.player2Up ? (tankyGame.tankyGUI.gameController.player2Down ? 0 : 1) : (tankyGame.tankyGUI.gameController.player2Down ? -1 : 0);
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
		
		// Stop tracking shot bullets after they die
		for (int i = 0; i < shotBullets.size(); i++) {
			Bullet bullet = shotBullets.get(i);
			if (bullet.isDead()) {
				shotBullets.remove(bullet);
			}
		}
	}

	/**
	 * Returns whether this tank is Player 1
	 */
	public boolean isPlayer1() {
		return player1;
	}
	

	/**
	 * Returns whether this tank is Player 2
	 */
	public boolean isPlayer2() {
		return player2;
	}

	/**
	 * Sets this tank as player 1 or player 2
	 * 
	 * @param player1		Whether this tank is player 1 or not (if not, it is considered player 2)
	 */
	public void setPlayer1(boolean player1) {
		this.player1 = player1;
		this.player2 = !player1;
	}
}
