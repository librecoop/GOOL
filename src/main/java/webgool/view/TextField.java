package webgool.view;

import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * TextField Class
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class TextField extends JTextArea {
	
	private static final long serialVersionUID = 1L;
	private PrintStream output = null;
	
	public TextField () {
		
		super();
		
		output = new PrintStream(new GoolOutputStream(this));		
		
		System.setOut(output);
		System.setErr(output);		
		
		this.setEditable(false);
		this.setVisible(true);
		
	}

}
