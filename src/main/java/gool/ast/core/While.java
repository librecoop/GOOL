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


/**
 * A while statement.
 */
public class While extends Statement {

	/**
	 * The condition expression.
	 */
	private Expression condition;
	
	/**
	 * The statement that is evaluated when the condition is true.
	 */
	private Statement whileStatement;

	/**
	 * The constructor of a while statement.
	 * @param condition
	 * 		: The condition expression used in the "while".
	 * @param statements
	 * 		: The statement executed by the while.
	 */
	public While(Expression condition, Statement whileStatement) {
		this.condition = condition;
		this.whileStatement = whileStatement;
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
	public Statement getWhileStatement() {
		return whileStatement;
	}

}
