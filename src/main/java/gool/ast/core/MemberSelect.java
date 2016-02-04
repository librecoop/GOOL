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
 * This class captures variable content invocation
 * with the name of the identifier used.
 */
public class MemberSelect extends VarAccess {
	
	/**
	 * The target expression in the variable access.
	 */
	private Expression target;

	/**
	 * The constructor of an member select representation.
	 * @param target
	 * 			: The target expression in the variable access.
	 * @param var
	 * 			: The variable declaration in the variable access.
	 */
	public MemberSelect(Expression target, Dec var) {
		super(var);
		this.target = target;
	}

	/**
	 * Gets the target expression in the variable access.
	 * @return
	 * 		The target expression in the variable access.
	 */
	public Expression getTarget() {
		return target;
	}

	/**
	 * Gets the name of the identifier (the variable).
	 * @return
	 * 		The name of the identifier in the member select.
	 */
	public String getIdentifier() {
		return var.getName();
	}

	/**
	 * Sets the name of the identifier (the variable).
	 * @param newName
	 * 		: The new name of the identifier in the member select.
	 */
	public void setIdentifier(String newName) {
		var.setName(newName);
	}
}
