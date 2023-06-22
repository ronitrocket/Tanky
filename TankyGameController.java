package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

public class TankyGameController extends KeyAdapter implements ActionListener {
	
	//Instance Variables
    private TankyGUI tankyGUI;              
    private JButton exitButton;
    
    public boolean player1Up = false;
    public boolean player1Down = false;
    public boolean player1Left = false;
    public boolean player1Right = false;
    public boolean player1Shot = false;
    
    public boolean player2Up = false;
    public boolean player2Down = false;
    public boolean player2Left = false;
    public boolean player2Right = false;
    public boolean player2Shot = false;
    
    /**
	 * Default constructor.
	 * 
	 * Creates a new GameController
	 * 
	 * @param tankyGUI		The TankyGUI view this controller is attached to
	 * @param exitButton	The exit button from the TankyGUI
	 */
    public TankyGameController(TankyGUI tankyGUI, JButton exitButton)
    {
         this.tankyGUI = tankyGUI;
         this.exitButton = exitButton;
    }

    /**
	 * Provides behaviour for the buttons 
	 * 
	 * @param e		The event that was performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		TankyGame tankyGame = tankyGUI.tankyGame;
		
		// If the button clicked was the exit button, return to main menu
		if (e.getSource() == exitButton)
        {
             tankyGame.inGame = false;
             tankyGame.openMenuFrame();
        }
	}
	
	/**
	 * Provides behaviour for when a key was pressed
	 * 
	 * @param e		The event that was performed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		TankyGame tankyGame = tankyGUI.tankyGame;
		
		// Update player 1 related variables when a key is pressed
		if (code == KeyEvent.VK_S) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Down = true;
			}
		}
		if (code == KeyEvent.VK_W) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Up = true;
			}
		}
		if (code == KeyEvent.VK_A) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Left = true;
			}
		}
		if (code == KeyEvent.VK_D) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Right = true;
			}
		}
		if (code == KeyEvent.VK_SHIFT) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				if (!player1Shot) {
					player.shoot();
					player1Shot = true;
				}
			}
		}
		
		// Update player 2 related variables when a key is pressed
		if (code == KeyEvent.VK_DOWN) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Down = true;
			}
		}
		if (code == KeyEvent.VK_UP) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Up = true;
			}
		}
		if (code == KeyEvent.VK_LEFT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Left = true;
			}
		}
		if (code == KeyEvent.VK_RIGHT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Right = true;
			}
		}
		if (code == KeyEvent.VK_SPACE) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				if (!player2Shot) {
					player.shoot();
					player2Shot = true;
				}
			}
		}
	}
	
	/**
	 * Provides behaviour for when a key was released
	 * 
	 * @param e		The event that was performed
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		TankyGame tankyGame = tankyGUI.tankyGame;
		
		// Update player 1 related variables when a key is released
		if (code == KeyEvent.VK_S) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Down = false;
			}
		}
		if (code == KeyEvent.VK_W) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Up = false;
			}
		}
		if (code == KeyEvent.VK_A) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Left = false;
			}
		}
		if (code == KeyEvent.VK_D) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Right = false;
			}
		}
		if (code == KeyEvent.VK_SHIFT) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				if (player1Shot) {
					player1Shot = false;
				}
			}
		}
		
		// Update player 2 related variables when a key is released
		if (code == KeyEvent.VK_DOWN) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Down = false;
			}
		}
		if (code == KeyEvent.VK_UP) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Up = false;
			}
		}
		if (code == KeyEvent.VK_LEFT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Left = false;
			}
		}
		if (code == KeyEvent.VK_RIGHT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Right = false;
			}
		}
		if (code == KeyEvent.VK_SPACE) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				if (player2Shot) {
					player2Shot = false;
				}
			}
		}
	}
	
	/**
	 * Reset all keys to their regular states
	 */
	public void resetKeys() {
		player1Up = false;
	    player1Down = false;
	    player1Left = false;
	    player1Right = false;
	    player1Shot = false;
	    
	    player2Up = false;
	    player2Down = false;
	    player2Left = false;
	    player2Right = false;
	    player2Shot = false;
	}
}
