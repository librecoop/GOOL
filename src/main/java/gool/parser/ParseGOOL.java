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

package gool.parser;

import gool.ast.core.ClassDef;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * This interface must be implemented by the parser input language
 */
public abstract class ParseGOOL {
	
	/**
	 * Parsing concrete Language into abstract GOOL.
	 * 
	 * @param inputFiles
	 *            : the concrete Language, as files
	 * @return a list of classdefs, i.e. of abstract GOOL classes.
	 * @throws Exception
	 */
	public abstract Collection<ClassDef> parseGool(
			Collection<? extends File> inputFiles) throws Exception ;
	
	/**
	 * If the parser is called on a map containing file names and associated codes,
	 * wrap it into compilation units, and call the parser on that files.
	 */
	public abstract Collection<ClassDef> parseGool(
			Map<String, String> input) throws Exception;
}
