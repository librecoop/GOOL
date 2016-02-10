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

/**
 * See Dependency for comments.
 */
public class UnrecognizedDependency extends Dependency {

	/**
	 * The name of the unrecognized dependency.
	 */
	private String name;

	/**
	 * The constructor of an "unrecognized dependency" representation.
	 * @param name
	 */
	public UnrecognizedDependency(String name) {
		this.name = name;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	/**
	 * Gets the name of the unrecognized dependency.
	 * @return
	 * 		The name of the unrecognized dependency.
	 */
	public String getName() {
		return name;
	}
}
