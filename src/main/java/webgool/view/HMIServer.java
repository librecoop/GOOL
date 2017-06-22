package webgool.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * HMIServer class
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class HMIServer extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public HMIServer () {
		
		super ();
		
		this.setSize(400, 800);		
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.add(new GoolPanel(), BorderLayout.CENTER);
		this.setTitle("GOOL Translate Dedicated Server");
		this.setVisible(true);
		
	}

}
