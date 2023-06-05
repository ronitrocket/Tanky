package Tanky;

import javax.swing.JFrame;

public class TankyMain {

	public static void main(String[] args) {

		// Initialize the Frame
		JFrame f = new JFrame("Tanky");
		TankyGUI mainScreen = new TankyGUI(f); // The game view
		f.setSize(mainScreen.X_SIZE_MENU, mainScreen.Y_SIZE_MENU);
		f.setResizable(true);
		f.setLocation(300, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				mainScreen.update();
				deltaTime--;
			}
		}
	}

}
