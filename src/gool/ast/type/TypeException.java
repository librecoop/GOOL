package gool.ast.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
		/** Highest exception, all exceptions inherit from it */
		GLOBAL,
		/** Input/Output related exception */
		IO,
		/** Arithmetic related exception */
		ARITHMETIC,
	}
	
	private String name;
	
	private List<TypeException> children;	
	
	private Kind kind;
		
	static private HashMap<String,TypeException> exceptions = new HashMap<String,TypeException>();

	public TypeException(String name, Kind kind, TypeException... children) {
		this(name, children);
		tryToSetKind(kind);
	}
	
	/**
	 * Propagate the kind so we don't have to write it when it's the same as
	 * the one of the parent.
	 * DO NOT USE
	 * @param kind
	 */
	public void tryToSetKind(Kind kind) {
		if (this.kind == Kind.CUSTOM) {
			this.kind = kind;
			for (TypeException child : children) {
				child.tryToSetKind(kind);
			}
		}
	}

	public TypeException(String name, TypeException... children) {
		this.name = name;
		this.kind = Kind.CUSTOM;
		this.children = Arrays.asList(children);
		exceptions.put(name, this);
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
		return null; //GoolGeneratorController.generator().getCode(this);
	}
}
