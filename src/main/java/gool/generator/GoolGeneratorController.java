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

package gool.generator;

import gool.generator.common.CodeGenerator;

/**
 * Which CodeGenerator to use is specified by the CodePrinter, which is
 * specified by the Platform, which is held at the level of the class that is
 * being translated from abstract GOOL to concrete Target. In order to remember
 * which one this is, as we travel this class, we keep it here. By default, the
 * CodeGenerator is GoolGenerator, i.e. just the GOOL pretty printer. TODO:
 * Maybe this file should be merged the GeneratorHelper
 * 
 * @author parrighi
 */
public class GoolGeneratorController {
	/**
	 * The current code generator.
	 */
	private static CodeGenerator currentGenerator = null;

	/**
	 * Specifies the new code generator.
	 * 
	 * @param generator
	 *            a new code generator.
	 */
	public static void setCodeGenerator(CodeGenerator generator) {
		currentGenerator = generator;
	}

	/**
	 * Gets the current code generator.
	 * 
	 * @return the current code generator.
	 */
	public static CodeGenerator generator() {
		if (currentGenerator == null) {
			throw new IllegalStateException(
					"The code generator is not properly initialized.");
		}
		return currentGenerator;
	}

	public static void reset() {
		currentGenerator = null;
	}
}
