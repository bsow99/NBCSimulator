package simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


/**
 * @author Babacar
 * @Overview 1)In this class we create the panel that allows us
 * to create the model of our robot
 *
 *2) it is in this class that we choose on which motor we will put our wheels
 *3) We also choose the sensors we need to run our code
 */

public class Init {
	public static final int CEL = 64;

	public Main main;
	private JPanel left, right;
	private JPanel emptyPanel;
	public static Robot robot;
	private ImageIcon[][] images;
	private ImageIcon imageRobot;
	private JIconButton[][] buttons;
	private JButton ok, ok2;
	private boolean cMotorL, cMotorR, cLightL, cLightR, cUltra;
	private boolean connectedA, connectedB, connectedC, connected1, connected2, connected3, connected4;
	private int clicksLED;
	private final int MINIMAL_CLICKS_LED_TO_OKE = 1;

	/**
	 * @author Babacar Sow
	 * @requires main != null
	 * @effects this class crate the different panels that will compose the init page
	 *
	 * @param main the main instance because main is the frame
	 */
	public Init(Main main){
		this.main = main;

		robot = new Robot(main);

		cMotorL = cMotorR = cLightL = cLightR = cUltra = true;
		connectedA = connectedB = connectedC = connected1 = connected2 = connected3 = connected4 = false;
		clicksLED = 0;

		Main.frame.getContentPane().removeAll();
		Main.frame.setSize(Main.WIDTH, Main.HEIGHT);
		Main.frame.setLayout(new GridLayout(1,2));
		setImages();
		setButtons();
		fillLeft();
		fillRight();
		fillEmptyPanel();
		activateButtons();
		Main.frame.add(left);
		Main.frame.add(right);
		Main.frame.setVisible(true);
	}

	/**
	 * @author Babacar
	 * @effects  create the image of the robot that we see at the left
	 */
	private void setImages(){
		imageRobot = new ImageIcon(this.getClass().getResource("/res/images/init/icon/robot.png"));

		images = new ImageIcon[4][4];

		//ACTUATORS: empty				SENSORS: empty
		for(int i = 0; i < 4; i++){
			try{
				String path = "/res/images/init/icon/empty1x1.5.png";
				images[0][i] = new ImageIcon(this.getClass().getResource(path));
				images[3][i] = new ImageIcon(this.getClass().getResource(path));
			}catch(NullPointerException e){
				System.out.println("Image not found: Empty (1x1.5)");
			}
		}


		//A, B, C, USB
		for(int i = 0; i < 4; i++){
			try{
				String path;
				if(i==0){
					path = "A";
				}else if(i==1){
					path = "B";
				}else if(i==2){
					path = "C";
				}else{
					path = "USB";
				}
				path = "/res/images/init/icon/" + path + ".png";
				images[1][i] = new ImageIcon(this.getClass().getResource(path));
			}catch(NullPointerException e){
				System.out.println("Image not found: A, B, C or USB");
			}
		}

		//1, 2, 3, 4
		for(int i = 0; i < 4; i++){
			try{
				String path;
				if(i==0){
					path = "1";
				}else if(i==1){
					path = "2";
				}else if(i==2){
					path = "3";
				}else{
					path = "4";
				}
				path = "/res/images/init/icon/" + path + ".png";
				images[2][i] = new ImageIcon(this.getClass().getResource(path));
			}catch(NullPointerException e){
				System.out.println("Image not found: 1, 2, 3 or 4");
			}
		}
	}

