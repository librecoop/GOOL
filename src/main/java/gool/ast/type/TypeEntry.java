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

import gool.ast.core.ClassDef;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 * This is the basic type for map entry in the intermediate language.
 */
public class TypeEntry extends ReferenceType {

	/**
	 * The class where the map was defined.
	 */
	private ClassDef classDef;

	/**
	 * The empty constructor of a "type entry" representation.
	 */
	public TypeEntry() {
	}

	/**
	 * The constructor of a "type entry" representation.
	 * @param keyType
	 * 		: The type of the key used by the map.
	 * @param elementType
	 * 		: The type of the element used by the map.
	 */
	public TypeEntry(IType keyType, IType elementType) {
		this();
		addArgument(keyType);
		addArgument(elementType);
	}

	/**
	 * Gets the type of the element used by the map.
	 * @return
	 * 		The type of the element used by the map.
	 */
	public IType getElementType() {
		if (getTypeArguments().size() > 1) {
			return getTypeArguments().get(1);
		}
		return null;
	}

	/**
	 * Gets the type of the key used by the map.
	 * @return
	 * 		The type of the key used by the map.
	 */
	public IType getKeyType() {
		if (getTypeArguments().size() > 1) {
			return getTypeArguments().get(0);
		}
		return null;
	}

	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName();
		}
		return cg.getCode(this);
	}

	/**
	 * Gets the class' definition where the map was defined.
	 * @return
	 * 		The class' definition where the map was defined.
	 */
	public ClassDef getClassDef() {
		return classDef;
	}

	/**
	 * Sets the class' definition where the map was defined.
	 * @param classDef
	 * 		: The new class' definition where the map was defined.
	 */
	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeEntry
				&& getName().equals(((IType) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String getName() {
		return this.callGetCode();
	}

}