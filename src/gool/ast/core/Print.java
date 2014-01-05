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
 * This captures the if statements of the intermediate language. Hence it is an
 * OOTStatement. Notice the type checking achieved through generics.
 */
public final class Print extends Statement {
	/**
	 * The expression to be printed.
	 */
	private Expression expr;

	private Print(Expression expr) {
		this.expr = expr;
	}

	/**
	 * Return the expression to be printed.
	 * 
	 * @return the expression to be printed.
	 */
	public Expression getExpression() {
		return getExpression();
	}

	@Override
	public String callGetCode() {
		return "System.out.println(" + expr + ")";
	}

	public static Print create(Expression expr) {
		return new Print(expr);
	}
}
