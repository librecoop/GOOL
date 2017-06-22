package webgool.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/**
 * GoolPanel class
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class GoolPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	
	public GoolPanel() {
		
		super();
		
		this.setLayout(new BorderLayout());
		this.add(new GoolScrollPane(new TextField()), BorderLayout.CENTER);
		this.setVisible(true);
		
	}

}
