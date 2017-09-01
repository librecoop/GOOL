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

/**
 * This class defines an operation used by the intermediate language.
 */
public abstract class Operation extends Expression {

	/**
	 * The operator used in the operation.
	 */
	private Operator operator;
	
	/**
	 * The textual operator.
	 */
	private String textualoperator;

	/**
	 * The constructor of an operation representation.
	 * @param operator
	 * 		: The operator used in the operation.
	 * @param type
	 * 		: The type of the operator used in the operation.
	 * @param textualoperator
	 * 		: The textual operator used.
	 */
	protected Operation(Operator operator, IType type, String textualoperator) {
		super(type);
		this.operator = operator;
		this.textualoperator = textualoperator;
	}

	/**
	 * Gets the operator used in the operation.
	 * @return
	 * 		The operator used in the operation.
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Gets the textual operator used in the operation.
	 * @return
	 * 		The textual operator used in the operation.
	 */
	public String getTextualoperator() {
		return textualoperator;
	}

}
