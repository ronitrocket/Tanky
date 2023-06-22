package Tanky;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

//TankyMenuController
//Final Programming Assignment - Tanky
/**
* The controller for the menu panel
* 
* @author ronittaleti
*/
//Created By: Ronit Taleti
//Last Modified: Jun 20th 2023
public class TankyMenuController implements ActionListener {

	// Instance Variables
	private TankyGUI tankyGUI;
	private JButton playButton;
	private JButton exitButton;
	private JCheckBox player2Option;
	private JCheckBox aiOption;
	private JCheckBox easyDiffOption;
	private JCheckBox hardDiffOption;
	private JCheckBox extremeDiffOption;
	private JTextField roundsOption;

	/**
	 * Default constructor.
	 * 
	 * Creates a new MenuController
	 * 
	 * @param tankyGUI				The TankyGUI view this controller is attached to
	 * @param playButton			The play button from the TankyGUI
	 * @param exitButton			The exit button from the TankyGUI
	 * @param player2Option			The player 2 option from the TankyGUI
	 * @param aiOption				The ai option from the TankyGUI
	 * @param easyDiffOption		The easy difficulty option from the TankyGUI
	 * @param hardDiffOption		The hard difficulty option from the TankyGUI
	 * @param extremeDiffOption		The extreme difficulty option from the TankyGUI
	 */
	public TankyMenuController(TankyGUI tankyGUI, JButton playButton, JButton exitButton, JCheckBox player2Option,
			JCheckBox aiOption, JCheckBox easyDiffOption, JCheckBox hardDiffOption, JCheckBox extremeDiffOption,
			JTextField roundOptions) {
		this.tankyGUI = tankyGUI;
		this.playButton = playButton;
		this.exitButton = exitButton;
		this.player2Option = player2Option;
		this.aiOption = aiOption;
		this.easyDiffOption = easyDiffOption;
		this.hardDiffOption = hardDiffOption;
		this.extremeDiffOption = extremeDiffOption;
		this.roundsOption = roundOptions;
	}

	/**
	 * Provides behaviour for the buttons 
	 * 
	 * @param e		The event that was performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		TankyGame tankyGame = tankyGUI.tankyGame;
		
		// If the button clicked was the play button
		if (e.getSource() == playButton) {
			// Set up the rules of the game based on the options chosen
			int numRounds = 5;
			try {
				numRounds = Integer.parseInt(roundsOption.getText());
				if (numRounds < 1) {
					numRounds = 1;
				} else if (numRounds > 10) {
					numRounds = 10;
				}
				if (numRounds % 2 == 0) {
					numRounds = numRounds - 1;
				}
				roundsOption.setText(Integer.toString(numRounds));
			} catch (NumberFormatException exception) {
				roundsOption.setText("5");
			}
			tankyGame.maxRounds = numRounds;
			tankyGame.maxBounces = easyDiffOption.isSelected() ? 1 : (hardDiffOption.isSelected() ? 2 : 4);
			tankyGame.numPlayers = player2Option.isSelected() ? 2 : 1;
			tankyGame.aiEnabled = aiOption.isSelected();
			tankyGame.startGame();
		}

		// If the button clicked was the player 2 option
		if (e.getSource() == player2Option) {
			// If neither the player 2 option or the ai option is selected, force the AI option to be selected
			if (!player2Option.isSelected() && !aiOption.isSelected()) {
				aiOption.setSelected(true);
			}
		}

		// If the button clicked was the ai option
		if (e.getSource() == aiOption) {
			// If neither the player 2 option or the ai option is selected, force the AI option to be selected
			if (!player2Option.isSelected() && !aiOption.isSelected()) {
				aiOption.setSelected(true);
			}
		}

		// If the button clicked was the easy difficulty option
		if (e.getSource() == easyDiffOption) {
			// Set all other options to be unselected
			easyDiffOption.setSelected(true);
			hardDiffOption.setSelected(false);
			extremeDiffOption.setSelected(false);
		}

		// If the button clicked was the hard difficulty option
		if (e.getSource() == hardDiffOption) {
			// Set all other options to be unselected
			easyDiffOption.setSelected(false);
			hardDiffOption.setSelected(true);
			extremeDiffOption.setSelected(false);
		}
		
		// If the button clicked was the extreme difficulty option
		if (e.getSource() == extremeDiffOption) {
			// Set all other options to be unselected
			easyDiffOption.setSelected(false);
			hardDiffOption.setSelected(false);
			extremeDiffOption.setSelected(true);
		}

		// If the button clicked was the rounds option
		if (e.getSource() == roundsOption) {
			try {
				int numRounds = Integer.parseInt(roundsOption.getText());
				if (numRounds < 1) {
					numRounds = 1;
				} else if (numRounds > 10) {
					numRounds = 10;
				}
				if (numRounds % 2 == 0) {
					numRounds = numRounds - 1;
				}
				roundsOption.setText(Integer.toString(numRounds));
			} catch (NumberFormatException exception) {
				roundsOption.setText("5");
			}
		}

		// If the button clicked was the exit button, exit the program
		if (e.getSource() == exitButton) {
			System.exit(0);
		}
	}

}// end of class
