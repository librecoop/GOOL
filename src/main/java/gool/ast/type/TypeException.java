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

package gool.ast.type;

import java.util.HashMap;

/**
 * This is the basic type for exception in the intermediate language.
 */
public class TypeException extends IType {

	/**
	 * Define when the exception is used.
	 */
	public enum Kind {
		/** User defined exceptions */
		CUSTOM,
		/** Language defined exceptions that does not belong to another kind */
		DEFAULT,
		/** Highest exception, all exceptions inherit from it */
		GLOBAL,
		/** Type unrecognized */
		UNRECOGNIZED,
		/* exceptions related to some domain */
		ARITHMETIC, COLLECTION, CAST, ENUM, ARGUMENT, THREAD, ARRAY, SECURITY,
		TYPE, UNSUPORTED, ARRAYSIZE, STATE, CLASSNOTFOUND, ACCESS, NEWINSTANCE,
		INTERUPT, NOSUCHFIELD, NOSUCHMETH, NULLREFERENCE,
	}

	/**
	 * The name of the exception in the source language
	 */
	private String name;

	/**
	 * The module name of the exception.
	 */
	private String module;

	/**
	 * The kind of exception, 'CUSTOM' for non language specified exceptions
	 */
	private Kind kind;

	/**
	 * All exceptions know to GOOL, language defined like custom ones
	 */
	static private HashMap<String, TypeException> exceptions = new HashMap<String, TypeException>();

	/**
	 * The constructor of a "type exception" representation. 
	 * @param name
	 * 		: The name of the exception in the source language.
	 * @param module
	 * 		: The module name of the exception.
	 * @param kind
	 * 		: The kind of exception, 'CUSTOM' for non language specified exceptions.
	 */
	public TypeException(String name, String module, Kind kind) {
		this.name = name;
		this.module = module;
		this.kind = kind;
	}

	/**
	 * Adds a "type exception" to the exceptions knows by GOOL.
	 * @param exception
	 * 		: The "type exception" to add.
	 */
	static public void add(TypeException exception) {
		exceptions.put(exception.getName(), exception);
		exceptions.put(exception.getModule() + "." + exception.getName(),
				exception);
	}

	/**
	 * Adds "types exceptions" to the exceptions knows by GOOL.
	 * @param args
	 * 		: The "types exceptions" to add.
	 */
	static public void add(TypeException... args) {
		for (TypeException exception : args)
			add(exception);
	}

	/**
	 * Gets the "type exception" defined in the exceptions knows by GOOL.
	 * @param name
	 * 		: The name of the exception in the source language.
	 * @return
	 * 		The "type exception" associated to the name of the exception in the source language.
	 */
	static public TypeException get(String name) {
		return exceptions.get(name);
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets the kind of the "type exception".
	 * @return
	 * 		The kind of the "type exception".
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * Determines if a "type exception" is known by GOOL.
	 * @param typeName
	 * 		: The name of the exception in the source language.
	 * @return
	 * 		True if GOOL knows the exception, else false.
	 */
	public static boolean contains(String typeName) {
		return exceptions.containsKey(typeName);
	}

	/**
	 * Gets the module name of the exception.
	 * @return
	 * 		The module name of the exception.
	 */
	public String getModule() {
		return module;
	}
}
