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



/**
 * This class accounts for a catch block in the intermediate language.
 */
public class Catch extends Statement {
	
	/**
	 * Catch parameter
	 */
	private VarDeclaration parameter;

	/**
	 * Catch instruction block
	 */
	private Block block;

	/**
	 * The constructor of a catch block representation.
	 * @param parameter
	 * 			: The declaration of the catch parameter.
	 * @param block
	 * 			: The catch instruction block.
	 */
	public Catch(VarDeclaration parameter, Block block) {
		this.parameter = parameter;
		this.block = block;
	}

	/**
	 * Gets the catch parameter.
	 * @return
	 * 		The catch parameter
	 */
	public VarDeclaration getParameter() {
		return parameter;
	}

	/**
	 * Gets the catch instruction block.
	 * @return
	 * 		The catch instruction block.
	 */
	public Block getBlock() {
		return block;
	}

}