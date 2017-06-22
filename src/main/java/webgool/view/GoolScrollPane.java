package webgool.view;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * GoolScrollPane class
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class GoolScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	/**
	 * Class ctor with a text field definition integrated in the scroll panel.
	 * @param field
	 */
	public GoolScrollPane (JTextArea field) {
		
		super(field);		
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVisible(true);
		
	}
	
}
