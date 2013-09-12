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

import gool.generator.GoolGeneratorController;

import java.util.HashMap;

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
		ARITHMETIC, COLLECTION, CAST, ENUM, ARGUMENT, THREAD, ARRAY, SECURITY, TYPE, UNSUPORTED, ARRAYSIZE, STATE, CLASSNOTFOUND, ACCESS, NEWINSTANCE, INTERUPT, NOSUCHFIELD, NOSUCHMETH, NULLREFERENCE,
	}

	/**
	 * The name of the exception in the source language
	 */
	private String name;

	private String module;

	/**
	 * The kind of exception, 'CUSTOM' for non language specified exceptions
	 */
	private Kind kind;

	/**
	 * All exceptions know to GOOL, language defined like custom ones
	 */
	static private HashMap<String, TypeException> exceptions = new HashMap<String, TypeException>();

	public TypeException(String name, String module, Kind kind) {
		this.name = name;
		this.module = module;
		this.kind = kind;
	}

	static public void add(TypeException exception) {
		exceptions.put(exception.getName(), exception);
		exceptions.put(exception.getModule() + "." + exception.getName(),
				exception);
	}

	static public void add(TypeException... args) {
		for (TypeException exception : args)
			add(exception);
	}

	static public TypeException get(String name) {
		return exceptions.get(name);
	}

	@Override
	public String getName() {
		return name;
	}

	public Kind getKind() {
		return kind;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public static boolean contains(String typeName) {
		return exceptions.containsKey(typeName);
	}

	public String getModule() {
		return module;
	}
}
