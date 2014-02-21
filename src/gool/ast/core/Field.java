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

import java.util.Collection;

/**
 * This class captures a field (i.e. an attribute) declaration in abstract GOOL.
 * Hence it is a Dec.
 */
public final class Field extends Dec {
	
	/**
	 * The default value of the field.
	 */
	private Expression defaultValue;

	/**
	 * The constructor of a field.
	 * @param name
	 * 		: The name of the field.
	 * @param type
	 * 		: The type of the field.
	 * @param defaultValue
	 * 		: The default value of the field.
	 */
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

	/**
	 * The constructor of a field.
	 * @param listModifiers
	 * 		: The modifiers list associate to the field.
	 *  @param name
	 * 		: The name of the field.
	 * @param type
	 * 		: The type of the field.
	 * @param initialValue
	 * 		: The default value of the field.
	 */
	public Field(Collection<Modifier> listModifiers, String name, IType type,
			Expression initialValue) {
		this(name, type, initialValue);
		addModifiers(listModifiers);
	}

	/**
	 * The constructor of a field.
	 * @param modifier
	 * 		: The modifier associate to the field.
	 *  @param name
	 * 		: The name of the field.
	 * @param type
	 * 		: The type of the field.
	 */
	public Field(Modifier modifier, String name, IType type) {
		this(modifier, name, type, null);
	}

	/**
	 * The constructor of a field.
	 * @param modifiers
	 * 		: The modifiers list associate to the field.
	 * @param var
	 * 		: The variable declaration in the field.
	 */
	public Field(Collection<Modifier> modifiers, VarDeclaration var) {
		this(modifiers, var.getName(), var.getType(), var.getInitialValue());
	}

	/**
	 * Sets the default value in the field.
	 * @param defaultValue
	 * 		: The new default value in the field.
	 */
	public void setDefaultValue(Expression defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the default value in the field.
	 * @return
	 * 		The default value in the field.
	 */
	public Expression getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
