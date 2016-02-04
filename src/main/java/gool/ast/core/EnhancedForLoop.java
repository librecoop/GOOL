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

import gool.ast.type.TypeNone;

/**
 * This class captures an enhanced for loop in the intermediate language.
 * Hence it inherits of Expression.
 * For example, this class captures
 * {@code
 * for( String s : array){
 * 	...
 * }
 */
public class EnhancedForLoop extends Expression {

	/**
	 * The variable declaration in the enhanced for loop.
	 */
	private VarDeclaration varDec;
	
	/**
	 * The expression used in the enhanced for loop.
	 */
	private Expression expr;
	
	/**
	 * The statement used in the enhanced for loop.
	 */
	private Statement statements;

	/**
	 * The simple constructor of an enhanced for loop representation.
	 */
	private EnhancedForLoop() {
		super(TypeNone.INSTANCE);
	}

	/**
	 * The constructor of an enhanced for loop representation.
	 * @param varDec
	 * 			: The variable declaration in the enhanced for loop.
	 * @param expr
	 * 			: The expression used in the enhanced for loop.
	 * @param statements
	 * 			: The statement used in the enhanced for loop.
	 */
	public EnhancedForLoop(VarDeclaration varDec, Expression expr,
			Statement statements) {
		this();
		this.varDec = varDec;
		this.expr = expr;
		this.statements = statements;
	}

	/**
	 * Gets the variable declaration in the enhanced for loop.
	 * @return
	 * 		The variable declaration in the enhanced for loop.
	 */
	public VarDeclaration getVarDec() {
		return varDec;
	}

	/**
	 * Gets the expression used in the enhanced for loop.
	 * @return
	 * 		The expression used in the enhanced for loop.
	 */
	public Expression getExpression() {
		return expr;
	}

	/**
	 * Gets the statement used in the enhanced for loop.
	 * @return
	 * 		The statement used in the enhanced for loop.
	 */
	public Statement getStatements() {
		return statements;
	}

}
