package simulator;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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
		
		addMouseListener(new MouseAdapter() {
			
            @Override
            public void mouseClicked(MouseEvent e) {
            	System.out.println("CodePanel:\tmouseClicked\t" + e.getX() + "," + e.getY());

            	//quand on clique sur le panel ça ouvre le file panel afin qu'on puisse choisir un fichier

            	Simulator.code = new FilePanel(main).getCode();
	            Main.simulator.refresh();
            }
            
        });
	}

	/**
	 * This function initiate the code panel
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
	 * This function is used to write the code inside the code panel but also
	 * to tell us to import a file
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
	 * We use this function to draw the code panel and to write the code into it when have an update
	 */
	public void refresh(){
		init();//place the panel
		editText();// show the text in the panel
	}
}
