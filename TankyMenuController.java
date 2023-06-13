package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

public class TankyMenuController implements ActionListener {
	
	
    private TankyGUI tankyGUI;           
    private JButton playButton;    
    private JButton exitButton;
    private JCheckBox player2Option;
    
    public TankyMenuController(TankyGUI tankyGUI, JButton playButton, JButton exitButton, JCheckBox player2Option)
    {
         this.tankyGUI = tankyGUI;
         this.playButton = playButton;
         this.exitButton = exitButton;
         this.player2Option = player2Option;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		TankyGame tankyGame = tankyGUI.tankyGame;
		if (e.getSource() == playButton)
        {
             tankyGame.openGameFrame();
             tankyGame.initializeMaze();  
             tankyGame.numPlayers = player2Option.isSelected() ? 2 : 1;
             tankyGame.spawnPlayers();
             tankyGame.resetGame();
             tankyGame.inGame = true;
        }
		
		if (e.getSource() == exitButton)
        {
             System.exit(0);
        }
	}

}
