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
    
    public boolean player2Up = false;
    public boolean player2Down = false;
    public boolean player2Left = false;
    public boolean player2Right = false;
    
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
             tankyGUI.inGame = false;
             tankyGUI.mainFrame.setSize(tankyGame.X_SIZE_MENU, tankyGame.Y_SIZE_MENU); 
             tankyGUI.mainFrame.setLocationRelativeTo(null);  
             tankyGUI.gamePanel.setVisible(false);
             tankyGUI.menuPanel.setVisible(true);
             tankyGame.resetGame();
        }
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		Random random = new Random();
		
		TankyGame tankyGame = tankyGUI.tankyGame;
		
		if (code == KeyEvent.VK_B) {
			tankyGame.spawnBullet(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), new Vector2D(random.nextDouble()*10-5, random.nextDouble()*10-5), .5);
			tankyGame.spawnBullet(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), new Vector2D(random.nextDouble()*10-5, random.nextDouble()*10-5), .5);
			tankyGame.spawnBullet(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), new Vector2D(random.nextDouble()*10-5, random.nextDouble()*10-5), .5);
			tankyGame.spawnBullet(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), new Vector2D(random.nextDouble()*10-5, random.nextDouble()*10-5), .5);
			
		}
		
		if (code == KeyEvent.VK_T) {
			//tankyGame.spawnTank(new Vector2D(random.nextInt(tankyGame.X_SIZE_GAME), random.nextInt(tankyGame.Y_SIZE_GAME)), random.nextDouble()*360, .5);
		}
		
		if (code == KeyEvent.VK_S) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Down = true;
				//player.moveDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_W) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Up = true;
				//player.moveDir = 1;
			}
		}
		
		if (code == KeyEvent.VK_A) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Left = true;
				//player.turnDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_D) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Right = true;
				//player.turnDir = 1;
			}
		}
		
		if (code == KeyEvent.VK_DOWN) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Down = true;
				//player.moveDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_UP) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Up = true;
				//player.moveDir = 1;
			}
		}
		
		if (code == KeyEvent.VK_LEFT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Left = true;
				//player.turnDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_RIGHT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Right = true;
				//player.turnDir = 1;
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
				//player.moveDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_W) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Up = false;
				//player.moveDir = 1;
			}
		}
		
		if (code == KeyEvent.VK_A) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Left = false;
				//player.turnDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_D) {
			Player player = tankyGame.getPlayer1();
			if (player != null) {
				player1Right = false;
				//player.turnDir = 1;
			}
		}
		
		if (code == KeyEvent.VK_DOWN) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Down = false;
				//player.moveDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_UP) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Up = false;
				//player.moveDir = 1;
			}
		}
		
		if (code == KeyEvent.VK_LEFT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Left = false;
				//player.turnDir = -1;
			}
		}
		
		if (code == KeyEvent.VK_RIGHT) {
			Player player = tankyGame.getPlayer2();
			if (player != null) {
				player2Right = false;
				//player.turnDir = 1;
			}
		}
	}
}
