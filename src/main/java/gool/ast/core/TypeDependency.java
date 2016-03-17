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

/**
 * This class defines the type of a dependency.
 */
public class TypeDependency extends Dependency {

	/**
	 * The type of a dependency.
	 */
	private IType type;

	
	/**
	 * Flag for personal dependencies (helpful for C++ inclusions)
	 */
	private boolean personal = false;
	
	
	public boolean isPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	/**
	 * The constructor for a type of dependency.
	 * @param type
	 * 		: The type of a dependency.
	 */
	public TypeDependency(IType type) {
		this.type = type;
	}

	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName() + "." + type.getName();
		}
		return cg.getCode(this);
	}

	/**
	 * Gets the type of a dependency.
	 * @return
	 * 		The type of a dependency.
	 */
	public IType getType() {
		return type;
	}

}
