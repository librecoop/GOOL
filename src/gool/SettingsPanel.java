
package gool;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.HashMap;
//import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



/**
 * Swing frame for displaying Configuration items.
 *
 * @author denis.arrivault@lif.univ-mrs.fr
 *
 */
public class SettingsPanel extends JFrame {

	/**
	 * Serialization version number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The gui panel
	 */
	private JPanel panel;
	/**
	 * Reference to the properties to edit
	 */
	private Properties properties;
	
	/**
	 * Hash map containing the displayed properties 
	 */
	private HashMap<String, JTextField> textFields;

	/**
	 * default constructor
	 * @param propertiesIn
	 * 			: Properties to configure
	 */
	public SettingsPanel(Properties propertiesIn) {
		super();
		properties = propertiesIn;
	}

	/**
	 * Display the frame.
	 */
	public void launch() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		this.textFields = new HashMap<String, JTextField>();
		JPanel configEntry = new JPanel();
		configEntry.setAlignmentX(Component.LEFT_ALIGNMENT);
		configEntry.setLayout(new BoxLayout(configEntry, BoxLayout.PAGE_AXIS));

		Enumeration<?> e = properties.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String prop = properties.getProperty(key);
			JLabel label = new JLabel(key);
			label.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			label.setAlignmentX(LEFT_ALIGNMENT);
			panel.add(label);
			JTextField textField = new JTextField(prop);
			textField.setColumns(30);
			textField.setAlignmentX(LEFT_ALIGNMENT);
			textFields.put(key, textField);
			panel.add(textField);
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(LEFT_ALIGNMENT);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				saveAndClose();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
		panel.add(buttonPanel);

		setSize(500, 500);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JScrollPane scrPane = new JScrollPane(panel);
		setContentPane(scrPane);
		pack();
		setVisible(true);
	}

	/**
	 * Method called by pressing the 'ok' button
	 */
	private void saveAndClose() {

		for (Entry<String, JTextField> entry : textFields.entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue().getText());
		}
		properties.list(System.out);
		close();
		continueGoolCompiler();
	}

	/**
	 * Method called by pressing the 'cancel' button
	 */
	private void close() {
		this.setVisible(false);
		this.dispose();
		continueGoolCompiler();
	}
	
	/**
	 * After settings properties, this method launch the translation
	 */
	private void continueGoolCompiler(){
		GOOLCompiler.launchTranslation();
	}

}
