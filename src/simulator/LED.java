package simulator;

/**
 * @Overview : This class represent the LED actuator
 */
public class LED implements Actuator{
	
	//Representation 
	private boolean power;
	Robot robot;
	
	/**
	 * @author Babacar
	 * @effects Initalise l'actionneur
	 * @param robot : Le robot auquel appartient l'actionneur
	 */
	public LED(Robot robot){
		this.robot = robot;
		this.power = false;
	}

	/**
	 * @author Babacar
	 * @effects l'actionneur est de type LED
	 */
	
	public String getType(){
		return "LED";
	}
	
	/**
	 * @author Babacar
	 * @effect activer les LED
	 */
	public void open() {
		power = true;
	}


	/**
	 * @author Babacar
	 * @effects: Eteindre les LED
	 */
	public void close() {
		power = false;
	}
	
	

	@Override
	public boolean isOn() {
		return power;
	}
	
}
