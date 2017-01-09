package vue;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * La classe StreamOutPerso.java
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class StreamOutPerso extends OutputStream {
	private JTextArea textArea;

	/**
	 * Constructeur de la classe avec d&eacute;finition du TextArea qui recevra les sorties.
	 * @param textArea le TextArea qui affichera les messages de sortie.
	 */
	public StreamOutPerso(JTextArea textArea) {
		this.textArea = textArea;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		
		// redirection des donnees vers le textarea
        textArea.append(String.valueOf((char)b));
        
        // on s'assure que le curseur du textarea soit toujours a la fin
        textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
