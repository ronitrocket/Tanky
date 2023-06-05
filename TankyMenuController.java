package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		if (e.getSource() == playButton)
        {
             tankyGUI.inGame = true;
             tankyGUI.gamePanel.setVisible(true);
             tankyGUI.menuPanel.setVisible(false);
        }
	}

}
