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
import gool.ast.type.TypeString;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 * Allows to compute the value of a boolean or integer operation with two
 * operands.
 */
public class BinaryOperation extends Operation {

	/**
	 * The left operand.
	 */
	private Expression left;
	/**
	 * The right operand.
	 */
	private Expression right;

	/**
	 * Creates a new instance of BinaryOperator.
	 * 
	 * @param operator
	 *            the operator to be used to compute the operation.
	 * @param left
	 *            the left operand.
	 * @param right
	 *            the right operand.
	 */
	public BinaryOperation(Operator operator, Expression left,
			Expression right, IType type, String symbol) {
		super(operator, type, symbol);
		/*
		 * If both expressions have different types and the '+' operator is
		 * used, we assume that it is a string concatenation. Thus, we force the
		 * type of the binary operation node to be a string.
		 */
		if (operator == Operator.PLUS
				&& (left.getType().equals(TypeString.INSTANCE) || right
						.getType().equals(TypeString.INSTANCE))) {
			setType(TypeString.INSTANCE);
		}
		this.left = left;
		this.right = right;
	}

	/**
	 * Gets the left operand.
	 * 
	 * @return the left operand expression.
	 */
	public Expression getLeft() {
		return left;
	}

	/**
	 * Gets the right operand.
	 * 
	 * @return the right operand expression.
	 */
	public Expression getRight() {
		return right;
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
