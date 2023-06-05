package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

import javax.swing.JButton;

public class TankyGameController extends KeyAdapter implements ActionListener {
	
	
    private TankyGUI tankyGUI;              
    private JButton exitButton;
    
    public TankyGameController(TankyGUI tankyGUI, JButton exitButton)
    {
         this.tankyGUI = tankyGUI;
         this.exitButton = exitButton;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitButton)
        {
             tankyGUI.inGame = false;
             tankyGUI.gamePanel.setVisible(false);
             tankyGUI.menuPanel.setVisible(true);
        }
	}

}