	/**
	 * @author Babacar Sow
	 * @effects create the buttons with their icon, color, text, size ...
	 *
	 */
	private void setButtons(){
		buttons = new JIconButton[2][4];

		//A, B, C, USB			1, 2, 3, 4
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 4; j++){
				JIconButton cel = new JIconButton();
				cel.setSize(1*CEL, (int) 0.5*CEL);
				cel.setBackground(Color.white);
				cel.setOpaque(true);
				cel.setIcon(images[i+1][j]);
				buttons[i][j] = cel;
			}
		}

		ok = new JButton();
		try{
			String path = "/res/images/init/icon/ok.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			ok.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: OK");
		}
		ok.setText("OK");
		ok.setFont(new Font("Helvetica", Font.PLAIN, 30));
		ok.setHorizontalAlignment(SwingConstants.CENTER);
		ok.setForeground(main.ORANGE);
		ok.setSize(10*CEL, 4*CEL);

		ok.setEnabled(clicksLED>=MINIMAL_CLICKS_LED_TO_OKE || (!cMotorL && !cMotorR));

		ok2 = new JButton();
		try{
			String path = "/res/images/init/icon/ok.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			ok2.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: OK");
		}
		ok2.setText("OK");
		ok2.setFont(new Font("Helvetica", Font.PLAIN, 30));
		ok2.setHorizontalAlignment(SwingConstants.CENTER);
		ok2.setForeground(Main.ORANGE);
		ok2.setSize(10*CEL, 4*CEL);

		ok2.setEnabled(clicksLED>=MINIMAL_CLICKS_LED_TO_OKE || (!cMotorL && !cMotorR));

	}

	/**
	 * @author Babacar Sow
	 * @effects This class create the orange panel that we see at the left of the init page
	 * this panel is used to give hint of hwo to put the actuator and the sensors in the robot
	 */

	private void fillEmptyPanel(){
		JPanel empty = new JPanel(new BorderLayout());
		empty.setSize(8*CEL, 12*CEL);
		empty.setBackground(Main.ORANGE);
		JLabel picture = new JLabel();
		picture.setFont(new Font("Helvetica", Font.PLAIN, 14));
		picture.setHorizontalAlignment(SwingConstants.CENTER);
		picture.setForeground(Color.white);
		picture.setIcon(new ImageIcon(this.getClass().getResource("/res/images/init/icon/robotlr.png")));
		empty.add(picture, BorderLayout.PAGE_START);


		//#
		String message = "<html><center><b>HINT:</b> Click on <b>A, B, C, 1, 2, 3</b> or <b>4</b> to make a connection with a sensor/actuator. "
				+ "Left and right are defined as shown at the top."
				+ "Click on <b>OK</b> if your robot is initialized. </center></html>";
		JLabel text = new JLabel();
		text.setText(message);
		text.setFont(new Font("Helvetica", Font.PLAIN, 30));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setForeground(Color.white);
		empty.add(text, BorderLayout.CENTER);
		empty.add(ok2, BorderLayout.PAGE_END);
		this.emptyPanel = empty;
	}

	/**
	 * @author Babacar
	 * @effects create the white panel who is on the left of the init page
	 * This panel represents the robot without nothing at first
	 * and if we click on button A,B,C,1,2,3 we have the possibility to put an actuator or a sensor
	 * in the robot
	 * the image is updated when we add sensor or actuator
	 */
	private void fillLeft(){
		JPanel left = new JPanel();
		left.setSize(8*CEL, 12*CEL);
		left.setBackground(Color.white);

		//main
		JPanel main = new JPanel(new BorderLayout());
		main.setSize(4*CEL, 10*CEL);
		main.setBackground(Color.white);

		//TOP
		JPanel top = new JPanel(new BorderLayout());
		top.setSize(4*CEL, 2*CEL);
		top.setBackground(Color.white);
		top.setOpaque(true);

		//actuators: connected images
		JPanel actuators = new JPanel(new GridLayout(1,4));
		actuators.setSize(4*CEL, (int) 1.5*CEL);

		for(int i = 0; i < 4; i++){
			JLabel cel = new JLabel();
			cel.setSize(1*CEL, (int) 1.5*CEL);
			cel.setIcon(images[0][i]);
			cel.setBackground(Color.white);
			cel.setOpaque(true);
			actuators.add(cel);
		}
		top.add(actuators, BorderLayout.NORTH);

		//A, B, C and USB
		JPanel abcusb = new JPanel(new GridLayout(1,4));
		abcusb.setSize(4*CEL, (int) 0.5*CEL);
		for(int i = 0; i < 4; i++){
			abcusb.add(buttons[0][i]);
		}
		top.add(abcusb, BorderLayout.SOUTH);

		main.add(top, BorderLayout.NORTH);

		//CENTER
		JLabel center = new JLabel();
		center.setSize(4*CEL, 6*CEL);
		center.setIcon(imageRobot);
		main.add(center, BorderLayout.CENTER);

		//BOTTOM
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.setSize(4*CEL, 2*CEL);
		bottom.setBackground(Color.white);
		bottom.setOpaque(true);

		//1, 2, 3 and 4
		JPanel port1234 = new JPanel(new GridLayout(1,4));
		port1234.setSize(4*CEL, (int) 0.5*CEL);
		port1234.setBackground(Color.white);
		port1234.setOpaque(true);

		for(int i = 0; i < 4; i++){
			port1234.add(buttons[1][i]);
		}
		bottom.add(port1234, BorderLayout.NORTH);

		//sensors: connected images
		JPanel sensors = new JPanel(new GridLayout(1,4));
		sensors.setSize(4*CEL, (int) 1.5*CEL);

		for(int i = 0; i < 4; i++){
			JLabel cel = new JLabel();
			cel.setSize(1*CEL,(int) 1.5*CEL);
			cel.setIcon(images[3][i]);
			cel.setBackground(Color.white);
			cel.setOpaque(true);
			sensors.add(cel);
		}
		bottom.add(sensors, BorderLayout.SOUTH);

		main.add(bottom, BorderLayout.SOUTH);

		left.add(main);
		this.left = left;
	}

	/**
	 * @author Babacar Sow
	 * @effects This class create the orange panel that we see at the left of the init page
	 * 	         this panel is used to give hint of hwo to put the actuator and the sensors in the robot
	 */
	private void fillRight(){

		JPanel right = new JPanel(new BorderLayout());
		right.setSize(8*CEL, 12*CEL);
		right.setBackground(Main.ORANGE);

		JLabel picture = new JLabel();

		picture.setFont(new Font("Helvetica", Font.PLAIN, 14));
		picture.setHorizontalAlignment(SwingConstants.CENTER);
		picture.setForeground(Color.white);
		picture.setIcon(new ImageIcon(this.getClass().getResource("/res/images/init/icon/robotlr.png")));
		right.add(picture, BorderLayout.PAGE_START);

		String message = "<html><center><b>HINT:</b> Click on <b>A, B, C, 1, 2, 3</b> or <b>4</b> to make a connection with a sensor/actuator. "
				+ "Left and right are defined as shown at the top."
				+ "Click on <b>OK</b> if your robot is initialized. </center></html>";
		JLabel text = new JLabel();
		text.setText(message);
		text.setFont(new Font("Helvetica", Font.PLAIN, 30));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setForeground(Color.white);
		right.add(text, BorderLayout.CENTER);
		right.add(ok, BorderLayout.PAGE_END);
		this.right = right;
	}

	/**
	 * @author Babacar Sow
	 * @param line : the line where we are at the robot image (line 1 is
	 *             for the actuators A,B... & line2 for the sensor)
	 * @param col : the column of the robot image (1,2,3,4,A,B,C)
	 */
	private void choosenImage(int line, int col){
		//##
		//A, B, C, USB
		for(int i = 0; i < 4; i++){
			try{
				String path;
				if(i==0){
					path = "A";
				}else if(i==1){
					path = "B";
				}else if(i==2){
					path = "C";
				}else{
					path = "USB";
				}
				if(line==1 && col==i){
					path += "c";
				}
				path = "/res/images/init/icon/" + path + ".png";
				images[1][i] = new ImageIcon(this.getClass().getResource(path));
			}catch(NullPointerException e){
				System.out.println("Image not found: A, B, C or USB");
			}
		}

		//1, 2, 3, 4
		for(int i = 0; i < 4; i++){
			try{
				String path;
				if(i==0){
					path = "1";
				}else if(i==1){
					path = "2";
				}else if(i==2){
					path = "3";
				}else{
					path = "4";
				}
				if(line==2 && col==i){
					path += "c";
				}

				path = "/res/images/init/icon/" + path + ".png";
				images[2][i] = new ImageIcon(this.getClass().getResource(path));
			}catch(NullPointerException e){
				System.out.println("Image not found: 1, 2, 3 or 4");
			}
		}

		//A, B, C, USB			1, 2, 3, 4
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 4; j++){
				JIconButton cel = new JIconButton();
				cel.setSize(1*CEL, (int) 0.5*CEL);
				cel.setBackground(Color.white);
				cel.setOpaque(true);
				cel.setIcon(images[i+1][j]);
				buttons[i][j] = cel;
			}
		}
		fillLeft();
	}

	/**
	 * @author Babacar Sow
	 * @effects make the action performed when we click on a button in the init page
	 */
	private void activateButtons(){
		buttons[0][0].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on buttonA");

			//#####
			choosenImage(1,0);


			selectedActuator("A");

		});

		buttons[0][1].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on buttonB");
			choosenImage(1,1);

			selectedActuator("B");
		});

		buttons[0][2].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on buttonC");
			choosenImage(1,2);

			selectedActuator("C");
		});



		buttons[1][0].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on button1");
			choosenImage(2,0);

			selectSensor("1");
		});

		buttons[1][1].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on button2");
			choosenImage(2,1);

			selectSensor("2");
		});

		buttons[1][2].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on button3");
			choosenImage(2,2);

			selectSensor("3");
		});

		buttons[1][3].addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on button4");
			choosenImage(2,3);

			selectSensor("4");
		});

		ok.addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on ok");
			quit();
		});

		ok2.addActionListener(e -> {
			//Execute when button is pressed
			System.out.println("Init:\tClick on ok2");
			quit();
		});

	}

	/**
	 * @author Babacar
	 * This function allow us to go in the simulator page
	 */
	private void quit(){
		Main.init = this;
		Main.simulator = new Simulator(main);
	}

	/**
	 * @author Babacar
	 * @param s the name of actuator or the sensor we want to deactivate
	 * @param button the sensor or the actuator button
	 * @effects allows us to disconnect a sensor or an actuator to put it elsewhere
	 */
	private void disconnect(String s, String button){
		switch (s) {
			case "A":
				connectedA = false;
				break;
			case "B":
				connectedB = false;
				break;
			case "C":
				connectedC = false;
				break;
			case "1":
				connected1 = false;
				break;
			case "2":
				connected2 = false;
				break;
			case "3":
				connected3 = false;
				break;
			case "4":
				connected4 = false;
				break;
			default:
				assert false;
		}


		String element = "/res/images/init/connected/led.png";
		if(button.contains(element)){
			clicksLED--;
		}

		element = "/res/init/connected/wheelL.png";
		if(button.contains(element)){
			cMotorL = true;
		}

		element = "/res/init/connected/wheelR.png";
		if(button.contains(element)){
			cMotorR = true;
		}

		element = "/res/images/init/connected/lightL.png";
		if(button.contains(element)){
			cLightL = true;
		}

		element = "/res/images/init/connected/lightR.png";
		if(button.contains(element)){
			cLightR = true;
		}


		element = "/res/images/init/connected/ultrasonic.png";
		if(button.contains(element)){
			cUltra = true;
		}


		//printConnections();
		ok.setEnabled(clicksLED>=MINIMAL_CLICKS_LED_TO_OKE || (!cMotorL && !cMotorR));
		ok2.setEnabled(clicksLED>=MINIMAL_CLICKS_LED_TO_OKE || (!cMotorL && !cMotorR));



	}

	/**
	 * @author Babacar
	 * @param s the name of actuator or the sensor we want to activate
	 * @param choice the sensor or the actuator button
	 * @effects allows us to connect a sensor or an actuator
	 */
	private void connect(String s, String choice){
		if(s.equals("A")){
			connectedA = true;
		}else if(s.equals("B")){
			connectedB = true;
		}else if(s.equals("C")){
			connectedC = true;
		}else if(s.equals("1")){
			connected1 = true;
		}else if(s.equals("2")){
			connected2 = true;
		}else if(s.equals("3")){
			connected3 = true;
		}else if(s.equals("4")){
			connected4 = true;
		}


		if(choice.equals("motorR")){
			cMotorR = false;
		}

		if(choice.equals("motorL")){
			cMotorL = false;
		}

		if(choice.equals("LED")){
			clicksLED++;
		}

		if(choice.equals("lightL")){
			cLightL = false;
		}

		if(choice.equals("lightR")){
			cLightR = false;
		}

		if(choice.equals("ultra")){
			cUltra = false;
		}

		//printConnections();
		ok.setEnabled(clicksLED>=MINIMAL_CLICKS_LED_TO_OKE || (!cMotorL && !cMotorR));
		ok2.setEnabled(clicksLED>=MINIMAL_CLICKS_LED_TO_OKE || (!cMotorL && !cMotorR));

	}


	private void selectedActuator(final String ABC){
		Color bg = Main.ORANGE;

		JPanel main = new JPanel(new GridLayout(4, 1));
		main.setBackground(bg);

		//motor right
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(bg);
		JButton choice1 = new JButton();
		String text1 = "<html><center>MOTOR (RIGHT)</center></html>";
		choice1.setText(text1);
		choice1.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/wheelR.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice1.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: motorR");
		}
		choice1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on motor (right)");

				choiceMotorR(ABC);
				updateRightEmpty();

			}
		});


		choice1.setEnabled(cMotorR && clicksLED==0);
		top.add(choice1, BorderLayout.CENTER);
		main.add(top);


		//motor left
		JPanel middle = new JPanel(new BorderLayout());
		middle.setBackground(bg);
		JButton choice2 = new JButton();
		String text2 = "<html><center>MOTOR (LEFT)</center></html>";
		choice2.setText(text2);
		choice2.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/wheelL.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice2.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: motorL");
		}
		choice2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on motor (left)");
				choiceMotorL(ABC);
				updateRightEmpty();

			}
		});
		choice2.setEnabled(cMotorL && clicksLED==0);
		middle.add(choice2, BorderLayout.CENTER);
		main.add(middle);





		//LED
		JPanel bottom = new JPanel(new BorderLayout());
		bottom.setBackground(bg);
		JButton choice3 = new JButton();
		String text3 = "<html><center>LED</center></html>";
		choice3.setText(text3);
		choice3.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/led.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice3.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: LED");
		}
		choice3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on LED");
				choiceLED(ABC);
				updateRightEmpty();
			}
		});
		choice3.setEnabled(cMotorL && cMotorR);
		bottom.add(choice3, BorderLayout.CENTER);
		main.add(bottom);

		//delete
		JPanel bottom2 = new JPanel(new BorderLayout());
		bottom2.setBackground(bg);
		JButton choice4 = new JButton();
		String text4 = "<html><center>DELETE</center></html>";
		choice4.setText(text4);
		choice4.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/delete.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice4.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: Delete");
		}
		choice4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on delete");
				choiceDelete(ABC);
				updateRightEmpty();
			}
		});
		bottom2.add(choice4, BorderLayout.CENTER);
		main.add(bottom2);

		//update right
		JPanel right = new JPanel(new BorderLayout());
		right.setSize(8*CEL, 12*CEL);
		right.setBackground(Color.blue);
		right.setOpaque(true);

		right.add(main);
		updateRight(main);
	}

	private void choiceMotorR(String ABC){
		//###
		choosenImage(0,0);


		connect(ABC, "motorR");

		updateRightEmpty();

		if(ABC.equals("A")){
			disconnect(ABC, images[0][0].toString());
			images[0][0] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/wheelR.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setA(new MotorR(robot));


		}else if(ABC.equals("B")){
			disconnect(ABC, images[0][1].toString());
			images[0][1] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/wheelR.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setB(new MotorR(robot));

		}else if(ABC.equals("C")){
			disconnect(ABC, images[0][2].toString());
			images[0][2] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/wheelR.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setC(new MotorR(robot));

		}else{
			System.out.println("ERROR: Onbekende actuator-input (geen A, B of C)");
		}

	}


	private void choiceMotorL(String ABC){
		//###
		choosenImage(0,0);


		connect(ABC, "motorL");

		updateRightEmpty();

		if(ABC.equals("A")){
			disconnect(ABC, images[0][0].toString());
			images[0][0] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/wheelL.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setA(new MotorL(robot));


		}else if(ABC.equals("B")){
			disconnect(ABC, images[0][1].toString());
			images[0][1] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/wheelL.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setB(new MotorL(robot));

		}else if(ABC.equals("C")){
			disconnect(ABC, images[0][2].toString());
			images[0][2] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/wheelL.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setC(new MotorL(robot));

		}else{
			System.out.println("ERROR: Onbekende actuator-input (geen A, B of C)");
		}

	}


	private void choiceLED(String ABC){

		//###
		choosenImage(0,0);


		connect(ABC, "LED");

		updateRightEmpty();

		updateRight(emptyPanel);
		if(ABC.equals("A")){
			disconnect(ABC, images[0][0].toString());
			images[0][0] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/led.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setA(new LED(robot));

		}else if(ABC.equals("B")){
			disconnect(ABC, images[0][1].toString());
			images[0][1] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/led.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setB(new LED(robot));

		}else if(ABC.equals("C")){
			disconnect(ABC, images[0][2].toString());
			images[0][2] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/led.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setC(new LED(robot));

		}else{
			System.out.println("ERROR: Onbekende actuator-input (geen A, B of C)");
		}
	}

	private void choiceDelete(String ABC){
		//###
		choosenImage(0,0);

		updateRightEmpty();

		updateRight(emptyPanel);
		if(ABC.equals("A")){
			disconnect(ABC, images[0][0].toString());

			images[0][0] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setA(null);

		}else if(ABC.equals("B")){
			disconnect(ABC, images[0][1].toString());


			images[0][1] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setB(null);

		}else if(ABC.equals("C")){
			disconnect(ABC, images[0][2].toString());

			images[0][2] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.setC(null);

		}else if(ABC.equals("1")){
			disconnect(ABC, images[3][0].toString());


			images[3][0] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.set1(null);

		}else if(ABC.equals("2")){
			disconnect(ABC, images[3][1].toString());

			images[3][1] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.set2(null);

		}else if(ABC.equals("3")){
			disconnect(ABC, images[3][2].toString());


			images[3][2] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.set3(null);

		}else if(ABC.equals("4")){
			disconnect(ABC, images[3][3].toString());


			images[3][3] = new ImageIcon(this.getClass().getResource("/res/images/init/icon/empty1x1.5.png"));
			fillLeft();
			fillRight();
			activateButtons();
			main.frame.add(left);
			main.frame.add(right);
			main.frame.setVisible(true);

			robot.set4(null);

		}else{
			System.out.println("ERROR: Onbekende actuator-input (geen A, B, C, 1, 2, 3 of 4)");
		}
	}

	private void updateRight(JPanel right){
		this.right = right;
		main.frame.getContentPane().removeAll();
		main.frame.getContentPane().add(this.left);
		main.frame.getContentPane().add(right);
		main.frame.setVisible(true);
	}

	private void updateRightEmpty(){
		this.right = emptyPanel;
		Main.frame.getContentPane().removeAll();
		Main.frame.getContentPane().add(this.left);
		Main.frame.getContentPane().add(emptyPanel);
		Main.frame.setVisible(true);
	}


	/**
	 *
	 * @param s
	 */
	private void selectSensor(final String s){
		Color bg = Main.ORANGE;
		JPanel main = new JPanel(new GridLayout(4, 1));
		main.setBackground(bg);
		//light (right)
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.setBackground(bg);
		JButton choice1 = new JButton();
		String text1 = "<html><center>LIGHT SENSOR (RIGHT)</center></html>";
		choice1.setText(text1);
		choice1.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/lightR.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice1.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: lightR");
		}
		choice1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on lightR");
				choiceLightR(s);
				updateRightEmpty();
			}
		});

		choice1.setEnabled(cLightR);
		panel1.add(choice1, BorderLayout.CENTER);
		main.add(panel1);

		//light (left)
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.setBackground(bg);
		JButton choice2 = new JButton();
		String text2 = "<html><center>LIGHT SENSOR (LEFT)</center></html>";
		choice2.setText(text2);
		choice2.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/lightL.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice2.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: lightL");
		}
		choice2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on lightL");
				choiceLightL(s);
				updateRightEmpty();
			}
		});
		choice2.setEnabled(cLightL);
		panel2.add(choice2, BorderLayout.CENTER);
		main.add(panel2);
		//ultrasonic
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.setBackground(bg);
		JButton choice3 = new JButton();
		String text3 = "<html><center>ULTRASONIC SENSOR</center></html>";
		choice3.setText(text3);
		choice3.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/ultrasonic.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice3.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: ultrasonic");
		}
		choice3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on ultrasonic");
				choiceUltrasonic(s);
				updateRightEmpty();
			}
		});
		choice3.setEnabled(cUltra);
		panel3.add(choice3, BorderLayout.CENTER);
		main.add(panel3);
		//delete
		JPanel panel4 = new JPanel(new BorderLayout());
		panel4.setBackground(bg);
		JButton choice4 = new JButton();
		String text4 = "<html><center>DELETE</center></html>";
		choice4.setText(text4);
		choice4.setFont(new Font("Helvetica", Font.BOLD, 36));
		try{
			String path = "/res/images/init/icon/delete.png";
			ImageIcon image = new ImageIcon(this.getClass().getResource(path));
			choice4.setIcon(image);
		}catch(NullPointerException e){
			System.out.println("Image not found: Delete");
		}
		choice4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Execute when button is pressed
				System.out.println("Init:\t\tClick on delete");
				choiceDelete(s);
				updateRightEmpty();
			}
		});
		panel4.add(choice4, BorderLayout.CENTER);
		main.add(panel4);

		//update right
		JPanel right = new JPanel(new BorderLayout());
		right.setSize(8*CEL, 12*CEL);
		right.setBackground(Color.blue);
		right.setOpaque(true);

		right.add(main);
		updateRight(main);
	}

	/**
	 * @author Babacar
	 * @param s the ultrasonic sensor
	 */
	private void choiceUltrasonic(String s){
		//###
		choosenImage(0,0);

		connect(s, "ultra");

		updateRightEmpty();

		updateRight(emptyPanel);
		switch (s) {
			case "1":
				disconnect(s, images[3][0].toString());

				images[3][0] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/ultrasonic.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set1(new Ultrasonic(robot));

				break;
			case "2":
				disconnect(s, images[3][1].toString());

				images[3][1] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/ultrasonic.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set2(new Ultrasonic(robot));

				break;
			case "3":
				disconnect(s, images[3][2].toString());

				images[3][2] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/ultrasonic.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set3(new Ultrasonic(robot));

				break;
			case "4":
				disconnect(s, images[3][3].toString());

				images[3][3] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/ultrasonic.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set4(new Ultrasonic(robot));

				break;
			default:
				System.out.println("ERROR: Onbekende actuator-input (geen 1, 2, 3 of 4)");
				break;
		}
	}


	/**
	 * @author Babacar
	 * @param s The sensor LigthR
	 */
	private void choiceLightR(String s){
		//###
		choosenImage(0,0);

		connect(s, "lightR");

		updateRightEmpty();

		updateRight(emptyPanel);
		switch (s) {
			case "1":
				disconnect(s, images[3][0].toString());

				images[3][0] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightR.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set1(new LightR(robot));

				break;
			case "2":
				disconnect(s, images[3][1].toString());

				images[3][1] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightR.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set2(new LightR(robot));

				break;
			case "3":
				disconnect(s, images[3][2].toString());

				images[3][2] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightR.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set3(new LightR(robot));

				break;
			case "4":
				disconnect(s, images[3][3].toString());

				images[3][3] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightR.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set4(new LightR(robot));

				break;
			default:
				System.out.println("ERROR: Onbekende actuator-input (geen 1, 2, 3 of 4)");
				break;
		}
	}


	/**
	 * @author Babacar
	 * @param s the sensor
	 */
	private void choiceLightL(String s){
		//###
		choosenImage(0,0);


		connect(s, "lightL");
		updateRightEmpty();

		updateRight(emptyPanel);
		switch (s) {
			case "1":
				disconnect(s, images[3][0].toString());

				images[3][0] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightL.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set1(new LightL(robot));

				break;
			case "2":
				disconnect(s, images[3][1].toString());

				images[3][1] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightL.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set2(new LightL(robot));

				break;
			case "3":
				disconnect(s, images[3][2].toString());

				images[3][2] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightL.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);

				robot.set3(new LightL(robot));

				break;
			case "4":
				disconnect(s, images[3][3].toString());

				images[3][3] = new ImageIcon(this.getClass().getResource("/res/images/init/connected/lightL.png"));
				fillLeft();
				fillRight();
				activateButtons();
				Main.frame.add(left);
				Main.frame.add(right);
				Main.frame.setVisible(true);
				robot.set4(new LightL(robot));
				break;
			default:
				System.out.println("ERROR: Unknown actuator input (no 1, 2, 3 or 4)");
				break;
		}
	}


	public Robot getRobot(){
		return robot;
	}

	private static final class JIconButton extends JButton{
		private static final long serialVersionUID = 7274140930080397481L;

		public JIconButton(){
			super(UIManager.getIcon("OptionPane.informationIcon"));
			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorder(BorderFactory.createEmptyBorder());

		}
	}

	/**
	 * @author Babacar
	 * @effects true if we have connected the led false otherwise
	 * @return true or false
	 */
	public boolean hasLED(){
		for(int i=0; i<3; i++){
			//System.out.println("Character.toChars(97+i)[0] = " + Character.toChars(97+i)[0]);
			Actuator a = robot.getActuator(Character.toChars(97+i)[0]);
			if(a!=null && a.getType().equals("LED")){
				return true;
			}
		}
		return false;
	}

}
