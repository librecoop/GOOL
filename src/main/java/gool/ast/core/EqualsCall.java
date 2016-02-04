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

import gool.ast.type.TypeBool;

/**
 * This class captures an equals call in the intermediate language. 
 * For exemple, this class captures
 * {@code
 * my_object.equals(my_target);
 * }
 */
public class EqualsCall extends Parameterizable {

	/**
	 * The target expression in the equals call representation.
	 */
	private Expression target;

	/**
	 * The constructor of an equals call representation.
	 * @param target
	 * 		: The target expression in the equals call representation.
	 */
	public EqualsCall(Expression target) {
		super(TypeBool.INSTANCE);
		this.target = target;
	}

	/**
	 * Gets the target expression in the equals call representation.
	 * @return
	 * 		The target expression in the equals call representation.
	 */
	public Expression getTarget() {
		return target;
	}

}
