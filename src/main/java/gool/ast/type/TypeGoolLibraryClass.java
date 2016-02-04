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

package gool.ast.type;

import logger.Log;

/**
 * This is the basic type for gool library class in the intermediate language.
 */
public final class TypeGoolLibraryClass extends ReferenceType {

	/**
	 * The name of the gool library class.
	 */
	private String goolclassname;

	/**
	 * Gets the name of the gool library class.
	 * @return
	 * 		The name of the gool library class.
	 */
	public String getGoolclassname() {
		return goolclassname;
	}

	/**
	 * The constructor of a "type gool library class" representation.
	 * @param goolclassname
	 * 		: The name of the gool library class.
	 */
	public TypeGoolLibraryClass(String goolclassname) {
		this.goolclassname = goolclassname;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}