package simulator;

/*
 * @Overview
 * This class is used to create the panel where the user will see
 * the code that he has imported
 */

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CodePanel extends JTextPane{
	
	public final int COLUMN = 5, LINE = 6;
	public final int CEL = 64, WIDTH = COLUMN*CEL, HEIGHT = LINE*CEL;
	
	public Main main;
	
	public CodePanel(final Main main){
		this.main = main;
		init();
		editText();

		//When we click on this panel that opens the file panel to permit us
		// to choose a file
		addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("CodePanel:\tmouseClicked\t" + e.getX() + "," + e.getY());
			Simulator.code = new FilePanel(main).getCode();
			Main.simulator.refresh();
            }
            
        });
	}

	/**
	 * @author : Babacar Sow
	 * This function create the panel where we write the code after
	 * the file's import
	 */
	
	private void init(){
		removeAll();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		Font font = new Font("Courier", Font.PLAIN, 15);
		setFont(font);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setBackground(Color.black);
		setForeground(Color.white);
		setEditable(true);
		setVisible(true);
	}

	/**
	 * @author : Babacar Sow
	 * @require nothing
	 * This function is used to write the code inside the code panel but also
	 * to tell us to import a file
	 * if we import a file we print it in this panel (the else part)
	 * @version 2
	 */
	private void editText(){
		if(Simulator.code == null){
			String text = 
					"\n" +
					"\t\tCODE\n" +
					"\n";
			text += "\tNO NBC-FILE IMPORTED YET.\n" +
					"\tCLICK ON FILE > IMPORT NBC-FILE.";
			setText(text);
		}else{
			String text = 
					"\n" +
					"\t\tCODE\n" +
					"\n";
			text += Simulator.code.toString();
			setText(text);
		}
	}

	/**
	 * @author : Babacar Sow
	 * We use this function to draw the code panel and to write the code into it when have an update
	 */
	public void paint(){
		init();//place the panel
		editText();// show the text in the panel
	}
}
