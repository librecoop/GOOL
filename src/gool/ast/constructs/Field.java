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

package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

import java.util.Collection;

/**
 * This class captures a field (i.e. an attribute) declaration in abstract GOOL.
 * Hence it is a Dec.
 */
public final class Field extends Dec {
	private Expression defaultValue;

	public Field(String name, IType type, Expression defaultValue) {
		super(type, name);
		this.defaultValue = defaultValue;
	}

	/**
	 * The type of the variable is T.
	 * 
	 * @param modifiers
	 *            codes for the visibility of the variable and so on
	 * @param name
	 *            codes for the name of the variable
	 */
	public Field(Modifier modifier, String name, IType type,
			Expression defaultValue) {
		this(name, type, defaultValue);
		addModifier(modifier);
	}

	public Field(Collection<Modifier> listModifiers, String name, IType type,
			Expression initialValue) {
		this(name, type, initialValue);
		addModifiers(listModifiers);
	}

	public Field(Modifier modifier, String name, IType type) {
		this(modifier, name, type, null);
	}

	public Field(Collection<Modifier> modifiers, VarDeclaration var) {
		this(modifiers, var.getName(), var.getType(), var.getInitialValue());
	}

	public void setDefaultValue(Expression defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Expression getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
