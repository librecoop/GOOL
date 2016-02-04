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

import gool.ast.type.IType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class accounts for method declarations in the intermediate language.
 * Hence it is an OOTDec.
 * 
 * @param T
 *            is the return type, if known at compile time, otherwise put
 *            OOTType. That way java generics grant us some level of type
 *            checking of the generated code at compiler design time. Sometimes
 *            we will not be able to use this though, because we will not know T
 *            at compiler design time.
 */
public class Meth extends Dec {
	/**
	 * The method body instructions.
	 */
	private Block block = new Block();
	/**
	 * The list of parameters.
	 */
	private List<VarDeclaration> parameters = new ArrayList<VarDeclaration>();
	/**
	 * The list of generic types (specific to the method).
	 */
	private List<IType> genericTypes = new ArrayList<IType>();
	/**
	 * The list of Exceptions the method throws
	 */
	private List<IType> throwStatement = new ArrayList<IType>();

	/**
	 * Indicates if this method is inherited.
	 */
	private boolean inherited;

	/**
	 * The definition of the class.
	 */
	private ClassDef classDef;

	/**
	 * Constructor of a method representation.
	 * @param returnType
	 * 			: The return type used by the method.
	 * @param modifier
	 * 			: The specific modifier used by the method.
	 * @param name
	 * 			: The name of the method.
	 */
	public Meth(IType returnType, Modifier modifier, String name) {
		super(returnType, name);
		addModifier(modifier);
	}

	/**
	 * The constructor of a public method representation.
	 * @param returnType
	 * 		: The return type used by the method.
	 * @param name
	 * 		: The name of the method.
	 */
	public Meth(IType returnType, String name) {
		super(returnType, name);
		addModifier(Modifier.PUBLIC);
	}

	/**
	 * Adds a parameter in the method declaration.
	 * @param varParam
	 * 		: The parameter declaration in the method.
	 */
	public final void addParameter(VarDeclaration varParam) {
		parameters.add(varParam);
	}

	/**
	 * Adds a statement in the block statements of the 
	 * method's declaration representation.
	 * @param statement
	 * 		: The statement to add.
	 */
	public final void addStatement(Statement statement) {
		block.addStatement(statement);
	}
	
	/**
	 * Adds a statements list in the block statements of the 
	 * method's declaration representation.
	 * @param statement
	 * 		: The statements list to add.
	 */
	public final void addStatements(List<Statement> statements) {
		block.addStatements(statements);
	}

	/**
	 * Adds a generic type in the method declaration.
	 * Use it if your method is generic.
	 * @param type
	 * 		: The generic type of the method declaration.
	 */
	public final void addGenericType(IType type) {
		genericTypes.add(type);
	}

	/**
	 * Determines whether the method declared is the main method.
	 * @return
	 * 		True if it is the main method. Else false.
	 */
	public boolean isMainMethod() {
		return false;
	}

	/**
	 * Gets the block statements of the 
	 * method's declaration representation.
	 * @return
	 * 	The block statements of the method.
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * Gets the definition of the class.
	 * @return
	 * 		The definition of the class.
	 */
	public ClassDef getClassDef() {
		return classDef;
	}

	/**
	 * Sets the definition of the class.
	 * @param classDef
	 * 		The new definition of the class.
	 */
	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	/**
	 * Gets the parameters list of the 
	 * method's declaration representation.
	 * @return
	 * 		The parameters list of the method.
	 */
	public List<VarDeclaration> getParams() {
		return parameters;
	}

	/**
	 * Determines whether the method is inherited.
	 * @return
	 * 		True if this method is inherited. Else false.
	 */
	public boolean isInherited() {
		return inherited;
	}

	/**
	 * Sets the inherit flag.
	 * @param inherited
	 */
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	/**
	 * Gets the list of generic types of the method.
	 * @return
	 * 		The list of generic types of the method.
	 */
	public List<IType> getGenericTypes() {
		return genericTypes;
	}
	
	/**
	 * Determines whether the method is a constructor.
	 * @return
	 * 		True if this method is a constructor. Else false.
	 */
	public boolean isConstructor() {
		return false;
	}

	/**
	 * Determines whether the method is an abstract method.
	 * @return
	 * 		True if this method is an abstract method. Else false.
	 */
	public boolean isAbstract() {
		return getModifiers().contains(Modifier.ABSTRACT);
	}

	/**
	 * Gets the list of the throw statement in the method.
	 * @return
	 * 		The list of the throw statement in the method.
	 */
	public List<IType> getThrowStatement() {
		return throwStatement;
	}

	/**
	 * Adds a throw statement in the method.
	 * @param type
	 * 		: The type of the throw statement to add.
	 */
	public final void addThrowStatement(IType type) {
		throwStatement.add(type);
	}
	
	/**
	 * Determines whether the method is a gool method implementation.
	 * @return
	 * 		True if this method is a gool method implementation. Else false.
	 */
	public boolean isGoolMethodImplementation(){
		return false;
	}
}
