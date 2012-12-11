package gool.executor;

import gool.ast.constructs.ClassDef;
import gool.executor.common.SpecificCompiler;
import gool.generator.common.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.lang.StringUtils;

public final class ExecutorHelper {


	public static Iterable<? extends JavaFileObject> getJavaFileObjects(
			Collection<? extends File> inputFiles) {
		return ToolProvider.getSystemJavaCompiler().getStandardFileManager(
				null, null, null).getJavaFileObjectsFromFiles(inputFiles);
	}


	public static ClassDef getMainClass(Collection<ClassDef> classDefs) {
		for (ClassDef classDef : classDefs) {
			if (classDef.isMainClass()) {
				return classDef;
			}
		}
		return null;
	}

	public static String run(Platform platform, File file)
			throws FileNotFoundException {
		return platform.getCompiler().run(file);
	}

	public static String compileAndRun(Platform platform,
			Map<Platform, List<File>> files) throws FileNotFoundException {
		StringBuilder result = new StringBuilder();

		List<File> compiledFiles = ExecutorHelper.compile(files);
		System.out.println(compiledFiles);
		result.append(platform.getCompiler().run(compiledFiles.get(0)));
		return result.toString();
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
