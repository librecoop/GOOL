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

import gool.generator.GoolGeneratorController;

/**
 * This class is for array types.
 */
public class TypeArray extends ReferenceType {

	/**
	 * The type of the elements.
	 */
	private IType elementType;

	/**
	 * The construtor of an "array type" representation.
	 * @param elementType
	 * 			: The element type used by the array.
	 */
	public TypeArray(IType elementType) {
		setElementType(elementType);
	}

	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

	/**
	 * Sets the type of the elements used by the array.
	 * @param elementType
	 * 			: The new type of the elements used by the array.
	 */
	public final void setElementType(IType elementType) {
		this.elementType = elementType;
	}

	/**
	 * Gets the type of the elements used by the array.
	 * @return
	 * 		The type of the elements used by the array.
	 */
	public final IType getElementType() {
		return elementType;
	}

	@Override
	public String callGetCode() {
		return getName();
	}

}
