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

import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This captures packages in the intermediate language. For each object member
 * of Package the compiler will have to generate a separate folder containing
 * classes and packages in the target language.
 */
public class Package extends Dependency {

	/**
	 * The list of classes that belong to the package.
	 */
	private List<ClassDef> classes = new ArrayList<ClassDef>();
	
	/**
	 * The name of the package.
	 */
	private String name;

	/**
	 * The constructor of a package representation.
	 * @param packageName
	 * 		: The name of the package.
	 */
	public Package(String packageName) {
		this.name = packageName;
	}

	/**
	 * Gets the name of the package.
	 * @return
	 * 		The name of the package.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a class definition in the package.
	 * @param mclass
	 * 		: The class definition to add.
	 * @return
	 * 		An error if there are problems.
	 */
	public final boolean addClass(ClassDef mclass) {
		return classes.add(mclass);
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
