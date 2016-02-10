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
 * This is the basic type Void of the intermediate language.
 */
public final class TypeVoid extends PrimitiveType {

	/**
	 * A static instance to avoid the creation of new objects.
	 */
	public static final TypeVoid INSTANCE = new TypeVoid();

	/**
	 * The empty constructor used to initialize the instance.
	 */
	private TypeVoid() {
	}

	@Override
	public String callGetCode() {
		return getName();
	}

	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
