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

import gool.ast.core.ClassDef;
import gool.ast.core.Dependency;
import gool.ast.core.RecognizedDependency;
import gool.ast.type.IType;
import gool.generator.common.CodePrinter;
import gool.generator.common.GeneratorMatcher;
import gool.generator.common.Platform;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logger.Log;

import org.apache.commons.lang.StringUtils;


/**
 * This class helps generate the concrete target from the abstract GOOL It has a
 * method starting the entire process As well as some ancillary methods
 */
public class GeneratorHelper {

	public static String joinParams(List<?> parameters) {
		if (parameters == null) {
			return "";
		}
		return StringUtils.join(parameters, ", ");
	}

	/**
	 * Used by velocity via the templates (e.g. templates/class.vm) TODO: Might
	 * have been better placed in the CodePrinter? Generated the code for the
	 * imports of a class
	 * 
	 * @param classDef
	 * @return a set of strings, each importing one dependency
	 */
	public static Set<String> printDependencies(ClassDef classDef) {
		Set<String> result = new HashSet<String>();
		// go through each dependency, produce its toString, add it to the set.
		for (Dependency dep : classDef.getDependencies()) {
			if (!(dep instanceof RecognizedDependency) && !dep.toString().equals(classDef.toString())) {
				String s=dep.toString();
				Log.d("<GeneratorHelper - printDependencies> print " + s);
				result.add(s);
			}
		}

		return result;
	}

	public static String printRecognizedDependencies(ClassDef classDef) {
		String result = "";
		//List<String> dependencies = new ArrayList<String>();
		// go through each dependency, produce its toString, add it to the set.
		List<Dependency> dependencies = classDef.getDependencies();
			for (Dependency dep : dependencies) {
			if (dep instanceof RecognizedDependency) {
				result+=dep.toString()+"\n";
				Log.d("<GeneratorHelper - printRecognizedDependencies> print " + result);
			}
		}
		return result;
	}

	/**
	 * This is the entry point. It generates code for the abstract GOOL classes
	 * classDefs Listing them as a map (filename, code) in case there where
	 * different target platforms For different classes.
	 */
	public static Map<String, String> printClassDefs(
			Collection<ClassDef> classDefs){
		Log.d("\n\n****************** Start Print *******************\n\n");
		Map<String, String> compilationUnits = new HashMap<String, String>();

		for (ClassDef classDef : classDefs) {
			Log.d("<GeneratorHelper - printClassDefs> Prepare the print of classDef " + classDef.getName());
			// The target platform is held by the GOOL class, retrieve it.
			Platform platform = (Platform) classDef.getPlatform();
			// Get a codePrinter corresponding to that platform.
			CodePrinter currentPrinter = CodePrinter.getPrinter(platform);
			// Just compile each abstract GOOL class and add it to the map.
			GeneratorMatcher.init(platform);
			Log.d(String.format("<GeneratorHelper - printClassDefs> Print of classDef %s with printer %s",
					classDef.getName(), currentPrinter.toString()));
			compilationUnits.putAll(currentPrinter.print(classDef));
		}

		Log.d("\n\n****************** End Print *******************\n\n");
		return compilationUnits;
	}

	// TODO => use in OBJC macro for delete pointer in param list for the name
	// of a function.
	// Should be use in the OBJCGeneratorHelper
	public static String removePointer(String s) {
		return s.replaceAll("[\\s*]+$", "");
	}

	// idem
	public static String removePointer(IType type) {
		return removePointer(type.toString());
	}
	
	public static boolean generatingMainMethod = false;

}
