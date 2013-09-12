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

/**
 * This class captures function invocation. Hence is is an OOTExpr.
 * 
 * @param T
 *            is the return type, , if known at compile time, otherwise put
 *            OOTType. That way java generics grant us some level of type
 *            checking of the generated code at compiler design time. Sometimes
 *            we will not be able to use this though, because we will not know T
 *            at compiler design time.
 */
public class MethCall extends Parameterizable {

	/**
	 * The target object.
	 */
	private Expression target;
	private String generalName;
	private String library;

	public MethCall(IType type, Expression target) {
		super(type);
		this.target = target;
	}

	private MethCall(IType type, Expression target, Expression param) {
		this(type, target);
		addParameter(param);
	}

	// public MethCall(IType type, Expression target, String methodName,
	// Expression... params) {
	// this(type, new MemberSelect(type, target, methodName));
	// if (params != null){
	// addParameters(Arrays.asList(params));
	// }
	//
	// }

	public MethCall(IType type, String name) {
		this(type, new Identifier(type, name));
	}

	public MethCall(IType type, Expression target, String generalName,
			String library) {
		this(type, target);
		this.generalName = generalName;
		this.library = library;
	}

	public static MethCall create(IType type, Expression target, Meth meth,
			Expression param) {
		return new MethCall(type, new FieldAccess(target.getType(), target,
				meth.getName()), param);
	}

	public static MethCall create(IType type, Expression expression) {
		return new MethCall(type, expression);
	}

	public Expression getTarget() {
		return target;
	}

	public String getGeneralName() {
		return this.generalName;
	}

	public String getLibrary() {
		return this.library;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
