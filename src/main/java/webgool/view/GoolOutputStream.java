package webgool.view;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * GoolOutputStream class
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class GoolOutputStream extends OutputStream {
	private JTextArea textArea;

	/**
	 * Class ctor with a text field definition which will receive the outputs.
	 * @param textArea JTextArea that displays the output messages.
	 */
	public GoolOutputStream(JTextArea textArea) {
		this.textArea = textArea;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		
		// Data redirection to the textarea
        textArea.append(String.valueOf((char)b));
        
        // Set the cursor position at the end of the textarea
        textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
