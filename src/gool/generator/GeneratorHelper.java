package gool.generator;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Dependency;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class helps generate the concrete target
 * from the abstract GOOL
 * It has a method starting the entire process
 * As well as some ancillary methods
 */
public final class GeneratorHelper {
	public static Logger logger = Logger.getLogger(GeneratorHelper.class.getName());
	public static String joinParams(List<?> parameters) {
		if (parameters == null) {
			return "";
		}
		return StringUtils.join(parameters, ", ");
	}
	
	/**
	 * Used by velocity via the templates (e.g. templates/class.vm)
	 * TODO: Might have been better placed in the CodePrinter?
	 * Generated the code for the imports of a class
	 * @param classDef
	 * @return a set of strings, each importing one dependency
	 */
	public static Set<String> printDependencies(ClassDef classDef) {
		Set<String> result = new HashSet<String>();
		//go through each dependency, produce its toString, add it to the set.
		for (Dependency dep : classDef.getDependencies()) {
			if (!dep.toString().equals(classDef.toString())){
				result.add(dep.toString());
			}
		}
		return result;
	}

		/**
		 * This is the entry point.
		 * It generates code for the abstract GOOL classes classDefs
		 * Listing them as a map (platform, file) in case there where different target platforms
		 * For different classes.
		 */
	public static Map<Platform, List<File>> printClassDefs(
			Collection<ClassDef> classDefs)
			throws FileNotFoundException {
		Map<Platform, List<File>> compilationUnits = new HashMap<Platform, List<File>>();

		for (ClassDef classDef : classDefs) {
			//The target platform is held by the GOOL class, retrieve it.
			Platform platform = (Platform) classDef.getPlatform();
			//Get a codePrinter corresponding to that platform.
			CodePrinter currentPrinter = CodePrinter.getPrinter(platform);
			//If that platform is not yet in the map, add it.
			if (!compilationUnits.containsKey(platform)) {
				compilationUnits.put(platform, new ArrayList<File>());
			}
			//If the target output directory is not there yet, make it.
			if (!currentPrinter.getOutputDir().exists()) {
				logger.info("Creating the output directory "
						+ currentPrinter.getOutputDir());
				currentPrinter.getOutputDir().mkdirs();
			}

			//Just compile each abstract GOOL class and add it to the map.
			compilationUnits.get(platform).addAll(
					currentPrinter.print(classDef));
		}
		return compilationUnits;
	}


}
