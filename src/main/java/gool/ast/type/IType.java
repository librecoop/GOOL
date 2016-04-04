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

import gool.ast.core.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface accounts for all types in the intermediate language.
 */
public abstract class IType extends Node {

	/**
	 * The type arguments. It is used to store 'internal' types for List, Maps,
	 * etc.
	 */
	private List<IType> typeArguments = new ArrayList<IType>();

	/**
	 * Gets the type's name.
	 * 
	 * @return a string representing the type's name.
	 */
	public abstract String getName();

	/**
	 * Gets the arguments of a type.
	 * 
	 * @return a list containing all the type arguments.
	 */

	public List<IType> getTypeArguments() {
		return typeArguments;
	}

	/**
	 * Add a new type argument to the current type.
	 * 
	 * @param type
	 *            the type to be added.
	 */
	public void addArgument(IType type) {
		getTypeArguments().add(type);
	}

	/**
	 * Adds a collection of type arguments.
	 * 
	 * @param params
	 *            the type collection to be added.
	 */
	public void addArguments(List<IType> params) {
		getTypeArguments().addAll(params);
	}

}