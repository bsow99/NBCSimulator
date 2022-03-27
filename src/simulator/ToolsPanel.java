package simulator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ToolsPanel extends JPanel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public final int col = 2, line = 10;
	public final int CEL = 64, WIDTH = col *CEL, HEIGHT = line *CEL;


	public static ImageIcon selected;
	public static String selectedURL;
	public static String previousURL;
	public static boolean isClicked;

	public String[] names = new String[20];

	public Main main;

	public JButton[] cells;

	public ToolsPanel(Main main){
		this.main = main;
		setLayout(new GridLayout(line, col));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Main.ORANGE);

		isClicked = false;

		initImages();
		makeCell();
		setVisible(true);
	}


	/**
	 * @author Babacar
	 *
	 */
	private void makeCell(){
		cells = new JButton[col * line];
		for(int i = 0; i < cells.length; i++){
			cells[i] = new JButton();
			cells[i].setPreferredSize(new Dimension(CEL, CEL));
			try{
				String path = "/res/images/tools/" + names[i] + ".png";
				ImageIcon image = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(path)));
				cells[i].setIcon(image);

			}catch(NullPointerException e){
				System.out.println("Image not found: names[" + i + "]");
			}
			final String tempURL = "/res/images/world/" + names[i] + ".png";
			final int j = i;
			cells[j].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					//Execute when button is pressed
					try{

						boolean deleteWasClicked = DeletePanel.isClicked;
						isClicked = true;
						DeletePanel.isClicked = false;
						//##
						previousURL = selectedURL;
						selectedURL = tempURL;
						int previousIndex = getIndex(previousURL);
						System.out.println("PreviousIndex = "+previousIndex);
						System.out.println("deleteWasClicked = "+deleteWasClicked);
						if(previousIndex!=-1 && !deleteWasClicked){
							cells[previousIndex].setBorder(new JButton().getBorder());
						}else if(deleteWasClicked){
							Simulator.deletePanel.del.setBorder(new JButton().getBorder());
						}
						cells[j].setBorder(BorderFactory.createEtchedBorder());
						selected = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(tempURL)));
					}catch(NullPointerException e2){
						System.out.println("Image not found: " + tempURL);
					}
					System.out.println("ToolsPanel:\tYou clicked cell["+j+"]\t"+tempURL);
				}

			});

			add(cells[i]);

		}

	}

	/**
	 * @author Babacar
	 * @param selectedURL the url of the image
	 * @return the index of the image
	 */
	private int getIndex(String selectedURL){
		if(selectedURL!=null){
			for(int i=0; i<20; i++){
				if(selectedURL.contains(names[i])){
					return i;
				}
			}
		}
		return -1;
	}



	private void initImages(){

		for(int i=0; i<16; i++){

			switch (i) {
				//rij 1
				case 0:  names[i] = "lijn1";
					break;
				case 1:  names[i] = "lijn2";
					break;
				case 2:  names[i] = "bocht1";
					break;
				case 3:  names[i] = "bocht2";
					break;

				//rij 2
				case 4:  names[i] = "bocht3";
					break;
				case 5:  names[i] = "bocht4";
					break;
				case 6:  names[i] = "einde1";
					break;
				case 7:  names[i] = "einde2";
					break;

				//rij 3
				case 8:  names[i] = "einde3";
					break;
				case 9:  names[i] = "einde4";
					break;
				case 10: names[i] = "kubus1";
					break;
				case 11: names[i] = "tree";
					break;

				//rij 4
				case 12: names[i] = "cirkel1";
					break;
				case 13: names[i] = "bridge";
					break;
				case 14: names[i] = "muur1";
					break;
				case 15: names[i] = "muur2";
					break;
			}
		}
		String led = "";
		if(Main.init.hasLED()){
			led = "led";
		}

		for(int i=0; i<4; i++){
			names[16+i] = "robot"+ led + (i+1);
			//names[16+i] = "robot" + (i+1);
			//names[16+i] = "robotled" + (i+1);
		}
	}
}
