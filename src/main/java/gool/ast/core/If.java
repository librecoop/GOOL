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
 * This captures the if statements of the intermediate language. Hence it is an
 * OOTStatement. Notice the type checking achieved through generics.
 */
public class If extends Statement {

	/**
	 * The condition expression.
	 */
	private Expression condition;
	/**
	 * The statement that is evaluated when the condition is true.
	 */
	private Statement thenStatement;
	/**
	 * The statement that is evaluated when the condition is false.
	 */
	private Statement elseStatement;

	/**
	 * The constructor of an "if" representation.
	 * @param condition
	 * 		: The condition expression.
	 * @param thenStatement
	 * 		: The statement that is evaluated when the condition is true.
	 * @param elseStatement
	 * 		: The statement that is evaluated when the condition is false.
	 */
	public If(Expression condition, Statement thenStatement,
			Statement elseStatement) {
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}

	/**
	 * Gets the condition expression.
	 * @return
	 * 		The condition expression.
	 */
	public Expression getCondition() {
		return condition;
	}

	/**
	 * Gets the statement that is evaluated when the condition is true.
	 * @return
	 * 		The statement that is evaluated when the condition is true.
	 */
	public Statement getThenStatement() {
		return thenStatement;
	}

	/**
	 * Gets the statement that is evaluated when the condition is false.
	 * @return
	 * 		The statement that is evaluated when the condition is false.
	 */
	public Statement getElseStatement() {
		return elseStatement;
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
