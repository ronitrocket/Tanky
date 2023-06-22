package Tanky;

import java.util.ArrayList;
import java.util.Random;

//Maze
//Final Programming Assignment - Tanky
/**
* Class that represents a Maze, and handles all creation of the maze and generation
* 
* @author ronittaleti
*/
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class Maze {

	// Instance Variables
	private int[][] maze;
	private Random randomGen;
	
	/**
	 * Default constructor.
	 * 
	 * Creates a new maze and initializes it
	 * 
	 * @param xSize		The size of the maze on the x-axis
	 * @param ySize		The size of the maze on the y-axis
	 */
	public Maze(int xSize, int ySize) {
		randomGen = new Random();
		initializeMaze(xSize, ySize);
	}

	/**
	 * Initializes the maze and generates it randomly
	 * 
	 * @param xSize		The size of the maze on the x-axis
	 * @param ySize		The size of the maze on the y-axis
	 */
	public void initializeMaze(int xSize, int ySize) {
		// Initialize the maze where 0 represents empty spaces and 1 represents a wall
		maze = new int[ySize][xSize];
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				if (i % 2 == 0 || j % 2 == 0) {
					maze[i][j] = 1;
				} else {
					maze[i][j] = 0;
				}
			}
		}
		
		// Print maze to console before generation
		System.out.println(this + "\n\n\n\n\n");
		
		// Generate the maze
		generateMaze(1,1,maze,3);
		
		// Replace all cells with a 2 (denotes visited cells by the algorithm) with a 0
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				if (maze[i][j] == 2) {
					maze[i][j] = 0;
				}
			}
		}
		
		// Print maze to console after generation
		System.out.println(this + "\n\n\n\n\n");
	}
	
	/**
	 * Generates the maze
	 * 
	 * @param cx		The cell the generation algorithm starts at (x-axis)
	 * @param cy		The cell the generation algorithm starts at (y-axis)
	 * @param maze		The maze as a 2D integer array
	 * @param dir		The starting direction (represented by an int) (1 - Up, 2 - Right, 3 - Down, 4 - Left)
	 */
	private void generateMaze(int cx, int cy, int[][] maze, int dir) {

		// Mark the initial cell as visited
		maze[cy][cx] = 2;

		// Get the valid moves from the current position
		ArrayList<Integer> validMoves = validCells(maze, cx, cy);

		// If there is a valid move
		if (validMoves.size() != 0) {
			// Look at all valid moves
			while (validMoves.size() > 0) {
				// And then choose a random one of them (removing them from the validMoves list)
				int random = randomGen.nextInt(validMoves.size());
				int move = validMoves.get(random);
				validMoves.remove(random);
				int ny = cy, nx = cx; // Coordinates of the new cell we have moved to
				int my = cy, mx = cx; // The wall inbetween the new cell and the current one
				switch (move) {
					case 1:
						ny = cy-2;
						my = cy-1;
						mx = cx;
						break;
					case 2:
						nx = cx+2;
						my = cy;
						mx = cx+1;
						break;
					case 3:
						ny = cy+2;
						my = cy+1;
						mx = cx;
						break;
					case 4:
						nx = cx-2;
						my = cy;
						mx = cx-1;
						break;
				}
				// If we haven't already visited the new cell, then run the algorithm again from the new cell (runs recursively)
				if (maze[ny][nx] != 2) {
					maze[my][mx] = 2;
					generateMaze(nx,ny,maze,move);
				}
			}
			// A random chance to create extra breaks in the maze so as to not make it too closed off
			int breakWallChance = randomGen.nextInt(100);
			// A 50% chance to break a wall
			if (breakWallChance > 50) {
				validMoves = validCells(maze, cx, cy);
				// Go through all valid moves
				while (validMoves.size() > 0) {
					// Choose a random one of those moves
					int random = randomGen.nextInt(validMoves.size());
					int move = validMoves.get(random);
					validMoves.remove(random);
					if (move == dir) {
						int my = cy, mx = cx;
						switch (move) {
						case 1:
							my = cy-1;
							mx = cx;
							break;
						case 2:
							my = cy;
							mx = cx+1;
							break;
						case 3:
							my = cy+1;
							mx = cx;
							break;
						case 4:
							my = cy;
							mx = cx-1;
							break;
						}
						// If the wall isn't broken, then break the loop and destroy the wall
						if (maze[my][mx] != 2) {
							maze[my][mx] = 2;
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Gets all valid cells around the current cell as an Integer array where:
	 * (1 - Up, 2 - Right, 3 - Down, 4 - Left)
	 * 
	 * @param maze		The maze as a 2D integer array
	 * @param cx		The current cell (x-axis)
	 * @param cy		The current cell (y-axis)
	 * @param dir		The starting direction (represented by an int) (1 - Up, 2 - Right, 3 - Down, 4 - Left)
	 */
	private ArrayList<Integer> validCells(int[][] maze, int cx, int cy) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		if (cy - 2 >= 0) {
			moves.add(1);
		}
		if (cx + 2 < maze[0].length) {
			moves.add(2);
		}
		if (cy + 2 < maze.length) {
			moves.add(3);
		}
		if (cx - 2 >= 0) {
			moves.add(4);
		}

		return moves;
	}
	
	/**
	 * Returns the maze as an 2D integer array.
	 * 
	 */
	public int[][] getMaze() {
		return maze;
	}

	/**
	 * Returns the maze as a string.
	 * 
	 */
	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				if (maze[i][j] == 0) {
					string += "◻";
				} else {
					string += "◾";
				}
			}
			string += "\n";
		}
		return string;
	}
}// end of class
