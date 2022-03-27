package simulator;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar{
	
	public static int WIDTH = Simulator.WIDTH, HEIGHT = 22;
	JMenu file;
		JMenuItem clear;
		JMenuItem importNBC;
		JMenuItem quit;
	JMenu edit;
		JMenuItem init;
	JMenu run;
		JMenuItem run2;
		JMenuItem abort;
	
	public Main main;
		
	public boolean runIsClicked;	
	
	
	public MenuBar(Main main){
		this.main = main;
		runIsClicked = false;
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		init();	
		updateItems();
		remove();
		addItems();
	}
	
	//makes items
	private void init(){
		edit = new JMenu("edit");
		init = new JMenuItem("Back to initialize");
		init.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_DOWN_MASK));
		init.addActionListener(e -> {
			System.out.println("MenuBar:\t" + e.getActionCommand());
			Main.init = new Init(main);

		});


		file= new JMenu("file");
		file.setMnemonic('F');
		clear = new JMenuItem("Clear environment");
		clear.setMnemonic('C');
		clear.setIcon(new ImageIcon(this.getClass().getResource("/res/images/menu/clear.png")));
		clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		clear.addActionListener(e -> {
			System.out.println("MenuBar:\t" + e.getActionCommand());
			GraphicsPanel.gd.clearImages();
		});
		
		importNBC = new JMenuItem("Import NBC-file");
		importNBC.setIcon(new ImageIcon(this.getClass().getResource("/res/images/menu/import.png")));
		importNBC.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));

		importNBC.addActionListener(e -> {
			System.out.println("MenuBar:\t" + e.getActionCommand());
			Simulator.code = new FilePanel(main).getCode();
			Simulator.codePanel.paint();
			Main.simulator.refresh();
		});
		
		quit = new JMenuItem("Quit");
		quit.setIcon(new ImageIcon(this.getClass().getResource("/res/images/menu/quit.png")));
		quit.addActionListener(e -> {
			System.out.println("MenuBar:\t" + e.getActionCommand());
			Main.frame.dispose();
		});
	
	run = new JMenu("Run");
		run2 = new JMenuItem("Run");
		run2.setIcon(new ImageIcon(this.getClass().getResource("/res/images/menu/play.png")));
		run2.addActionListener(e ->{
			System.out.println("MenuBar:\t" + e.getActionCommand());

			runIsClicked = true;
			Simulator.menuBar.refresh();
			Simulator.run = new RunNBC(main);

			}
        );
	
	abort = new JMenuItem("Abort");
	abort.setIcon(new ImageIcon(this.getClass().getResource("/res/images/menu/abort.png")));
	abort.addActionListener(e -> {
		System.out.println("MenuBar:\t" + e.getActionCommand());
		Simulator.outputPanel.end();
		Simulator.run.end();
		main.end();
		runIsClicked = false;
		Simulator.menuBar.refresh();
	});
	}
	
	
	//updates items
	private void updateItems(){

		run2.setEnabled(Simulator.code != null);

		abort.setEnabled(run2.isEnabled() && runIsClicked);
	}
	
	//remove all
	private void remove(){
		
		file.removeAll();
		edit.removeAll();
		run.removeAll();
		removeAll();
	}

	//adds items to menu
	private void addItems(){

		edit.add(init);

		file.add(clear);
		file.add(importNBC);
		file.addSeparator();
		file.add(quit);

		run.add(run2);
		run.addSeparator();
		run.add(abort);

		add(file);
		add(edit);
		add(run);
	}
	
	public void refresh(){
		updateItems();
		remove();
		addItems();
	}
}