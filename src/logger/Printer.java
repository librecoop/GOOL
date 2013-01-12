package logger;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import logger.Log.Level;

public abstract class Printer {

	protected Level level;
	
	protected boolean tag;
	
	private SimpleDateFormat dateFormat;
	
	public Printer(Level lv, boolean t, String d) {
		level = lv;
		tag = t;
		if(!d.isEmpty()) {
			try {
				dateFormat = new SimpleDateFormat (d);
			}
			catch(Exception e) {
				dateFormat = null;
			}
		}
		else {
			dateFormat = null;
		}
	}
	
	public abstract void print(Level lv, String message, Color color);
	
	protected String formatedDate() {
		if(dateFormat!=null) {
			return dateFormat.format(new Date());
		}
		else {
			return "";
		}
	}
}
