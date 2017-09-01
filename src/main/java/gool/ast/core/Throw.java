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
import gool.generator.common.CodeGenerator;

/**
 * This class captures a "throw" invocation in the intermediate language.
 */
public class Throw extends Statement {
	/**
	 * Throw expression.
	 */
	private Expression expression;

	/**
	 * 
	 * @param expression
	 */
	public Throw(Expression expression) {
		this.expression = expression;
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

	/**
	 * Gets the throw expression.
	 * @return
	 * 		The throw expression.
	 */
	public Expression getExpression() {
		return expression;
	}
}
