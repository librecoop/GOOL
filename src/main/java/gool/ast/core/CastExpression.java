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
import gool.generator.common.CodeGenerator;

/**
 * Represents the conversion of an expression to a target type. In object
 * oriented languages, this construct represents an explicit cast.
 * 
 * @param <T>
 *            the target type.
 */
public final class CastExpression extends Expression {

	/**
	 * The expression to be casted.
	 */
	private Expression expression;

	/**
	 * Creates a new cast using the target type and the expression to be casted.
	 * 
	 * @param targetType
	 *            the target type.
	 * @param expression
	 *            the expression to be casted.
	 */
	public CastExpression(IType targetType, Expression expression) {
		super(targetType);
		this.expression = expression;
	}

	/**
	 * Gets the expression to be casted.
	 * 
	 * @return the expression to be casted.
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Generates the code in the target language.
	 */
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
