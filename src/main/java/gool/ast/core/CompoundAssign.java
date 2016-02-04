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
 * This class accounts for compound assignments in the intermediate language. Hence it is
 * a Assign.
 */
public class CompoundAssign extends Assign {

	/**
	 * The operator used in the compound assignment.
	 */
	private Operator operator;
	
	/**
	 * The symbol of the operator used in the compound assignment.
	 */
	private String textualoperator;
	
	/**
	 * The type of the operator used in the compound assignment.
	 */
	private IType type;

	/**
	 * 
	 * @param var
	 * 			: The variable assigned.
	 * @param value
	 * 			: The value assigned to the variable.
	 * @param operator
	 * 			: The operator used in the compound assignment.
	 * @param textualoperator
	 * 			: The symbol of the operator used in the compound assignment.
	 * @param type
	 * 			: The type of the operator used in the compound assignment.
	 */
	public CompoundAssign(Node var, Expression value, Operator operator,
			String textualoperator, IType type) {
		super(var, value);
		this.operator = operator;
		this.textualoperator = textualoperator;
		this.type = type;
	}

	/**
	 * Gets the operator used in the compound assignment.
	 * @return
	 * 		The operator used in the compound assignment.
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Gets the symbol of the operator used in the compound assignment.
	 * @return
	 * 		The symbol of the operator used in the compound assignment.
	 */
	public String getTextualoperator() {
		return textualoperator;
	}

	/**
	 * Gets the type of the operator used in the compound assignment.
	 * @return
	 * 		The type of the operator used in the compound assignment.
	 */
	public IType getType() {
		return type;
	}


}
