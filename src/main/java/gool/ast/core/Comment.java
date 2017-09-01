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

import gool.ast.type.TypeNone;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 * This class captures all the expressions of the intermediate language which we
 * have not bothered to represent in the Abstract Syntax Tree. Its code will
 * compile in the target language just as it is. It is an OOTExpr.
 * 
 * @param T
 *            is the type of this expression, if known at compile time,
 *            otherwise put OOTType. That way java generics grant us some level
 *            of type checking of the generated code at compiler design time.
 *            Sometimes we will not be able to use this though, because we will
 *            not know T at compiler design time.
 */
public class Comment extends Expression {

	/**
	 * The constant value.
	 */
	private String value;

	/**
	 * The type of the expression.
	 */

	/**
	 * The type of the return value is T
	 * 
	 * @param type
	 * @param code
	 */
	public Comment(String code) {
		super(TypeNone.INSTANCE);
		this.value = code;
	}

	/**
	 * Gets the code in the comment node.
	 * @return
	 * 		The code in the comment node.
	 */
	public String getValue() {
		return value;
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

}
