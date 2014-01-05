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

import gool.generator.GoolGeneratorController;

/**
 * This class captures variable content invocation. Hence is is an Expression.
 * This has to be distinguished from VarDeclaration because later it does not
 * compile in the same way.
 */
public class VarAccess extends Expression {

	/**
	 * The declared variable.
	 */
	protected Dec var;

	/**
	 * The type of the return value is T
	 * 
	 * @param method
	 *            is a pointer to the previous declaration of the method to be
	 *            invoked.
	 * @param params
	 */
	public VarAccess(Dec var) {
		super(var.getType());
		this.var = var;
	}

	public Dec getDec() {
		return var;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
