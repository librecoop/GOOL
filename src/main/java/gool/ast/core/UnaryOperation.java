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
 * Allows to compute the value of an unary operation. It may be used on boolean
 * or integer expressions.
 */
public class UnaryOperation extends Operation {
	
	/**
	 * The operand of the operation.
	 */
	private Expression expression;

	/**
	 * Creates a new instance of UnaryOperator.
	 * @param operator
	 * 		: The operator to be used to compute the operation.
	 * @param expr
	 * 		: The operand of the operation
	 * @param type
	 * 		: The type of the operation.
	 * @param symbol
	 * 		: The textual symbol used by the operation.
	 */
	public UnaryOperation(Operator operator, Expression expr, IType type,
			String symbol) {
		super(operator, type, symbol);
		this.expression = expr;
	}

	/**
	 * Gets the operand of the operation.
	 * @return
	 * 		The operand of the operation.
	 */
	public Expression getExpression() {
		return expression;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
