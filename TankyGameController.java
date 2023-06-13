package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;

public class TankyGameController extends KeyAdapter implements ActionListener {
	
	
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
    
    public TankyGameController(TankyGUI tankyGUI, JButton exitButton)
    {
         this.tankyGUI = tankyGUI;
         this.exitButton = exitButton;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		TankyGame tankyGame = tankyGUI.tankyGame;
		if (e.getSource() == exitButton)
        {
             tankyGame.inGame = false;
             tankyGame.openMenuFrame();
        }
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		TankyGame tankyGame = tankyGUI.tankyGame;
		
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
		
		if (code == KeyEvent.VK_Q) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				if (!player1Shot) {
					player.shoot();
					player1Shot = true;
				}
			}
		}
		
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
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		TankyGame tankyGame = tankyGUI.tankyGame;
		
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
		
		if (code == KeyEvent.VK_Q) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				if (player1Shot) {
					player1Shot = false;
				}
			}
		}
		
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
