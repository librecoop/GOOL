package gool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import logger.Log;


public final class Settings {

	private static Properties properties;

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
			Log.e(String.format("Failed to load the property file %s", propertyFile)+e.toString());
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
}
