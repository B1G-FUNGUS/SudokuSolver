/** 
 * A basic program to check the validity of Sudoku boardstates
 *
 * @version 0.2
 * @author B1g Fungus
 */
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import java.util.List;
public class SudokuSolver {
	public static JTextField textFieldArray[][];
	/**
	 * The main method for the program
	 * @param args command line args
	 */
	public static void main(String[] args) {
		// All this junk creates the text fields, buttons, etc. and sets them up for the user-interface
		textFieldArray = new JTextField[9][9];
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		JFrame frame = new JFrame("SudokuSolver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(475,550);
		frame.setLayout(null);
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				textFieldArray[x][y] = new JTextField();
				textFieldArray[x][y].setBounds(50*x+10, 50*y+10, x+40, y+40);
				textFieldArray[x][y].setHorizontalAlignment(JTextField.CENTER);
				textFieldArray[x][y].setFont(font1);
				frame.add(textFieldArray[x][y]);	
			}
		}	
		ButtonPress buttonPress = new ButtonPress();
		JButton checkButton = new JButton(buttonPress); 
		checkButton.setText("Check");
		checkButton.setBounds(10, 460, 80, 40);
		frame.add(checkButton); 
		ButtonPress.checkLabel.setBounds(100, 460, 400, 40);
		frame.add(ButtonPress.checkLabel);
		frame.setVisible(true);
	}
}
@SuppressWarnings("serial") //TODO: fix later
class ButtonPress extends AbstractAction {
	public static JLabel checkLabel = new JLabel("Waiting for input...");
	/**
	 * A constructor for an object to handle/listen for the button press event
	 */
	public ButtonPress() {}
	/**
	 * A method to validate the boardstate when the button is pressed
	 * @param e the action event (which should be the press of the button)
	 */
	public void actionPerformed(ActionEvent e) {
		//Clears the board, then checks if the symbols are valid and then if there are any matches. Spits back out some message to the user
		Checker.clear();
		if(!(Checker.validateSymbols())) {
			checkLabel.setText("Entries should be whole numbers 1-9");
			return;
		}
		boolean invalid = false;
		for(int a = 0; a < 9; a++) {
			invalid = Checker.superMatch(a) || invalid;

		}
		if(invalid) {
			checkLabel.setText("Invalid board state");
		} else {
			checkLabel.setText("Valid board state");
		}
	}
}
class Checker {
	// Creates an array for valid numbers, which is used to see if there are any matches where there shouldn't be.
	private static int entryArray[][] = new int[9][9];
	/**
	 * A method that makes sure all of the entries are valid numbers for sudoku
	 * @return True if all the entries are valid numbers
	 */
	public static boolean validateSymbols() {
		boolean netValid = true;
		// Attempts to parse an integer from each text field. If the entry is not an integer or is not a whole number 1-9 then the program warns the user.
		for(int x = 0; x < 9; x ++) {
			for(int y = 0; y < 9; y ++) {
				try {
					entryArray[x][y] = Integer.parseInt(SudokuSolver.textFieldArray[x][y].getText());
				} catch (NumberFormatException f) {
					entryArray[x][y] = 0;
				}
				if(!((entryArray[x][y] % 1 == 0) && (entryArray[x][y] > 0) && (entryArray[x][y] < 10))) {
					mark(x, y, Color.ORANGE);
					netValid = false;
				}
			}
		}
		return netValid;
	}
	/**
	 * A method that ensures there are no matching numbers within each row/column/square
	 * @param whichOne Which row/column/square to check. Rows go top-down, columns go left-right, and squares go left-right then top-down like you're reading an english book
	 * @return True if there IS a match
	 */
	public static boolean superMatch(int whichOne) {
		boolean netMatch = false;
		// The following 2 lines take the whichOne value and figures out which TODO
		int squareXDist = ((whichOne > 2 && whichOne < 6)?3:0) + ((whichOne > 5)?6:0);
		int squareYDist = ((whichOne % 3 == 1)?3:0) + ((whichOne % 3 == 2)?3:0);
		int sX, sY, sX1, sY1;
		// The c & d values are which text field within the row/column/square is being analyzed
		for(int c = 0; c < 9; c ++) {
			// The following 2 lines take the c value and turn it into the x/y values of a text field within the square. The order of the text fields is the same as the order that the whichOne variable goes in for the larger squares.
			sX = ((c > 2 && c < 6)?1:0) + ((c > 5)?2:0) + squareXDist;
			sY = ((c % 3 == 1)?1:0) + ((c % 3 == 2)?2:0) + squareYDist;
			for(int d = 0; d < 9; d ++) {
				sX1 = ((d > 2 && d < 6)?1:0) + ((d > 5)?2:0) + squareXDist;
				sY1 = ((d % 3 == 1)?1:0) + ((d % 3 == 2)?2:0) + squareYDist;
				if(entryArray[c][whichOne] == entryArray[d][whichOne] && !(c == d)) {
					mark(c, whichOne, Color.RED);
					mark(d, whichOne, Color.RED);
					netMatch = true;
				}
				if(entryArray[whichOne][c] == entryArray[whichOne][d] && !(c == d)) {
					mark(whichOne, c, Color.RED);
					mark(whichOne, d, Color.RED);
					netMatch = true;
				}
				if(entryArray[sX][sY] == entryArray[sX1][sY1] && !(c == d)) {
					mark(sX, sY, Color.RED);
					mark(sX1, sY1, Color.RED);
					netMatch = true;
				}
			}
		}
		return netMatch;
	}
	/**
	 * A method that makes all the text fields' text black
	 */
	public static void clear() {
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 9; y++) {
				SudokuSolver.textFieldArray[x][y].setForeground(Color.BLACK);
			}
		}	
	}
	/**
	 * A method that changes a specific text field's text a certain color
	 * @param x the x coordinate of the text field
	 * @param y the y coordinate of the text field (goes top down)
	 * @param color the color that the text should be
	 */
	private static void mark(int x, int y, Color color) {
		SudokuSolver.textFieldArray[x][y].setForeground(color);
	}
}
