package simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GraphicsPanel extends JPanel{

	String res = "";
	public static GraphicsData gd ;
	public Main main;


	public GraphicsPanel(Main main){

		this.main = main;

		gd = new GraphicsData(main);

		setPreferredSize(new Dimension(gd.WIDTH,gd.HEIGHT));
		setOpaque(false);
		Clickable();


		setVisible(true);
	}

	/**
	 * @author Babacar Sow
	 * @effects allows us to make the panel clickable
	 */
	private void Clickable(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addImage(e.getX(), e.getY());
				System.out.println("GraphicsPanel:\tmouseClicked\t" + e.getX() + "," + e.getY());
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		System.out.println("paint...");
		Graphics2D g2d = (Graphics2D) g;

		//create grid
		g2d.setColor(Color.LIGHT_GRAY);
		//vertical lines
		for(int i = 0; i< GraphicsData.y.length; i++){
			g2d.drawLine(GraphicsData.x[0], GraphicsData.y[i], GraphicsData.x[GraphicsData.x.length-1], GraphicsData.y[i]);
		}
		//horizontal lines
		for(int i = 0; i< GraphicsData.x.length; i++){
			g2d.drawLine(GraphicsData.x[i], GraphicsData.y[0], GraphicsData.x[i], GraphicsData.y[GraphicsData.y.length-1]);
		}

		//draw the map 
		for(int i = 0; i< GraphicsData.line; i++){
			for(int j = 0; j< GraphicsData.col; j++){
				Image im = GraphicsData.images[i][j];
				if(im != null){
					g2d.drawImage(im, j*gd.CEL, i*gd.CEL, null);
				}
			}
		}


		//Draw the robot
		if(GraphicsData.robot!=null){
			AffineTransform backup = g2d.getTransform();
			AffineTransform trans = new AffineTransform();
			trans.rotate(Math.toRadians(GraphicsData.rotation), GraphicsData.pointX, GraphicsData.pointY); // the points to rotate around (the center in my example, your left side for your problem)
			g2d.transform(trans);
			g2d.drawImage(GraphicsData.robot, GraphicsData.pointX-GraphicsData.POINTX_MARGIN, GraphicsData.pointY, null);
			g2d.setTransform(backup); // restore previous transform
			gd.refreshSensors();

		}
	}

	/**
	 * @author Babacar SOw
	 * @param x the "abscisse" coordinate
	 * @param y the "ordonnée" coordinate
	 * @effects allow to put an image on the middle panel
	 * when we click it
	 */
	public void addImage(int x, int y){

		GraphicsData.colClicked = x/gd.CEL;
		GraphicsData.lineClicked = y/gd.CEL;

		if(ToolsPanel.isClicked){

			if(ToolsPanel.selectedURL.contains("robot")){		//robot is clicked
				String path = res+ "/res/images/world/robot1.png";

				if(ToolsPanel.selectedURL.contains("led")){
					//For example, digits = "101"
					String digits = Init.robot.getDigitsLED();
					//main.simulator.toolsPanel.selectedURL.lastIndexOf("d");
					System.out.println("digits="+digits);
					path = res+ "/res/images/world/led000.png";

				}

				try {

					GraphicsData.robot = ImageIO.read(this.getClass().getResource(path));
					GraphicsData.rotation = (Integer.parseInt(ToolsPanel.selectedURL.charAt(ToolsPanel.selectedURL.length()-5)+"")-1)*90.;
					GraphicsData.pointX = x;
					GraphicsData.pointY = y;
					gd.refreshSensors();
					System.out.println("GraphicsPanel:\trobot is placed on ("+ GraphicsData.pointX +","+ GraphicsData.pointY +") after click on ("+x+","+y+")");
				} catch (IOException e) {
					System.out.println("ERROR: Image not found:" + path);
					e.printStackTrace();
				}

			}else{
				String path = res + ToolsPanel.selectedURL;
				try {
					GraphicsData.images[GraphicsData.lineClicked][GraphicsData.colClicked] = ImageIO.read(this.getClass().getResource(path));
					GraphicsData.imagesURL[GraphicsData.lineClicked][GraphicsData.colClicked] = path;
					System.out.println("GraphicsPanel:\timages["+GraphicsData.lineClicked +"]["+GraphicsData.colClicked+"] is changed by click<" + x + "," + y+">");
				} catch (IOException e) {
					System.out.println("ERROR: Image not found:" + path);
					e.printStackTrace();
				}
			}
		}else if(DeletePanel.isClicked){
			
			if(GraphicsData.robot!=null){		//delete the robot
				int xmin = GraphicsData.pointX, xmax = GraphicsData.pointX + GraphicsData.robot.getWidth(null);
				int ymin = GraphicsData.pointY, ymax = GraphicsData.pointY + GraphicsData.robot.getHeight(null);

				if(x >= xmin && x <= xmax && y >= ymin && y <= ymax){	//clicked op robot
					GraphicsData.robot=null;
					GraphicsData.pointX = GraphicsData.pointY = -1;
					gd.deleteLight();
					gd.deleteUltra();

				}else{
					GraphicsData.images[GraphicsData.lineClicked][GraphicsData.colClicked] = null;
					GraphicsData.imagesURL[GraphicsData.lineClicked][GraphicsData.colClicked] = null;
				}
			}else{		//delete the obstacle images
				GraphicsData.images[GraphicsData.lineClicked][GraphicsData.colClicked] = null;
				GraphicsData.imagesURL[GraphicsData.lineClicked][GraphicsData.colClicked] = null;
			}
		}
	}

	/**
	 * @author Babacar Sow
	 * @effects make the robot moving
	 */
	public void drive(){
		int speedLeft = Init.robot.motorLeft.getSpeed(), speedRight = Init.robot.motorRight.getSpeed();
		if(!(speedLeft==0 && speedRight==0)){
			gd.moveStep(speedLeft, speedRight);
			double r = GraphicsData.getDegree(speedLeft-speedRight)* GraphicsData.ROTATION_STEP;
			gd.rotate(r);
		}
	}
}
