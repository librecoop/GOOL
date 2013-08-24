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

import gool.ast.type.TypeNone;
import gool.generator.GoolGeneratorController;

public class EnhancedForLoop extends Expression {

	private VarDeclaration varDec;
	private Expression expr;
	private Statement statements;

	private EnhancedForLoop() {
		super(TypeNone.INSTANCE);
	}

	public EnhancedForLoop(VarDeclaration varDec, Expression expr,
			Statement statements) {
		this();
		this.varDec = varDec;
		this.expr = expr;
		this.statements = statements;
	}

	public VarDeclaration getVarDec() {
		return varDec;
	}

	public Expression getExpression() {
		return expr;
	}

	public Statement getStatements() {
		return statements;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
