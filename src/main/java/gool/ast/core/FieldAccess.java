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

/**
 * Represents member invocation over class members. For example, in Java they
 * are known as static methods or attributes.
 */
public class FieldAccess extends Expression {

	/**
	 * The target object.
	 */
	private Expression target;
	
	/**
	 * The target member to be called.
	 */
	private String member;

	/**
	 * The parameters needed to call the member.
	 */
	public FieldAccess(IType type, Expression target, String member) {
		super(type);
		this.target = target;
		this.member = member;
	}

	/**
	 * Gets the target member to be called.
	 * @return
	 * 		The target member to be called in an field access.
	 */
	public String getMember() {
		return member;
	}

	/**
	 * Gets the target object.
	 * @return
	 * 		The target object in an field access.
	 */
	public Expression getTarget() {
		return target;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
