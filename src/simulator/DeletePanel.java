package simulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DeletePanel extends JPanel{

	public static boolean isClicked;
	public Main main;
	
	public JButton del;

	/**
	 * @author : Babacar Sow
	 * @require nothing
	 * @param main : the reference of the main container
	 * @version 2
	 *
	 */
	public DeletePanel(final Main main){
		this.main = main;
		setLayout(new BorderLayout());
		int CEL = 64;
		int WIDTH = 2 * CEL;
		int HEIGHT = 2 * CEL;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Main.ORANGE);
		
		isClicked = false;
		
		del = new JButton();
		del.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		try{
			String path = "/res/images/tools/delete.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			del.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("/res/images/tools/delete.png is null");
		}

		del.addActionListener(e -> {
			boolean toolsWasClicked = ToolsPanel.isClicked;
			isClicked = true;
			ToolsPanel.isClicked = false;

			ToolsPanel.previousURL = ToolsPanel.selectedURL;
			ToolsPanel.selectedURL = null;

			int indexVorige = getIndex(ToolsPanel.previousURL);

			System.out.println("indexVorige = "+indexVorige);
			System.out.println("toolsWasClicked = "+toolsWasClicked);

			if(indexVorige!=-1 && toolsWasClicked){

				Simulator.toolsPanel.cells[indexVorige].setBorder(new JButton().getBorder());
			}

			del.setBorder(BorderFactory.createLoweredBevelBorder());
			System.out.println("DeletePanel:\tYou clicked the delete button");
		});
		
		add(del, BorderLayout.CENTER );
		
		setVisible(true);
		
	}

	private int getIndex(String selectedURL){

		if(selectedURL!=null){
			for(int i=0; i<20; i++){
				if(selectedURL.contains(Simulator.toolsPanel.names[i])){
					return i;
				}
			}
		}
		return -1;
	}

}
