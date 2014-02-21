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

import java.util.ArrayList;
import java.util.List;

/**
 * This class specifies that the respective GOOL node may have a list of
 * parameters.
 */
public abstract class Parameterizable extends Expression {

	/**
	 * The parameters needed to call the method.
	 */
	private List<Expression> params = new ArrayList<Expression>();

	/**
	 * The constructor of a parameter node.
	 * @param type
	 * 		: The type of the GOOL node.
	 */
	protected Parameterizable(IType type) {
		super(type);
	}

	/**
	 * Appends a parameter to the end of the parameters' list.
	 * @param param
	 * 		: The parameter to be appended.
	 * @return
	 * 		The instance of the class.
	 */
	public Expression addParameter(Expression param) {
		params.add(param);
		return this;
	}

	/**
	 * Appends a list of parameters to the end of the parameters' list.
	 * @param parameters
	 * 		: The list of parameters to be appended.
	 */
	public void addParameters(List<Expression> parameters) {
		params.addAll(parameters);
	}

	/**
	 * Gets the list of parameters.
	 * @return
	 * 		The list of parameters.
	 */
	public List<Expression> getParameters() {
		return params;
	}
}