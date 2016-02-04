package logger;

import java.awt.Color;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.Config;
import org.ini4j.Ini;

public class Log {
	
	private static int compt = 0;
	static public Boolean DEBUG_LOG = false;
	
	// The instance
	private static final Log log = new Log();

	// Levels possible
	public enum Level {
		ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF
	}

	// List of printers
	private List<Printer> printers;

	private Log(){
		printers = new ArrayList<Printer>();

		// Read the config file
		Ini ini;
		try {
			ini = new Ini();
			Config cfg = new Config();
			cfg.setMultiSection(true);
			ini.setConfig(cfg);
			InputStream stream = ClassLoader.getSystemResourceAsStream("config.ini");
			ini.load(stream);

			// Adding printers into file
			try {
				List<Ini.Section> printersFile = ini.getAll("printer-file");
				for (Ini.Section printer : printersFile) {
					try {
						if (printer.get("enable", Boolean.class)) { // If the
																	// printer
																	// is enable
							Level level = getLevel(printer);
							Boolean tag = getTag(printer);
							String date = getDate(printer);

							List<String> outputs = printer.getAll("output");
							printers.add(new PrinterFile(level, tag, date,
									outputs));
						}
					} catch (Exception e) {
						System.err.println(String.format("%s \n %s", e.toString(),
								e.getMessage()));
					}
				}
			} catch (Exception e) {
				System.err.println(String.format("%s \n %s", e.toString(),
						e.getMessage()));
			}

			// Adding the printer into terminal
			try {
				Ini.Section printer = ini.get("printer-terminal");
				if (printer.get("enable", Boolean.class)) {// If the printer is
															// enable
					Level level = getLevel(printer);
					Boolean tag = getTag(printer);
					String date = getDate(printer);
					printers.add(new PrinterTerminal(level, tag, date));
				}
			} catch (Exception e) {
				System.err.println(String.format("%s \n %s", e.toString(),
						e.getMessage()));
			}

		} catch (Exception e) {
			System.err.println(String.format("%s \n %s", e.toString(),
					e.getMessage()));
		}
	}

	// Print a trace message
	public static void t(String message) {
		print(Level.TRACE, Color.DARK_GRAY, message);
	}

	// Print a debug message
	public static void d(String message) {
		if (DEBUG_LOG)
			print(Level.DEBUG, Color.GREEN, message);
	}

	// Print a information message
	public static void i(String message) {
		print(Level.INFO, Color.BLUE, message);
	}

	// Print a warn message
	public static void w(String message) {
		print(Level.WARN, Color.YELLOW, message);
	}

	// Print an error message
	public static void e(String message) {
		print(Level.ERROR, Color.RED, message);
	}

	// Print an error message
	public static void e(Exception e) {
		print(Level.ERROR, Color.RED, e);
	}

	// Print a fatal message
	public static void f(String message) {
		print(Level.FATAL, Color.MAGENTA, message);
	}

	private static void print(Level lv, Color c, String message) {
		for (Printer p : log.printers) {
			p.print(lv, message, c);
		}
	}

	private static void print(Level lv, Color c, Exception e) {
		for (Printer p : log.printers) {
			p.printErr(lv, e, c);
		}
	}

	private Level getLevel(Ini.Section printer) {
		Level lv;
		try {
			lv = printer.get("level", Level.class);
			if (lv == null) {
				throw new Exception();
			}
		} catch (Exception e) { // If the level is wrong
			lv = Level.ALL;
		}
		return lv;
	}

	private boolean getTag(Ini.Section printer) {
		Boolean tag;
		try {
			tag = printer.get("tag", Boolean.class);
			if (tag == null) {
				throw new Exception();
			}
		} catch (Exception e) { // If the level is wrong
			tag = false;
		}
		return tag;
	}

	private String getDate(Ini.Section printer) {
		String date;
		try {
			date = printer.get("date", String.class);
			if (date == null) {
				throw new Exception();
			}
		} catch (Exception e) { // If the level is wrong
			date = "";
		}
		return date;
	}
	
	public static void MethodIn(Thread th) {
		compt++;
		String mess = ">" + compt;
		mess += String.format("%" + compt + "s", "").replace(' ', '-');
		Log.d(mess + " " + th.getStackTrace()[2].toString()
				+ " | " + th.getStackTrace()[3].toString());
	}
	
	public static void MethodOut(Thread th) {
		String mess = "<" + compt;
		mess += String.format("%" + compt + "s", "").replace(' ', '-');
		Log.d(mess + " " + th.getStackTrace()[2].toString()
				+ " | " + th.getStackTrace()[3].toString() + " - Bye !");
		if (compt > 1)
			compt--;
	}
	
	public static Object MethodOut(Thread th, Object retarg) {
		String mess = "<" + compt;
		mess += String.format("%" + compt + "s", "").replace(' ', '-');
		Log.d(mess + " " + th.getStackTrace()[2].toString()
				+ " | " + th.getStackTrace()[3].toString() + " - Bye !");

		if (compt > 1)
			compt--;
		return retarg;
	}
}
