package vue;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * La classe IHMServeur.java
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class IHMServeur extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de la classe
	 */
	public IHMServeur () {
		
		super ();
		
		this.setSize(400, 800);		
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.add(new Panneau(), BorderLayout.CENTER);
		this.setTitle("GOOL Translate Dedicated Server");
		this.setVisible(true);
		
	}

}
