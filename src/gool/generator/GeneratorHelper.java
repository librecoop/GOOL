/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
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

import gool.GOOLCompiler;
import gool.GoolLibDefs.GoolClassAstBuilder;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.RecognizedDependency;
import gool.generator.android.AndroidCodePrinter;
import gool.generator.android.AndroidPlatform;
import gool.ast.type.IType;
import gool.generator.common.CodePrinter;
import gool.generator.common.GeneratorMatcher;
import gool.generator.common.Platform;
import gool.methods.MethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logger.Log;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.log4j.Logger;

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
			if (!dep.toString().equals(classDef.toString())) {
				String s=dep.toString();
				result.add(s);
				System.out.println("[printDependencies] dependance ajoutée à la classe "+classDef.getName()+": "+s);
			}
		}

		return result;
	}

	/**
	 * This is the entry point. It generates code for the abstract GOOL classes
	 * classDefs Listing them as a map (platform, file) in case there where
	 * different target platforms For different classes.
	 */
	public static Map<Platform, List<File>> printClassDefs(
			Collection<ClassDef> classDefs) throws FileNotFoundException {
		Map<Platform, List<File>> compilationUnits = new HashMap<Platform, List<File>>();

		MethodManager.reset();
		for (ClassDef classDef : classDefs) {

			// The target platform is held by the GOOL class, retrieve it.
			Platform platform = (Platform) classDef.getPlatform();
			// Get a codePrinter corresponding to that platform.
			CodePrinter currentPrinter = CodePrinter.getPrinter(platform);
			// If that platform is not yet in the map, add it.
			if (!compilationUnits.containsKey(platform)) {
				compilationUnits.put(platform, new ArrayList<File>());
			}
			// If the target output directory is not there yet, make it.
			if (!currentPrinter.getOutputDir().exists()) {
				Log.i("Creating the output directory "
						+ currentPrinter.getOutputDir());
				currentPrinter.getOutputDir().mkdirs();
			}

			// Just compile each abstract GOOL class and add it to the map.

			// try {
			
			
			GeneratorMatcher.init(platform);
			compilationUnits.get(platform).addAll(
					currentPrinter.print(classDef));
			
			
			// } catch (ResourceNotFoundException e) {
			// Log.e(String.format(
			// "Impossible to produce file '%s': platforms should" +
			// " either implements CodeGeneratorNoVelocity" +
			// " or have a 'class.vm' template.",
			// currentPrinter.getFileName(classDef.getName())));
			// }
			// compilationUnits.get(platform).addAll(
			// currentPrinter.printPersonalLib());

		}

		// If the platform is android a project has to be created and the files
		// created
		// above copied into the project.
		if (compilationUnits.containsKey(AndroidPlatform.getInstance())) {
			Platform platform = AndroidPlatform.getInstance();
			AndroidCodePrinter currentPrinter = (AndroidCodePrinter) CodePrinter
					.getPrinter(platform);
			List<File> newFileList = currentPrinter
					.createAndroidProject(compilationUnits.get(platform));
			compilationUnits.put(platform, newFileList);
		}
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

}
