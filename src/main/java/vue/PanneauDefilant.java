package vue;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * La classe PanneauDefilant.java
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class PanneauDefilant extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de la classe avec d&eacute;finition du champ texte a int&eacute;grer au panneau defilant.
	 * @param champ
	 */
	public PanneauDefilant (JTextArea champ) {
		
		super(champ);		
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVisible(true);
		
	}
	
}
