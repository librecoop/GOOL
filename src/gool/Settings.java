package gool;

import gool.test.GoolTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public final class Settings {

	private static Properties properties;
	private static Logger logger = Logger.getLogger(Settings.class);
	private static String androidPackage;
	private static String androidMainActivity;

	static {
		load("gool.properties");
	}


	public static void load(String propertyFile) {
		try {
			properties = new Properties();
			InputStream stream = ClassLoader.getSystemResourceAsStream(propertyFile);
			if (stream != null) {
				properties.load(stream);
			}
		} catch (IOException e) {
			logger.error(String.format("Failed to load the property file %s", propertyFile)+e);
		}

	}

	public static String get(String property) {
		if (properties == null) {
			throw new IllegalStateException(
					"The configuration settings are not properly initiliazed.");
		}
		@SuppressWarnings("unchecked")
		String value = (String) properties.get(property);
		if (value == null) {
			throw new IllegalStateException(String.format(
					"The property '%s' does not exist.", property));
		}
		return value;
	}

	public static void setAndroidPackage(String androidPackage) {
		Settings.androidPackage = androidPackage;
	}

	public static void setAndroidMainActivity(String androidMainActivity) {
		Settings.androidMainActivity = androidMainActivity;
	}
	
	public static String getAndroidRunCommand() {
		return androidPackage + "/." + androidMainActivity.replace(".java", "");
	}
}
