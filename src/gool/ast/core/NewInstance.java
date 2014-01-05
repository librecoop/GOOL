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

import java.util.Arrays;
import java.util.List;

/**
 * Represents the declaration and the creation of a new object instance in the
 * intermediate language.
 */
public final class NewInstance extends Parameterizable {
	/**
	 * The variable's name.
	 */
	private VarDeclaration variable;

	public NewInstance(VarDeclaration variable) {
		super(variable.getType());
		this.variable = variable;
	}

	private NewInstance(VarDeclaration variable, List<Expression> parameters) {
		this(variable);
		addParameters(parameters);
	}

	public NewInstance(VarDeclaration variable, Expression expression) {
		this(variable);
		addParameter(expression);
	}

	public static NewInstance create(VarDeclaration variable,
			Expression... params) {
		if (params != null) {
			return new NewInstance(variable, Arrays.asList(params));
		}
		return new NewInstance(variable);
	}

	public VarDeclaration getVariable() {
		return variable;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
