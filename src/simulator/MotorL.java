package simulator;

/**
 * @Overview This class represent the Left motor of the robot 
 * Un moteur est un objet mutable car sa vitesse change avec le temps
 * Un objet de type moteur est defini comme :
 * @specfield speed : int // la vitesse du moteur
 * @specfield power : boolean // est ce que le moteur est allumé ou éteint
 */
public class MotorL implements Actuator{
	
	//Represetation
	private int speed;
	private boolean power;
	public Robot robot;
	
	/**
	 * @author Babacar Sow
	 * @effects Initialise le moteur du robot avec une vitesse nulle et et le moteur eteint
	 * @param robot : robot auquel le moteur appartient
	 */	
	public MotorL(Robot robot){
		this.robot = robot;
		
		speed = 0;
		power = false;
	}

	/**
	 * Constructeur par copie
	 * @param speed : la vitesse du moteur
	 */
	public MotorL(int speed){
		this.speed = speed;
		power = false;
	}

	/**
	 * @author Babacar 
	 * @effects l'actionneur est de type MotorLeft
	 */
	public String getType() {
		return "MotorLeft";
	}

	/**
	 * @author babacar
	 * @effects allume le moteur
	 */
	@Override
	public void open() {
		power = true;
		
	}
	
	/**
	 * @effects eteint le moteur
	 * @author Babacar
	 */
	@Override
	public void close() {
		power = false;
		
	}
	
	/**
	 * @author Babacar
	 * @requires -100 <= speed <= 100
	 * @param speed // la vitesse du moteur
	 * @effect definit la vitesse du moteur
	 * @modifies speed
	 */
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	/**
	 * @effects give us the speed of the moteur
	 * @return the speed of the moteur
	 */
	
	public int getSpeed(){
		if(power){
			return speed;
		}else{
			return 0;
		}
	}

	/**
	 * @effects verifie si le moteur est allumé ou pas
	 */

	@Override
	public boolean isOn() {
		return power;
	}
}
