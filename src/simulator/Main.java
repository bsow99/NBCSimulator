package simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Objects;
import javax.swing.*;

/**
 * @author Babacar
 * @Overview This class is the main class
 * In this class we have the main Frame
 * where we add all the the other panels
 *
 * In this class we also have some threads which
 * allow us to do the simulation
 *
 * The instructions jmp, brtst, brcmp and wait are
 * also managed in this class
 */

public class Main {
	
	public static final int WIDTH = 1024, HEIGHT = 768 + 2*MenuBar.HEIGHT;
	public final static Color ORANGE = new Color(232, 131, 56);
	
	public static JFrame frame;
	public static Simulator simulator;
	public static Presentation accueil;
	public static Init init;
	
	public String title = "NBCUNamur - Lego Mindstorms NBC Simulator";
	public static boolean endDetected;

	/**
	 * @author Babacar
	 * Constructeur de la classe
	 */
	public Main() {
		init();
	}

	/**
	 * @author Babacar
	 * @effects initialise la fenetre principale avec ses caracteristiques
	 */
	public void init()  {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle(title);
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

		/******************************************************
		 * Pour passer les tests mettre accueil en commentaires (ligne 42)
		 * desactiver les commentaires de init (ligne 43)
		 ********************************************************/
		accueil = new Presentation(this);
		//init = new Init(this);

	}

	/**
	 * @auhor Babacar Sow
	 *
	 * @effects Fonction princiaple qui nous permet de lancer la simulation
	 * 1) lance l'execution du code si nous apppuyons sur le bouton run
	 * 2) cette fonction gere nos threads pour pouvoir lancer la simulation
	 * 3)cette fonction nous aide à pouvoir executer les instructions jmp, brtst, brcmp et wait
	 *
	 * @param args liste d'arguments
	 */
	public static void main(String[] args) {

		Runnable r1 = () -> {
			endDetected = false;

			boolean runCode = true;
			System.out.println(runCode);
			while(runCode){
				System.out.print("");

				if(simulator!=null && Simulator.menuBar.runIsClicked){
					System.out.println("###1");
					simulator.performStep();

					try {
						double seconds = 0.5;
						System.out.println("Main:\twait "+seconds+" seconds... ");
						Thread.sleep((int) (seconds*1000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.print("");
					if(Simulator.run !=null){
						System.out.println("###2");
						if(!Simulator.run.instructionSet.isEmpty()){
							Instruction a_instruc = Simulator.run.instructionSet.pop();
							try {
								if(a_instruc.getLine().contains("wait")){
									String line = a_instruc.getLine();
									line = line.replace("wait", "");
									String time = line.trim();
									int value;
									if(Variable.has(time)){
										value =Integer.parseInt(Objects.requireNonNull(Variable.getValue(time)));
									}
									else{
										value = Integer.parseInt(time);
									}

									long start = System.currentTimeMillis();
									long end = start + value;
									while (System.currentTimeMillis() < end) {
										Thread.sleep(500);
										simulator.performStep();
									}
									System.out.println(Thread.currentThread().getName());
								} else if(a_instruc.getLine().contains("brcmp")){

									String line = a_instruc.getLine();
									InstructionSet instructions_new = new InstructionSet();
									Boolean ok = Simulator.run.comparaison_toLabel(line,instructions_new);
									if (ok){
										Simulator.run.instructionSet = instructions_new;
									}
								}
								else if(a_instruc.getLine().contains("brtst")){

									// brtst GTEQ, CheckSensor, Level
									String line = a_instruc.getLine();
									line += ", 0";
									// brtst GTEQ, CheckSensor, Level, 0

									InstructionSet instructions_new = new InstructionSet();

									//System.out.println(instructions_backup.instructions.size());
									Boolean ok = Simulator.run.comparaison_toLabel(line,instructions_new);
									if (ok){
										Simulator.run.instructionSet = instructions_new;
									}

								}
								else if (a_instruc.getLine().contains("jmp")){
									String line = a_instruc.getLine();
									InstructionSet instructions_new = new InstructionSet();
									Simulator.run.jump_toLabel(line,instructions_new);
									Simulator.run.instructionSet = instructions_new;
								}
								else {
									Simulator.run.executeLine(a_instruc.getLine(),a_instruc.getNumberLine());}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

				}
			}

			System.out.println("Thread 1 is stopped.");
		};
		new Thread(r1).start();


		Runnable r2 = () -> new Main();

		Thread p2 = new Thread(r2);
		p2.setName("p2");
		p2.start();
	}

	/**
	 * @author Babacar
	 * @effects this function is used to know if the code is still running
	 */
	public void end(){
		endDetected = false;
	}


}