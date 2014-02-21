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
 * Hence it is a Dec. But because declarations are usually OK statements in OO
 * Languages it is also a Statement
 * 
 * @param T
 *            is the type of the declared variable, if known at compile time,
 *            otherwise put IType. That way java generics grant us some level of
 *            type checking of the generated code at compiler design time.
 *            Sometimes we will not be able to use this though, because we will
 *            not know T at compiler design time.
 */
public final class Identifier extends Expression {

	/**
	 * The name of the identifier.
	 */
	private String name;

	/**
	 * The constructor of an identifier.
	 * @param type
	 * 		: The type of the identifier.
	 * @param name
	 * 		: The name of the identifier.
	 */
	public Identifier(IType type, String name) {
		super(type);
		this.name = name;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	/**
	 * Gets the name of the identifier.
	 * @return
	 * 		The name of the identifier.
	 */
	public String getName() {
		return name;
	}

}
