package gool;

import gool.ast.ClassDef;
import gool.parser.GoolParser;
import gool.platform.Platform;
import gool.util.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GOOLCompiler {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(GOOLCompiler.class);

	
	
	public Map<Platform, List<File>> concreteGoolToConcretePlatform(
			Platform destPlatform, String input)
			throws Exception {
		Collection<ClassDef> classDefs = concreteGoolToAbstractGool(destPlatform, input);
		return abstractGool2Target(classDefs);
	}

	private Collection<ClassDef> concreteGoolToAbstractGool(Platform destPlatform, String input)
			throws Exception {
		return GoolParser.parseGool(destPlatform, input);
	}


	
	public Map<Platform, List<File>> concreteGoolToConcretePlatform(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		Collection<ClassDef> classDefs = concreteGoolToAbstractGool(destPlatform, inputFiles);
		return abstractGool2Target(classDefs);
	}

	private Map<Platform, List<File>> abstractGool2Target(Collection<ClassDef> classDefs) throws FileNotFoundException {
		return Helper.printClassDefs(classDefs);
	}

	private Collection<ClassDef> concreteGoolToAbstractGool(Platform destPlatform,
			Collection<? extends File> inputFiles) throws Exception {
		return GoolParser.parseGool(destPlatform,
				Helper.getJavaFileObjects(inputFiles));
	}
	
	class result {
		Map<Platform, List<File>> files;
		File mainfile;
	}

}
