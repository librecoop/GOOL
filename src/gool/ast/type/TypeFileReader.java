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

public class TypeFileReader extends ReferenceType {

	/**
	 * The class where the list was defined.
	 */
	private ClassDef classDef;

	public TypeFileReader() {
	}

	public TypeFileReader(IType elementType) {
		this();
		addArgument(elementType);
	}

	public IType getElementType() {
		if (getTypeArguments().size() > 0) {
			return getTypeArguments().get(0);
		}
		return null;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public ClassDef getClassDef() {
		return classDef;
	}

	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeFile
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