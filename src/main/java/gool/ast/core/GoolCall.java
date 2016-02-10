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
import gool.generator.GoolGeneratorController;

/**
 * This captures the invocation of a gool class method.
 */
public class GoolCall extends Parameterizable {

	/**
	 * The name of the method in the gool libraries.
	 */
	private String method;

	/**
	 * The constructor of a gool call representation.
	 * @param type
	 * 		: The type for parameterizable.
	 */
	protected GoolCall(IType type) {
		super(type);
	}

	/**
	 * The constructor of a gool call representation.
	 * @param type
	 * 		: The type for parameterizable.
	 * @param method
	 * 		: The name of the method in the gool libraries.
	 */
	public GoolCall(IType type, String method) {
		super(type);
		this.method = method;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	/**
	 * Gets the name of the method used in the gool call.
	 * @return
	 * 		The name of the method defined in the gool libraries.
	 */
	public String getMethod() {
		return method;
	}
}
