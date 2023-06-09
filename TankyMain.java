package Tanky;

import javax.swing.JFrame;

public class TankyMain {

	public static void main(String[] args) {

		// Initialize the Frame
		JFrame f = new JFrame("Tanky");
		TankyGame tankyGame = new TankyGame(); // The game view
		TankyGUI mainScreen = new TankyGUI(tankyGame, f); // The game view
		tankyGame.setView(mainScreen);
		f.setSize(tankyGame.X_SIZE_MENU, tankyGame.Y_SIZE_MENU);
		f.setResizable(true);
		f.setLocation(300, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(mainScreen);
		f.setVisible(true);

		double drawInterval = 1000 / 200;
		double deltaTime = 0;
		long lastTime = System.currentTimeMillis();
		long currentTime;

		while (true) {

			currentTime = System.currentTimeMillis();

			deltaTime += (currentTime - lastTime) / drawInterval;

			lastTime = currentTime;

			if (deltaTime >= 1) {
				tankyGame.update(deltaTime);
				mainScreen.update();
				deltaTime--;
			}
		}
	}

}
