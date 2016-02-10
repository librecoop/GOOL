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
 * The class captures the invocation of a method of treating list.
 */
public class ListMethCall extends Parameterizable {
	
	/**
	 * The expression used in the invocation.
	 */
	private Expression expression;

	/**
	 * The constructor of a method call for list.
	 * @param type
	 * 		: The type for parameterizable.
	 */
	public ListMethCall(IType type) {
		super(type);
	}

	/**
	 * The constructor of a method call for list.
	 * @param type
	 * 		: The type of the target expression.
	 * @param expression
	 * 		: The expression used in the invocation.
	 */
	public ListMethCall(IType type, Expression expression) {
		super(type);
		this.expression = expression;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	/**
	 * Gets the expression used in the invocation.
	 * @return
	 * 		The expression used in the invocation.
	 */
	public Expression getExpression() {
		return expression;
	}

}
