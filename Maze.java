package Tanky;

import java.util.ArrayList;
import java.util.Random;

public class Maze {

	private int[][] maze;
	
	private Random randomGen;

	public int[][] getMaze() {
		return maze;
	}

	// 0 denotes an empty cell, 1 denotes a cell with a wall, 2 denotes a cell the
	// algorithm visited already
	public void initializeMaze(int xSize, int ySize) {
		// Initialize the maze
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
		
		System.out.println(this + "\n\n\n\n\n");
		
		randomGen= new Random();
		generateMaze(1,1,maze,3);
		
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				if (maze[i][j] == 2) {
					maze[i][j] = 0;
				}
			}
		}
	}
	
	private void generateMaze(int cx, int cy, int[][] maze, int dir) {

		maze[cy][cx] = 2;

		ArrayList<Integer> validMoves = validCells(maze, cx, cy);

		if (validMoves.size() != 0) {
			while (validMoves.size() > 0) {
				int random = randomGen.nextInt(validMoves.size());
				int move = validMoves.get(random);
				validMoves.remove(random);
				int ny = cy, nx = cx;
				int my = cy, mx = cx;
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
				if (maze[ny][nx] != 2) {
					maze[my][mx] = 2;
					generateMaze(nx,ny,maze,move);
				}
			}
			int breakWallChance = randomGen.nextInt(100);
			if (breakWallChance > 100) {
				validMoves = validCells(maze, cx, cy);
				while (validMoves.size() > 0) {
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
						if (maze[my][mx] != 2) {
							maze[my][mx] = 2;
							break;
						}
					}
				}
			}
		}
	}

	// 1 - Up, 2 - Right, 3 - Down, 4 - Left
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
}
