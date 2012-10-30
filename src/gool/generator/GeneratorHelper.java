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

public final class GeneratorHelper {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(GeneratorHelper.class);

	public static String joinParams(List<?> parameters) {
		if (parameters == null) {
			return "";
		}
		return StringUtils.join(parameters, ", ");
	}
	
	// Used by velocity at the templates (ie. templates/class.vm)
	public static Set<String> printDependencies(ClassDef classDef) {
		Set<String> result = new HashSet<String>();
		for (Dependency dep : classDef.getDependencies()) {
			if (!dep.getFullName().equals(classDef.getFullName())){
				result.add(dep.getFullName());
			}
		}
		return result;
	}

	public static Map<Platform, List<File>> printClassDefs(
			Collection<ClassDef> classDefs)
			throws FileNotFoundException {
		Map<Platform, List<File>> compilationUnits = new HashMap<Platform, List<File>>();


		/*
		 * If the target platform is GOOL, we force that all the generated
		 * classes should be printed using the GoolPrinter.
		 */

		for (ClassDef classDef : classDefs) {
			Platform platform = (Platform) classDef.getPlatform();
			CodePrinter currentPrinter = CodePrinter.getPrinter(platform);
			if (!compilationUnits.containsKey(platform)) {
				compilationUnits.put(platform, new ArrayList<File>());
			}

			if (!currentPrinter.getOutputDir().exists()) {
				LOG.debug("Creating the output directory "
						+ currentPrinter.getOutputDir());
				currentPrinter.getOutputDir().mkdirs();
			}

			compilationUnits.get(platform).addAll(
					currentPrinter.print(classDef));
		}
		return compilationUnits;
	}


}
