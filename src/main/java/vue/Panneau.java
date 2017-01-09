package vue;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * La classe Panneau.java
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class Panneau extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de la classe
	 */
	public Panneau() {
		
		super();
		
		this.setLayout(new BorderLayout());
		this.add(new PanneauDefilant(new ChampTxt()), BorderLayout.CENTER);
		this.setVisible(true);
		
	}

}
