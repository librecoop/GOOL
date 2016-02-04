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
	
	/**
	 * The general name of the method invocation.
	 */
	private String generalName;
	
	/**
	 * The name of the library for the method invocation.
	 */
	private String library;
	
	/**
	 * The name of the gool library for the method invocation.
	 * Use it if you use the gool library manager.
	 */
	private String goolLibraryMethod;

	/**
	 * The constructor of a method's invocation representation.
	 * @param type
	 * 		: The type of the target.
	 * @param target
	 * 		: The target object in the method invocation.
	 */
	public MethCall(IType type, Expression target) {
		super(type);
		this.target = target;
	}

	/**
	 * The constructor of a method's invocation representation.
	 * @param type
	 * 		: The type of the target.
	 * @param target
	 * 		: The target object in the method invocation.
	 * @param param
	 * 		: The parameter in the method invocation.
	 */
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

	/**
	 * The constructor of a method's invocation representation.
	 * @param type
	 * 		: The type of the target.
	 * @param name
	 * 		: The name of the target.
	 */
	public MethCall(IType type, String name) {
		this(type, new Identifier(type, name));
	}

	/**
	 * The constructor of a method's invocation representation.
	 * @param type
	 * 		: The type of the target.
	 * @param target
	 * 		: The target object in the method invocation.
	 * @param generalName
	 * 		: The general name of the method invocation.
	 * @param library
	 * 		: The name of the gool library for the method invocation.
	 */
	public MethCall(IType type, Expression target, String generalName,
			String library) {
		this(type, target);
		this.generalName = generalName;
		this.library = library;
	}

	/**
	 * Creator for a method call.
	 * @param type
	 * 		: The type of the target.
	 * @param target
	 * 		: The target object in the method invocation.
	 * @param meth
	 * 		: The method declaration.
	 * @param param
	 * 		: The parameter in the method invocation.
	 * @return
	 * 		A instance of a method call with the appropriate fields.
	 */
	public static MethCall create(IType type, Expression target, Meth meth,
			Expression param) {
		return new MethCall(type, new FieldAccess(target.getType(), target,
				meth.getName()), param);
	}

	/**
	 * Creator for a method call.
	 * @param type
	 * 		: The type of the target.
	 * @param expression
	 * 		: The target object in the method invocation.
	 * @return
	 * 		A instance of a method call with the appropriate fields.
	 */
	public static MethCall create(IType type, Expression expression) {
		return new MethCall(type, expression);
	}

	/**
	 * Gets the target object in the method invocation.
	 * @return
	 * 		The target object in the method invocation.
	 */
	public Expression getTarget() {
		return target;
	}

	/**
	 * Gets the general name in the method invocation.
	 * @return
	 * 		The general name in the method invocation.
	 */
	public String getGeneralName() {
		return this.generalName;
	}

	/**
	 * Gets the name of the library for the method invocation.
	 * @return
	 * 		The name of the library for the method invocation.
	 */
	public String getLibrary() {
		return this.library;
	}

	/**
	 * Sets the mapping gool library method.
	 * @param goolLibraryMethod
	 * 		: The name of gool library method to change.
	 */
	public void setGoolLibraryMethod(String goolLibraryMethod){
		this.goolLibraryMethod = goolLibraryMethod;
	}
	
	/**
	 * Gets the gool library method in the method invocation.
	 * @return
	 * 			The gool library method in the method invocation.
	 */
	public String getGoolLibraryMethod(){
		return this.goolLibraryMethod;
	}
	
}
