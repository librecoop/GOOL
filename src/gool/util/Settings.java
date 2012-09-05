package gool.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public final class Settings {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Settings.class);
	private final static Settings INSTANCE = new Settings();
	private Properties properties;

	private Settings() {
	}

	public static synchronized Settings getInstance() {
		return INSTANCE;
	}

	public void load(Properties properties) {
		this.properties = properties;
	}

	public void load(String propertyFile) {
		try {
			properties = new Properties();
			InputStream stream = ClassLoader.getSystemResourceAsStream(propertyFile);
			if (stream != null) {
				properties.load(stream);
			}
		} catch (IOException e) {
			LOG.error(String.format("Failed to load the property file %s", propertyFile), e);
		}

	}

	public <T> T get(String property) {
		if (properties == null) {
			throw new IllegalStateException(
					"The configuration settings are not properly initiliazed.");
		}
		@SuppressWarnings("unchecked")
		T value = (T) properties.get(property);
		if (value == null) {
			throw new IllegalStateException(String.format(
					"The property '%s' does not exist.", property));
		}
		return value;
	}
}
