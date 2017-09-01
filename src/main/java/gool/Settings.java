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

import java.io.FileInputStream;
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

	static {
		load(ClassLoader.getSystemClassLoader().getResource("gool/properties").getFile());
	}

	/**
	 * Load the properties file. This method is called internally within the class.
	 * @param propertyFile 
	 * 			: The path name of the properties file.
	 * @throws IOException 
	 */
	public static void load(String propertyFile){
		properties = new Properties();
		try{
			//Try to load the properties file from project root folder
			FileInputStream input = new FileInputStream(propertyFile);
			properties.load(input);			
		} catch ( Exception ef){
			//If failed, load the default properties file from classpath.
			try {				
				InputStream stream = ClassLoader
						.getSystemResourceAsStream(propertyFile);
				if (stream != null) {
					properties.load(stream);
				}
			} catch (IOException ex) {
				Log.e(String.format("Failed to load the property file %s",
						propertyFile) + ex.toString());
			}
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
	 * Set the value of a property. If this property does not exist, it is added.
	 * @param property
	 * 		: The name of the property
	 * @param value
	 * 		: The value of the property
	 * @return
	 * 		The previous value of the property.
	 */
	public static String set(String property, String value){
		return (String)properties.setProperty(property, value);
	}

	/**
	 * Launch a graphical setter
	 */

	public static void launchGuiSetter(){
		SettingsPanel guiSetter = new SettingsPanel(properties);
		guiSetter.launch();
	}
}
