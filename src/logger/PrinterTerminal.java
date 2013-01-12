package logger;

import java.awt.Color;
import logger.Log.Level;

public class PrinterTerminal extends Printer {

	public PrinterTerminal(Level lv, boolean t, String d) {
		super(lv, t, d);
	}
	
	@Override
	public void print(Level lv, String message, Color color) {
		if(lv.ordinal() >= level.ordinal()) {
			message = super.formatedDate() + message;
			
			if(tag) {
				message = "[" + lv + "] " + message;
			}
			
			if(lv.ordinal() >= Level.ERROR.ordinal()) {
				System.err.println(message);
			}
			else {
				System.out.println(message);
			}
		}
	}

}
