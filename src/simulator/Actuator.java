package simulator;

/**
 * @Overview: This interface is used to interact with the different  
 * actuators of the robot
 */

public interface Actuator {
	
	/**
	 * @effects : Give us the type of the actuator (LED or one of the motor)
	 * @return the name of the actuator
	 */
	public String getType();

	/**
	 * open the actuator
	 */
	public void open();
	
	/**
	 * close the actuator
	 */
	public void close();

	/**
	 * verify if the actuator is open or close
	 */
	public boolean isOn();
}
