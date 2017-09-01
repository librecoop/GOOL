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

import gool.ast.core.ClassDef;
import gool.ast.core.Constant;
import gool.ast.core.Expression;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 * This is the basic type for classes defined in the intermediate language.
 */
public class TypeClass extends ReferenceType {

	/**
	 * The class' package.
	 */
	private String packageName;
	/**
	 * The name of the class.
	 */
	private String name;
	/**
	 * The class definition.
	 */
	private ClassDef classDef;
	/**
	 * A flag to know if the class is a enumeration.
	 */
	private boolean isEnum;

	/**
	 * The constructor of a "type class" representation.
	 * @param name
	 * 		: The textual name of the class used as a type.
	 */
	public TypeClass(String name) {
		this.name = name;
	}

	/**
	 * The constructor of a "type class" representation.
	 * @param packageName
	 * 		: The textual name of the package used by the class.
	 * @param name
	 * 		: The textual name of the class used as a type.
	 */
	public TypeClass(String packageName, String name) {
		this(name);
		this.packageName = packageName;
	}

	/**
	 * Sets the name of the class.
	 * @param name
	 * 		: The new name of the class.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the literal name of the class. It can be used as an expression to
	 * call static members.
	 * 
	 * @return
	 */
	public Expression getClassReference() {
		return new Constant(this, getName());
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Determines if the class is an enumeration.
	 * @return
	 * 		True if the class is an enumeration, else false.
	 */
	public boolean isEnum() {
		return isEnum;
	}

	/**
	 * Sets the flag to know if the class is a enumeration.
	 * @param isEnum
	 * 		: True if the class is an enumeration, else false.
	 */
	public void setIsEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	/**
	 * Gets the name of the package used by the class.
	 * @return
	 * 		The textual name of the package used by the class.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Gets the class' definition.
	 * @return
	 * 		The class' definition.
	 */
	public ClassDef getClassDef() {
		return classDef;
	}

	/**
	 * Sets the class' definition.
	 * @param def
	 * 		: The new class' definition.
	 */
	public void setClassDef(ClassDef def) {
		this.classDef = def;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeClass
				&& getName().equals(((IType) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName();
		}
		return cg.getCode(this);
	}

}