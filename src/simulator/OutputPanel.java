package simulator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;

public class OutputPanel extends JTextPane{
	public final int CEL = 64, WIDTH = 5*CEL, HEIGHT = 6*CEL;
	
	String text;
	
	public Main main;
	
	public String begin = 
			"\n" +
			"\t\tOUTPUT\n" +
			"\n" +
			Init.robot.toString();
	
	public String middle = "";
	public String middle2 = "";
	public String end = "";
	
	public OutputPanel(Main main){
		this.main = main;
		
		removeAll();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		
		Font font = new Font("Helvetica", Font.PLAIN, 10);
		setFont(font);
		
		text = begin + middle2 + middle + end;
		setText(text);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
		setEditable(false);
		setVisible(true);
	}

	/**
	 * @author Babacar
	 * print the code in  the output panel
	 * @param s the strin to write in the console
	 */
	public void print(String s){
		end += s;
		text = begin + middle2 + middle + end;
		setText(text);
		
	}

	
	
	public void refresh(){
		removeAll();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		
		Font font = new Font("Helvetica", Font.PLAIN, 10);
		setFont(font);
		setText(text);
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
		setEditable(false);
		setVisible(true);
		
	}
	
	public void end(){
		end = "";
	}
	
	public void differentiateSensor(){
		middle2 = "";
		
		
		middle2 += "Value of light sensor (left):\t";
		if(Init.robot.hasLightL()){
			middle2 += GraphicsPanel.gd.lightL + "\n";
		}else{
			middle2 += "-\n";
		}
		
		middle2 += "Value of light sensor (right):\t";
		if(Init.robot.hasLightR()){
			middle2 += GraphicsPanel.gd.lightR + "\n";
		}else{
			middle2 += "-\n";
		}
		
		middle2 += "Value of ultrasonic sensor:\t";
		if(Init.robot.hasUltra()){
			middle2 += GraphicsPanel.gd.ultraDistance + "\n";
		}else{
			middle2 += "-\n";
		}
		
		text = begin + middle2 + middle + end;
		setText(text);
		
	}
	
	
}
