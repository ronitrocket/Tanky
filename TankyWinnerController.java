package Tanky;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;

//TankyWinnerController
//Final Programming Assignment - Tanky
/**
* The controller for the winner panel
* 
* @author ronittaleti
*/
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class TankyWinnerController implements ActionListener {

	// Instance Variables
	private TankyGUI tankyGUI;
	private JButton winnerExitButton;
	private JButton winnerPlayAgainButton;
	private JButton winnerOpenFileButton;

	/**
	 * Default constructor.
	 * 
	 * Creates a new WinnerController
	 * 
	 * @param tankyGUI					The TankyGUI view this controller is attached to
	 * @param winnerExitButton			The exit button from the TankyGUI
	 * @param winnerPlayAgainButton		The play again option from the TankyGUI
	 * @param winnerOpenFileButton		The open file option from the TankyGUI
	 */
	public TankyWinnerController(TankyGUI tankyGUI, JButton winnerExitButton, JButton winnerPlayAgainButton,
			JButton winnerOpenFileButton) {
		this.tankyGUI = tankyGUI;
		this.winnerExitButton = winnerExitButton;
		this.winnerPlayAgainButton = winnerPlayAgainButton;
		this.winnerOpenFileButton = winnerOpenFileButton;
	}

	/**
	 * Provides behaviour for the buttons 
	 * 
	 * @param e		The event that was performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		TankyGame tankyGame = tankyGUI.tankyGame;
		
		// If the button clicked was the play again button, start the game again
		if (e.getSource() == winnerPlayAgainButton) {
			tankyGame.startGame();
		}

		// If the button clicked was the exit button, return to the main menu
		if (e.getSource() == winnerExitButton) {
			tankyGame.openMenuFrame();
		}

		// If the button clicked was the open file button, open the file containing the data for the last gameplay session
		if (e.getSource() == winnerOpenFileButton) {
			File file = new File(tankyGame.timeOfRoundStart);
			if (!Desktop.isDesktopSupported())
			{
				System.out.println("not supported");
				return;
			}
			Desktop desktop = Desktop.getDesktop();
			if (file.exists()) {
				try {
					desktop.open(file);
				} catch (Exception exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
		}
	}
}// end of class
