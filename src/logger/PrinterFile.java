package logger;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logger.Log.Level;

public class PrinterFile extends Printer {

	private List<FileWriter> fstreams;
	
	public PrinterFile(Level lv, boolean t, String d, List<String> outputs) {
		super(lv, t, d);
		fstreams = new ArrayList<FileWriter>();
		
		for(String output : outputs) {
			try {
				fstreams.add(new FileWriter(output, true));
			} catch (IOException e) {
			}
		}
	}

	@Override
	public void print(Level lv, String message, Color color) {
		if(lv.ordinal() >= level.ordinal()) {
			message = super.formatedDate() + message;
			
			if(tag) {
				message = "[" + lv + "] " + message;
			}
			
			for (FileWriter fstream : fstreams) {
				try {
					fstream.write(message + "\n");
					fstream.flush();
				} catch (IOException e) {
				}
			}
		}
	}
	
	@Override
	public void printErr(Level lv, Exception e, Color color) {
		print(lv, e.toString(), color);
	}
}
