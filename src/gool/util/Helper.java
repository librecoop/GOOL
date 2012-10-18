package gool.util;

import gool.GOOLCompiler;
import gool.ast.ClassDef;
import gool.ast.Dependency;
import gool.platform.Platform;
import gool.platform.common.CodePrinter;
import gool.platform.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public final class Helper {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(Helper.class);

	public static String surroundWithClassMain(String input, String className) {
		return surroundWithClass("public static void main(String[] args){"
				+ input + " } ", className, "");
	}

	public static Iterable<? extends JavaFileObject> getJavaFileObjects(
			Collection<? extends File> inputFiles) {
		return ToolProvider.getSystemJavaCompiler().getStandardFileManager(
				null, null, null).getJavaFileObjectsFromFiles(inputFiles);
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

	public static ClassDef getMainClass(Collection<ClassDef> classDefs) {
		for (ClassDef classDef : classDefs) {
			if (classDef.isMainClass()) {
				return classDef;
			}
		}
		return null;
	}

	public static String generateCompileRun(Platform platform, String input,
			String mainClassName) throws Exception, FileNotFoundException {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteGoolToConcretePlatform(platform, input); 
		System.out.println("--2-->"+files);
		return compileAndRun(platform, files);
	}


	public static List<File> generateCompile(Platform platform,
			Collection<File> inputFiles, ClassDef classDef) throws Exception {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteGoolToConcretePlatform(platform, inputFiles);
		return Helper.compile(files);
	}

	public static String generateCompileRun(Platform platform,
			Collection<File> inputFiles, File file) throws Exception {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteGoolToConcretePlatform(platform,
				inputFiles);
		return compileAndRun(platform, files);
	}

	public static String run(Platform platform, File file)
			throws FileNotFoundException {
		return platform.getCompiler().run(file);
	}

	private static String compileAndRun(Platform platform,
			Map<Platform, List<File>> files) throws FileNotFoundException {
		StringBuilder result = new StringBuilder();

		List<File> compiledFiles = Helper.compile(files);
		LOG.info(compiledFiles);
		result.append(platform.getCompiler().run(compiledFiles.get(0)));
		return result.toString();
	}

	public static String surroundWithClass(String input, String className, String accessModifier) {
		return  accessModifier + " class " + className + " { public " + className+ "(){} " + input + " } ";
	}

	public static String joinParams(List<?> parameters) {
		if (parameters == null) {
			return "";
		}
		return StringUtils.join(parameters, ", ");
	}

	public static Set<String> printDependencies(ClassDef classDef) {
		Set<String> result = new HashSet<String>();
		for (Dependency dep : classDef.getDependencies()) {
			if (!dep.getFullName().equals(classDef.getFullName())){
				result.add(dep.getFullName());
			}
		}
		return result;
	}

	public static File getClassDefFile(ClassDef classDef) {
		return new File(classDef.getPlatform().getCodePrinter().getOutputDir()
				.getAbsolutePath(), StringUtils.replace(classDef
				.getPackageName(), ".", File.separator));
	}

	public static List<File> compile(Map<Platform, List<File>> files) throws FileNotFoundException {
		List<File> result = new ArrayList<File>();
	
		for (Entry<Platform, List<File>> item : files.entrySet()) {
			SpecificCompiler compiler = item.getKey().getCompiler();
			System.out.println("---3-->" + compiler);
			File outputFile = compiler.compileToExecutable(item.getValue(), null, null, null);	
			result.add(outputFile);
		}
	
		return result;
	}
}
