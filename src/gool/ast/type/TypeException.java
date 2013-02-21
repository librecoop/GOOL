
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
		/** Type unrecognized*/
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
	
	private String module;
	
	/**
	 * The kind of exception, 'CUSTOM' for non language specified exceptions
	 */
	private Kind kind;
	
	/**
	 * All exceptions know to GOOL, language defined like custom ones
	 */
	static private HashMap<String,TypeException> exceptions = new HashMap<String,TypeException>();
	
	public TypeException(String name, String module, Kind kind) {
		this.name = name;
		this.module = module;
		this.kind = kind;
	}
	
	static public void add(TypeException exception) {
		exceptions.put(exception.getName(), exception);
		exceptions.put(exception.getModule() + "." + exception.getName(), exception);
	}
	
	static public void add (TypeException... args) {
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
