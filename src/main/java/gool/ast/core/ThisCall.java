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
 * This class captures the invocation of a "this".
 * For example, it captures
 * {@code
 * this.my_attribute 
 * or
 * this()
 * }
 */
public class ThisCall extends InitCall {

	/**
	 * The constructor of a "this" representation.
	 * @param type
	 * 		: The type of the target.
	 * @param target
	 * 		: The target expression used by the "this" invocation.
	 */
	private ThisCall(IType type, Expression target) {
		super(type, target);
	}
	
	/**
	 * The constructor of a "this" representation.
	 * @param type
	 * 		: The type of the target used by the "this" invocation.
	 */
	public ThisCall(IType type) {
		super(type, null);
	}

}
