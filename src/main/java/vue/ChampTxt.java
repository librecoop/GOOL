package vue;

import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * La classe ChampTxt.java
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class ChampTxt extends JTextArea {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PrintStream sortie = null;
	
	/**
	 * Constructeur de la classe
	 */
	public ChampTxt () {
		
		super();
		
		sortie = new PrintStream(new StreamOutPerso(this));		
		
		System.setOut(sortie);
		System.setErr(sortie);		
		
		this.setEditable(false);
		this.setVisible(true);
		
	}

}
