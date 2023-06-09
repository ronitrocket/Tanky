package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;

public class TankyMenuController implements ActionListener {
	
	
    private TankyGUI tankyGUI;           
    private JButton playButton;    
    private JButton exitButton;
    
    public TankyMenuController(TankyGUI tankyGUI, JButton playButton, JButton exitButton)
    {
         this.tankyGUI = tankyGUI;
         this.playButton = playButton;
         this.exitButton = exitButton;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		TankyGame tankyGame = tankyGUI.tankyGame;
		Random random = new Random();
		if (e.getSource() == playButton)
        {
             tankyGUI.inGame = true;
             tankyGame.maze = new Maze();
             tankyGame.maze.initializeMaze(tankyGame.mazeSizeX, tankyGame.mazeSizeY);
             tankyGame.generateMazeWalls(tankyGame.maze);
             tankyGame.spawnPlayer(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), random.nextDouble()*360, true);
             tankyGame.spawnPlayer(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), random.nextDouble()*360, false);
             tankyGUI.mainFrame.setSize(tankyGame.X_SIZE_GAME, tankyGame.Y_SIZE_GAME);    
             tankyGUI.mainFrame.setLocationRelativeTo(null);  
             tankyGUI.gamePanel.setVisible(true);
             tankyGUI.menuPanel.setVisible(false);
        }
		
		if (e.getSource() == exitButton)
        {
             System.exit(0);
        }
	}

}
