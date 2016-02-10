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
import gool.generator.GoolGeneratorController;

/**
 * This class captures a variable declaration in the intermediate language.
 * Hence it inherits of Dec.
 */
public class VarDeclaration extends Dec {
	
	/**
	 * The initial value of the variable.
	 */
	private Expression initialValue;

	/**
	 * The constructor of a "variable declaration" representation. 
	 * @param var
	 * 		: The declaration of the variable.
	 */
	public VarDeclaration(Dec var) {
		this(var.getType(), var.getName());
	}

	/**
	 * The constructor of a "variable declaration" representation.
	 * @param type
	 * 		: The type of the variable.
	 * @param name
	 * 		: The name of the variable.
	 */
	public VarDeclaration(IType type, String name) {
		super(type, name);
	}

	/**
	 * Sets the initial value of the variable.
	 * @param initialValue
	 * 		: The new initial value of the variable.
	 */
	public void setInitialValue(Expression initialValue) {
		this.initialValue = initialValue;
	}

	/**
	 * Gets the initial value of the variable.
	 * @return
	 * 		The initial value of the variable.
	 */
	public Expression getInitialValue() {
		return initialValue;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
