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
import gool.generator.common.CodeGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * This class captures the "new" of the intermediate language, i.e object
 * 
 * instantiation from a class definition Hence is is an OOTExpr.
 * 
 * 
 * 
 * @param T
 * 
 *            is the return type In generic OO languages each object
 * 
 *            instantiations may generate new types, hence this is an OOType.
 */

public class ClassNew extends Parameterizable {

	/**
	 * 
	 * Creates an instance with a class name and a type.
	 * 
	 * @param name
	 *            the class name.
	 * 
	 * @param type
	 *            the type of the instance.
	 */
	public ClassNew(IType type) {
		super(type);
	}

	/**
	 * 
	 * Creates an instance with a defining class and a list of parameters.
	 * 
	 * 
	 * 
	 * @param method
	 * 
	 *            is a pointer to the previous declaration of the class to be
	 * 
	 *            instantiated.
	 * 
	 * @param params
	 * 
	 *            are the constructor's parameters
	 */
	public ClassNew(ClassDef mclass, List<Expression> params) {
		this(mclass.getType());
		addParameters(params);
	}

	/**
	 * 
	 * Creates an instance with a type and a list of parameters.
	 * 
	 * 
	 * 
	 * @param type
	 * 
	 *            : Type of the class.
	 * 
	 * @param params
	 * 
	 *            : Are the constructor's parameters
	 */
	public ClassNew(IType type, List<Expression> params) {
		this(type);
		addParameters(params);
	}
	
	/**
	 * 
	 * Creates an instance with a type and a parameter.
	 * 
	 * 
	 * 
	 * @param type
	 * 
	 *            : Type of the class.
	 * 
	 * @param params
	 * 
	 *            : Are the constructor's parameter
	 */
	public ClassNew(IType type, Expression expression) {
		this(type);
		addParameter(expression);
	}
	
	/**
	 * 
	 * Creates an instance with a type and optional parameters.
	 * 
	 * 
	 * 
	 * @param type
	 * 
	 *            : Type of the class.
	 * 
	 * @param params
	 * 
	 *            : Are the constructor's parameters
	 */
	public ClassNew(IType type, Expression... paramArray) {
		this(type);
		if (paramArray != null) {
			addParameters(Arrays.asList(paramArray));
		}
	}

	/**
	 * Gets the class name.
	 * @return
	 * 		The class name used in the "new".
	 */
	public String getName() {
		return getType().getName();
	}

	/**
	 * Gets the parameters of the instance.
	 * @return
	 * 		The parameters list of the instance.
	 */
	public List<IType> getTypeArguments() {
		return getType().getTypeArguments();
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
