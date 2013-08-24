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

import gool.generator.GoolGeneratorController;

/**
 * This class accounts for assignments in the intermediate language. Hence it is
 * a Statement.
 * 
 * @param T
 *            is the type of the variable, if known at compile time, otherwise
 *            IType is used. The value must have an IType which extends this
 *            type. That way java generics grant us some level of type checking
 *            of the generated code at compiler design time. Sometimes we will
 *            not be able to use this though, because we will not know T at
 *            compiler design time.
 */
public class Assign extends Statement {

	/**
	 * The expression located on the left of the assignment.
	 */
	private Node var;
	/**
	 * The value to be assigned.
	 */
	private Expression value;

	/**
	 * @param var
	 *            is a pointer to a previously declared variable
	 * @param value
	 */
	public Assign(Node var, Expression value) {
		this.var = var;
		this.value = value;
	}

	/**
	 * Gets the left expression of the assign operator.
	 * 
	 * @return the left expression node of the assign operator
	 */
	public Node getLValue() {
		return var;
	}

	/**
	 * Gets the expression to assign.
	 * 
	 * @return the node expression to assign.
	 */
	public Expression getValue() {
		return value;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public void setExpression(Expression expression) {
		this.value = expression;
	}

}
