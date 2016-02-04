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

package gool.ast.core;


/**
 * This class captures the definition of a static class. 
 */
public class StaticClass extends ClassDef {

	/**
	 * The constructor of a "static class" representation. 
	 * @param modifier
	 * 		: The modifier of the class (except STATIC).
	 * @param name
	 * 		: The name of the static class.
	 * @param platform
	 * 		: The destination platform used by the system.
	 */
	public StaticClass(Modifier modifier, String name) {
		super(modifier, name);
		addModifier(Modifier.STATIC);
	}

	/**
	 * Adds a method to the "static class" representation.
	 * @param method
	 * 		: The "method" representation to add.
	 */
	public void addMethod(Meth method) {
		super.addMethod(method);
		method.addModifier(Modifier.STATIC);
	}

	/**
	 * Adds a field to the "static class" representation.
	 * @param field
	 * 		: The "field" representation to add.
	 */
	public void addField(Field field) {
		super.addField(field);
		field.addModifier(Modifier.STATIC);
	}
}
