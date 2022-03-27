package simulator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Simulator{

	public static final int WIDTH = 1024, HEIGHT = 768 + 2*MenuBar.HEIGHT;

	public static MenuBar menuBar;
	public static ToolsPanel toolsPanel;
	public static DeletePanel deletePanel;
	public static GraphicsPanel graphicsPanel;
	public static CodePanel codePanel;
	public static OutputPanel outputPanel;
	public static Code code;
	public static RunNBC run;
	public static Init init;
	public Main main;

	public Simulator(Main main){
		this.main = main;
		init();
		placePanels();
	}

	/**
	 * This class allows us to place the different panel of the simumation Page
	 * it is a composition of the codePanel, ToolPanel,MenuBar and OutputPanel
	 */
	private void placePanels(){
		Main.frame.getContentPane().removeAll();
		Main.frame.setTitle("Lego Mindstorms Simulator");
		Main.frame.setSize(WIDTH, HEIGHT);
		Main.frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		Main.frame.setLayout(new BorderLayout());
		Main.frame.setBackground(Color.white);

		Main.frame.add(menuBar, BorderLayout.PAGE_START);

		JPanel main = new JPanel(new BorderLayout());

		JPanel west = new JPanel(new BorderLayout());
		west.add(toolsPanel, BorderLayout.PAGE_START);
		west.add(deletePanel, BorderLayout.PAGE_END);
		main.add(west, BorderLayout.WEST);

		main.setOpaque(false);
		main.add(graphicsPanel, BorderLayout.CENTER);

		JPanel textPanel = new JPanel(new GridLayout(2,1));
		textPanel.add(codePanel);
		textPanel.add(outputPanel);
		main.add(textPanel, BorderLayout.EAST);


		Main.frame.add(main, BorderLayout.PAGE_END);
		Main.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Main.frame.setVisible(true);
	}

	private void init(){
		menuBar = new MenuBar(this.main);

		toolsPanel = new ToolsPanel(this.main);
		deletePanel = new DeletePanel(this.main);

		graphicsPanel = new GraphicsPanel(this.main);

		codePanel = new CodePanel(this.main);
		outputPanel = new OutputPanel(this.main);

	}

	public void refresh(){
		codePanel.paint();
		menuBar.refresh();
		outputPanel.refresh();
		placePanels();
	}


	public void performStep(){
		if(!Main.init.hasLED()){
			if(Init.robot.motorLeft.isOn() || Init.robot.motorRight.isOn()){
				graphicsPanel.drive();
				GraphicsPanel.gd.refreshSensors();
			}
		}else{
			try {
				//For example, digits = "101"
				String digits = Init.robot.getDigitsLED();
				String path = "/res/images/world/led" +digits+".png";
				GraphicsData.robot = ImageIO.read(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		graphicsPanel.repaint();
		outputPanel.differentiateSensor();
	}

}