/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import logger.Log;

/**
 * Used to configure the system GOOL with the file named "src/gool.properties".
 *
 * For more information look at this properties file 
 * or the file example "src/gool.properties.example".
 */
public final class Settings {
	
	/**
	 * The properties to configure the system GOOL.
	 */
	private static Properties properties;
	
	/**
	 * TODO Do the documentation.
	 */
	private static String androidPackage;
	
	/**
	 * TODO Do the documentation.
	 */
	private static String androidMainActivity;
	
	static {
		load("gool.properties");
	}
	
	/**
	 * Load the properties file. This method is called internally within the class.
	 * @param propertyFile 
	 * 			: The path name of the properties file.
	 */
	public static void load(String propertyFile) {
		try {
			properties = new Properties();
			InputStream stream = ClassLoader
					.getSystemResourceAsStream(propertyFile);
			if (stream != null) {
				properties.load(stream);
			}
		} catch (IOException e) {
			Log.e(String.format("Failed to load the property file %s",
					propertyFile) + e.toString());
		}

	}
	
	/**
	 * Gets the value of an existing property in the "properties" file.
	 * @param property 
	 * 		: The name of the property.
	 * @return 
	 * 		The value of the desired property. 
	 *      For example, the properties file contains a line with "property_x=My_VALUE"
	 *      so if you want the property "property_x", this will return "My_VALUE" else
	 *      generates an error
	 */
	public static String get(String property) {
		if (properties == null) {
			throw new IllegalStateException(
					"The configuration settings are not properly initiliazed.");
		}
		// TODO @SuppressWarnings("unchecked")
		String value = (String) properties.get(property);
		if (value == null) {
			throw new IllegalStateException(String.format(
					"The property '%s' does not exist.", property));
		}
		return value;
	}

	/**
	 * TODO Do the documentation.
	 */
	public static void setAndroidPackage(String androidPackage) {
		Settings.androidPackage = androidPackage;
	}

	/**
	 * TODO Do the documentation.
	 */
	public static void setAndroidMainActivity(String androidMainActivity) {
		Settings.androidMainActivity = androidMainActivity;
	}

	/**
	 * TODO Do the documentation.
	 */
	public static String getAndroidRunCommand() {
		return androidPackage + "/." + androidMainActivity.replace(".java", "");
	}
}
