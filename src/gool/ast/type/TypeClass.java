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

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Expression;
import gool.generator.GoolGeneratorController;

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

	private boolean isEnum;

	public TypeClass(String name) {
		this.name = name;
	}

	public TypeClass(String packageName, String name) {
		this(name);
		this.packageName = packageName;
	}

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

	public boolean isEnum() {
		return isEnum;
	}

	public void setIsEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public String getPackageName() {
		return packageName;
	}

	public ClassDef getClassDef() {
		return classDef;
	}

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
		return GoolGeneratorController.generator().getCode(this);
	}

}