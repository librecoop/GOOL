package gool.ast.type;

import java.util.HashMap;

import logger.Log;

import gool.generator.GoolGeneratorController;

public class TypeException extends IType {

	/**
	 * Define when the exception is used.
	 * Apart from CUSTOM and DEFAULT, only one instance should exist for each kind
	 */
	public enum Kind {
		//TODO: add more stuff...
		/** User defined exceptions */
		CUSTOM,
		/** Language defined exceptions that does not belong to another kind */
		DEFAULT,
		/** Input/Output related exception */
		IO;
	}
	
	final private String name;
	
	final private TypeException parent;
	
	final private Kind kind;
	
	private String packageName;
	
	static private HashMap<String,TypeException> exceptions = new HashMap<String,TypeException>();

	private TypeException(String name, TypeException parent, String packageName, Kind kind) {
		this.name = name;
		this.parent = parent;
		this.kind = kind;
		this.packageName=packageName;
	}
	
	static public void add(String name, TypeException parent, String packageName) {
		add(name, parent, packageName, Kind.UNDEFINED);
	}
	
	static public void add(String name, TypeException parent, String packageName, Kind kind) {
		if (! exceptions.containsKey(name)) {
			exceptions.put(name, new TypeException(name, parent, packageName, kind));
		} else {
			Log.e(String.format("Exception '%s' already defined", name));
		}
	}
	
	static public TypeException get(String name) {
		return exceptions.get(name);
	}

	public TypeException getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	public Kind getKind() {
		return kind;
	}
	
	public String getPackageName() {
		return packageName;
	}

	@Override
	public String callGetCode() {
		return null; //GoolGeneratorController.generator().getCode(this);
	}
}
