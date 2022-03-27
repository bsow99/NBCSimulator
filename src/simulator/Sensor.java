package simulator;

/**
 * @Overview this interface is used to interact with 
 * the sensors of the robot (Light or Ultrasonic)
 */
public interface Sensor {


	/**
	 * The type of the used sensor
	 * @return the name of the sensor 
	 */
	public String getType();

	/**
	 * @effects switch on the sensor
	 */
	public void open();
	/**
	 * @effects switch off the motor
	 */
	public void close();

	/**
	 * @effects tell us the value of the sensor
	 */
	public int getValue();
}
