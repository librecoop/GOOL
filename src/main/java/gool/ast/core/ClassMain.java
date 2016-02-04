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
 * This class captures the "main class" of the intermediate language, 
 * i.e the definition of a main in a class declaration.
 */
public class ClassMain extends ClassDef {

	/**
	 * The Main block of the program. 
	 */
	private Block mainBlock;

	/**
	 *  The constructor of a main class representation.
	 * @param name
	 * 			: The name of the class.
	 * @param platform
	 * 			: The destination platform.
	 */
	public ClassMain(String name) {
		super(Modifier.PUBLIC, name);
	}

	/**
	 * Gets the main block of the program.
	 * @return
	 * 			The main block of the program.
	 */
	public Block getMainBlock() {
		return mainBlock;
	}

	/**
	 * Sets the main block of the main class.
	 * @param mainBlock
	 * 		The main block to insert.
	 */
	public void setMainBlock(Block mainBlock) {
		this.mainBlock = mainBlock;
	}

}
