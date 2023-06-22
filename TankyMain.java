package Tanky;

import javax.swing.JFrame;

public class TankyMain {

	public static void main(String[] args) {

		// Initialize the Frame
		JFrame f = new JFrame("Tanky");
		TankyGame tankyGame = new TankyGame(); // The game view
		TankyGUI mainScreen = new TankyGUI(tankyGame, f); // The game view
		tankyGame.setView(mainScreen);
		tankyGame.startThread();
		f.setSize(tankyGame.X_SIZE_MENU, tankyGame.Y_SIZE_MENU);
		f.setResizable(true);
		f.setLocation(300, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(mainScreen);
		f.setVisible(true);
	}

}
