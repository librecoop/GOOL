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

/**
 * This is the basic type Method of the intermediate language.
 */
public final class TypeVar extends IType {
	
	/**
	 * The textual name of the type.
	 */
	private String textualtype;

	/**
	 * Gets the textual name of the type.
	 * @return
	 * 		The textual name of the type.
	 */
	public String getTextualtype() {
		return textualtype;
	}

	/**
	 * The constructor of an "type variable" representation.
	 * @param textualtype
	 * 		: The textual name of the type.
	 */
	public TypeVar(String textualtype) {
		this.textualtype = textualtype;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
