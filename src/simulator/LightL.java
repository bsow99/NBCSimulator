package simulator;

/**
 * @Overview
 * This class represent the left light sensor 
 */

public class LightL implements Sensor{
	
	//representation
	public Robot robot;
	
	/**
	 * Initialisation of the left light sensor
	 * @param robot : the 
	 */
	public LightL(Robot robot){
		this.robot = robot;
		GraphicsPanel.gd.lightIsOn = false;
	}
	
	/**
	 * the type of the sensor is LightL
	 */
	public String getType(){
		return "LightL";
	}

	/**
	 * Open the sensor
	 */
	
	public void open() {
		//GraphicsPanel.gd.lightIsOn = true;
	}
	
	/**
	 * close the sensor
	 */
	public void close() {
		//GraphicsPanel.gd.lightIsOn = false;
	}

	/**
	 * @return the value of the light
	 */
	public int getValue() {
		return GraphicsPanel.gd.lightL;
	}
}
