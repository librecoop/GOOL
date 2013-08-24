/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
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

package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

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

	private ClassDef classDef;

	public Meth(IType returnType, Modifier modifier, String name) {
		super(returnType, name);
		addModifier(modifier);
	}

	public Meth(IType returnType, String name) {
		super(returnType, name);
		addModifier(Modifier.PUBLIC);
	}

	public final void addParameter(VarDeclaration varParam) {
		parameters.add(varParam);
	}

	public final void addStatement(Statement statement) {
		block.addStatement(statement);
	}

	public final void addStatements(List<Statement> statements) {
		block.addStatements(statements);
	}

	public final void addGenericType(IType type) {
		genericTypes.add(type);
	}

	public boolean isMainMethod() {
		return false;
	}

	public Block getBlock() {
		return block;
	}

	public ClassDef getClassDef() {
		return classDef;
	}

	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	public List<VarDeclaration> getParams() {
		return parameters;
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	public List<IType> getGenericTypes() {
		return genericTypes;
	}

	public boolean isConstructor() {
		return false;
	}

	public boolean isAbstract() {
		return getModifiers().contains(Modifier.ABSTRACT);
	}

	public String getHeader() {
		return GoolGeneratorController.generator().getCode(this);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public List<IType> getThrowStatement() {
		return throwStatement;
	}

	public final void addThrowStatement(IType type) {
		throwStatement.add(type);
	}
	public boolean isGoolMethodImplementation(){
		return (this instanceof GoolMethodImplementation);
	}
}
