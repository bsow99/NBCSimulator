package simulator;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GraphicsData {

	public static double rotation = 0.;

	public static final int col = 9, line = 12;
	public final int CEL = 64, WIDTH = col*CEL, HEIGHT = line*CEL;

	public static int[] x = new int[col+1];
	public static int[] y = new int[line+1];
	public static int lineClicked, colClicked;
	public static Image[][] images;	
	public static String[][] imagesURL;
	public static Image robot;
	

	public static int rotationX;
	public static int rotationY;

	
	public static int pointX;
	public static int pointY;

	public static final int ROBOT_WIDTH = 68;

	public static final int POINTX_MARGIN = ROBOT_WIDTH /2;		//34

	public static final int STEP_SIZE = 5;		//10 cm is 2 px
	public static final int MINIMAL_STEP = 3;
	public static final int MOVE = 5;
	public static final double ROTATION_STEP = 8.;

	public static final double[][][] DIAGONAL_LIGHT = new double[2][3][3];	//left/right, col, line
	public static final double[][][] DEGREES_LIGHT = new double[2][3][3];	//left(-1)/right, col, line
	public int[][][] lightX;	//left/right, a/b/c/..
	public int[][][] lightY;
	public Color[][][] lightColors;
	public Color lightLeft, lightRight;
	public int lightL, lightR;		//
	public static final int WHITE = 800;
	public double[] DIAGONAL_ULTRA = new double[5];
	public double[] DEGREES_ULTRA = new double[5];

	public boolean lightIsOn;
	public boolean ultraIsOn;
	public final int ULTRA_MAX_RANGE = 255;		//entre 5 et 255 cm
	public boolean ultraSees;
	public int ultraDistance;

	public int[] ultraStartX;
	public int[] ultraStartY;

	public int[] ultraRangeX;
	public int[] ultraRangeY;

	public Main main;


	public GraphicsData(Main main){
		this.main = main;
		init();
	}

	private void init(){
		rotation = 0.;
		robot = null;

		rotationX = rotationY = -1;
		pointX = pointY = -100;

		images = new Image[line][col];
		imagesURL = new String[line][col];

		lineClicked = colClicked = -1;

		Coordinate();

		lightX = new int[2][3][3];
		lightY = new int[2][3][3];
		lightColors = new Color[2][3][3];
		lightLeft = lightRight = Color.white;
		lightL = lightR = WHITE;
		lightIsOn = true;
		lightConstants();
		
		
		ultraStartX = new int[5];
		ultraStartY = new int[5];
		ultraRangeX = new int[5];
		ultraRangeY = new int[5];
		ultraIsOn = true;
		ultraSees = false;
		ultraDistance = -1;

		
		ultraConstants();

		refreshSensors();

	}

	private void lightConstants(){
		
		double[] y = new double[3];
		y[0]=1;
		y[1]=9;
		y[2]=18;

		double[] x1 = new double[3];
		x1[0]=21;
		x1[1]=16;
		x1[2]=10;

		double[] x2 = new double[3];
		x2[0]=11;
		x2[1]=16;
		x2[2]=22;

		for(int lines=0; lines<3; lines++){
			for(int columns=0; columns<3; columns++){
				DIAGONAL_LIGHT[0][lines][columns] = Math.sqrt(Math.pow(y[lines],2) + Math.pow(x1[columns],2) );
				DEGREES_LIGHT[0][lines][columns] = 270. + -1.*Math.toDegrees( Math.atan(y[lines] / x1[columns]) );

				DIAGONAL_LIGHT[1][lines][columns] = Math.sqrt(Math.pow(y[lines],2) + Math.pow(x2[columns],2) );
				DEGREES_LIGHT[1][lines][columns] = 90. + Math.toDegrees( Math.atan(y[lines] / x2[columns]) );

				lightX[0][lines][columns] = lightY[0][lines][columns] = lightX[1][lines][columns] = lightY[1][lines][columns] = -50;
			}
		}
	}

	private void ultraConstants(){

		DIAGONAL_ULTRA[0] = 34;
		DIAGONAL_ULTRA[1] = 17;
		DIAGONAL_ULTRA[2] = 0;
		DIAGONAL_ULTRA[3] = 17;
		DIAGONAL_ULTRA[4] = 34;

		DEGREES_ULTRA[0] = -90.;
		DEGREES_ULTRA[1] = -90.;
		DEGREES_ULTRA[2] = 0;
		DEGREES_ULTRA[3] = 90.;
		DEGREES_ULTRA[4] = 90.;
	}

	private void Coordinate(){
		int x0=0, y0=0, xi=x0, yi=y0;

		for(int i = 0; i < x.length; i++){
			x[i] = xi;
			xi+=CEL;
		}

		for(int i = 0; i < y.length; i++){
			y[i] = yi;
			yi+=CEL;
		}
	}


	/**
	 * @author Babacar Sow
	 * move right
	 */
	private void moveRight(){
		double degree = 90. + rotation;

		double cos = cos(degree);
		double x = MOVE * cos;
		double sin = sin(degree);
		double y = MOVE * sin;


		pointX += round(x);
		pointY -= round(y);

	}

	/**
	 * @author Babacar Sow
	 * @Effects move the robot to the left
	 */
	private void moveLeft(){
		double degree = 270. + rotation;

		double cos = cos(degree);
		double x = MOVE * cos;
		double sin = sin(degree);
		double y = MOVE * sin;


		pointX += round(x);
		pointY -= round(y);


	}

	/**
	 * @author Babacar Sow
	 * @effects tell us in which step we are in pur movement
	 * @param left the left step movement
	 * @param right the right step movement
	 */
	public void moveStep(int left, int right){
		//System.out.println("# moveStep("+left+","+right+")");
		//left = 20, right = 10
		int step;
		if(right<left){
			step = round(right / STEP_SIZE);
		}else{
			step = round(left / STEP_SIZE);
		}
		if(left == 0 || right == 0){
			step = MINIMAL_STEP;
		}

		if(left==0){
			moveLeft();
		}

		if(right==0){
			moveRight();
		}

		System.out.println("step = "+step);

		double x = step * cos(rotation);
		double y = step * sin(rotation);
		pointX += round(x);
		pointY -= round(y);

		refreshSensors();

	}

	public void computeLight(){
		for(int lines=0; lines<3; lines++){
			for(int columns=0; columns<3; columns++){
				//left: number i
				double cos = cos(rotation + DEGREES_LIGHT[0][lines][columns]);
				double x = DIAGONAL_LIGHT[0][lines][columns] * cos;
				double sin = sin(rotation + DEGREES_LIGHT[0][lines][columns]);
				double y = DIAGONAL_LIGHT[0][lines][columns] * sin;
				lightX[0][lines][columns] = pointX + round(x);
				lightY[0][lines][columns] = pointY - round(y);

				//right: number i
				cos = cos(rotation + DEGREES_LIGHT[1][lines][columns]);
				x = DIAGONAL_LIGHT[1][lines][columns] * cos;
				sin = sin(rotation + DEGREES_LIGHT[1][lines][columns]);
				y = DIAGONAL_LIGHT[1][lines][columns] * sin;
				lightX[1][lines][columns] = pointX + round(x);
				lightY[1][lines][columns] = pointY - round(y);

			}
		}

	}

	public void computeUltraStart(){

		for(int i=0; i<5; i++){
			double cos = cos(rotation + DEGREES_ULTRA[i]);
			double x = DIAGONAL_ULTRA[i] * cos;
			double sin = sin(rotation + DEGREES_ULTRA[i]);
			double y = DIAGONAL_ULTRA[i] * sin;
			ultraStartX[i] = pointX + round(x);
			ultraStartY[i] = pointY - round(y);
		}
	}

	public void deleteLight(){
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				lightX[0][i][j] = lightY[0][i][j] = lightX[1][i][j] = lightY[1][i][j] = -1;
			}
		}
	}

	public void deleteUltra(){
		for(int i=0; i<5; i++){
			ultraStartX[i] = ultraStartY[i] = -1;
		}
	}

	private int round(double d){
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - (double) i;
		if(result<0.5){
			return d<0 ? -i : i;
		}else{
			return d<0 ? -(i+1) : i+1;
		}
	}

	private double degreeJAVAToMath(double d){
		return (90. - d);
	}

	private double cos(double d){
		return Math.cos(Math.toRadians(degreeJAVAToMath(d)));
	}

	private double sin(double d){
		return Math.sin(Math.toRadians(degreeJAVAToMath(d)));
	}

	public static double getDegree(int dif){
		dif /= 10;
		return Math.toDegrees( Math.atan( ( dif*2 ) / 68.) );
	}

	public void rotate(double r){
		rotation += r;
	}

	public boolean isTransparent(BufferedImage img, int x, int y ) {
		int pixel = img.getRGB(x,y);
		return (pixel >> 24) == 0x00;
	}

	private Color getColor(int x, int y){

		if(x<0 || y<0 || x>=WIDTH || y>=HEIGHT){
			//System.out.println("foo1");
			return Color.white;
		}

		try {
			int columns = x/CEL;
			int lines = y/CEL;
			String url = imagesURL[lines][columns];
			//System.out.println(" url = "+url);
			if(url!=null){
				//Image i = ImageIO.read(new File(url));
				int x1 = x%CEL;
				int y1 = y%CEL;
				BufferedImage i2 = ImageIO.read(this.getClass().getResource(url));


				if(isTransparent(i2, x1, y1)){
					//System.out.println("("+x+","+y+") "+"foo2");
					return Color.white;
				}

				//System.out.println("("+x+","+y+") "+"foo3");
				return new Color(i2.getRGB(x1, y1));


			}else{
				//System.out.println("("+x+","+y+") "+"foo4");
				return Color.white;
			}

		} catch (IOException e) {
			//System.out.println("GraphicsData:\tImage not found");
			e.printStackTrace();
		}

		//System.out.println("("+x+","+y+") "+"foo5");
		return null;
	}


	private int getLight(Color c){
		return round((0.21*c.getRed() + 0.72*c.getGreen() + 0.07*c.getBlue()) / 255 * 800);

	}

	private void updateLightL(){
		for(int lines=0; lines<3; lines++){
			for(int columns=0; columns<3; columns++){

				if(!lightColors[0][lines][columns].equals(Color.white)){
					lightL = getLight(lightColors[0][lines][columns]);
					return;
				}
			}
		}
	}

	private void updateLightR(){
		for(int lines=0; lines<3; lines++){
			for(int columns=0; columns<3; columns++){

				if(!lightColors[1][lines][columns].equals(Color.white)){
					lightR = getLight(lightColors[1][lines][columns]);
					return;
				}
			}
		}
	}



	public void updateLight(){

		for(int lines=0; lines<3; lines++){
			for(int columns=0; columns<3; columns++){

				lightColors[0][lines][columns] = getColor(lightX[0][lines][columns], lightY[0][lines][columns]);
				lightColors[1][lines][columns] = getColor(lightX[1][lines][columns], lightY[1][lines][columns]);


			}
		}


		lightL = lightR = WHITE;

		updateLightL();
		updateLightR();

	}


	public void computeUltraRange(){

		for(int i=0; i<5; i++){
			double cos = cos(rotation);
			double x = (ultraDistance*6.4) * cos;
			double sin = sin(rotation);
			double y = (ultraDistance*6.4) * sin;
			ultraRangeX[i] = ultraStartX[i] + round(x);
			ultraRangeY[i] = ultraStartY[i] - round(y);

		}
	}

	public boolean ultraSees(){
		for(int i = 0; i<5; i++){
			Color c = getColor(ultraRangeX[i], ultraRangeY[i]);
			//System.out.println("\t"+ultraRangeX[i]+","+ultraRangeY[i]+" = "+getRGB(c));
			if(!(Color.BLACK.equals(c) || Color.LIGHT_GRAY.equals(c) || Color.WHITE.equals(c))){
				//if(colorKubusCirkel.equals(c) || colorMuur.equals(c) || colorMuurHoek.equals(c)){
				//System.out.println("There is a object found!\tultraDistance = "+ultraDistance);
				return true;
			}
		}
		return false;
	}

	private void updateUltra(){

		ultraDistance = 0;
		computeUltraRange();

		while(ultraDistance<ULTRA_MAX_RANGE){

			if(ultraSees()){
				ultraSees = true;
				return;
			}else{
				ultraDistance++;
				computeUltraRange();
			}
		}

		ultraSees = false;
	}

	public void refreshSensors(){

		computeLight();
		computeUltraStart();
		computeUltraRange();
		updateLight();
		updateUltra();

		if(Simulator.run !=null){
			Simulator.run.valueUltra = ultraDistance;
			Simulator.run.valueLightL = lightL;
			Simulator.run.valueLightR = lightR;
		}
	}

	public void clearImages(){

		for(int i=0; i<line; i++){
			for(int j=0; j<col; j++){
				images[i][j] = null;
				imagesURL[i][j] = null;
			}
		}
		robot = null;
		Simulator.graphicsPanel.repaint();
	}
}