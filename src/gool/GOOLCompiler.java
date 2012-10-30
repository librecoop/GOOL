package gool;

import gool.ast.constructs.ClassDef;
import gool.executor.ExecutorHelper;
import gool.generator.GeneratorHelper;
import gool.generator.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.parser.java.JavaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class GOOLCompiler {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(GOOLCompiler.class);

	public static void main(String[] args) {
		try {
			File folder = new File(Settings.get("java_input_dir"));
			Collection<File> files = Arrays.asList(folder.listFiles());
			GOOLCompiler gc = new GOOLCompiler();
			gc.concreteGoolToConcretePlatform(JavaPlatform.getInstance(), files);
			gc.concreteGoolToConcretePlatform(CSharpPlatform.getInstance(),
					files);
			gc.concreteGoolToConcretePlatform(CppPlatform.getInstance(), files);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<Platform, List<File>> concreteGoolToConcretePlatform(
			Platform destPlatform, String input) throws Exception {
		Collection<ClassDef> classDefs = concreteGoolToAbstractGool(
				destPlatform, input);
		return abstractGool2Target(classDefs);
	}

	private Collection<ClassDef> concreteGoolToAbstractGool(
			Platform destPlatform, String input) throws Exception {
		return JavaParser.parseGool(destPlatform, input);
	}

	public Map<Platform, List<File>> concreteGoolToConcretePlatform(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		Collection<ClassDef> classDefs = concreteGoolToAbstractGool(
				destPlatform, inputFiles);
		return abstractGool2Target(classDefs);
	}

	private Map<Platform, List<File>> abstractGool2Target(
			Collection<ClassDef> classDefs) throws FileNotFoundException {
		return GeneratorHelper.printClassDefs(classDefs);
	}

	private Collection<ClassDef> concreteGoolToAbstractGool(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		return JavaParser.parseGool(destPlatform,
				ExecutorHelper.getJavaFileObjects(inputFiles));
	}

	class result {
		Map<Platform, List<File>> files;
		File mainfile;
	}

}
