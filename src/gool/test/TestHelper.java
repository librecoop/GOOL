package gool.test;

import gool.GOOLCompiler;
import gool.ast.constructs.ClassDef;
import gool.executor.ExecutorHelper;
import gool.generator.common.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public final class TestHelper {
	
	private static Logger logger = Logger.getLogger(TestHelper.class);


	public static String surroundWithClassMain(String input, String className) {
		return surroundWithClass("public static void main(String[] args){"
				+ input + " } ", className, "");
	}

	public static String surroundWithClass(String input, String className, String accessModifier) {
		return accessModifier + " class " + className + " { public " + className+ "(){} " + input + " } ";
	}
	
	public static String generateCompileRun(Platform platform, String input,
			String mainClassName) throws Exception, FileNotFoundException {
		GOOLCompiler gc = new GOOLCompiler();
		logger.info("START: complete concrete JAVA to platform: " +platform.getName());
		Map<Platform, List<File>> files = gc.concreteJavaToConcretePlatform(platform, input); 
		logger.info("FINISH: complete concrete JAVA to platform: " +platform.getName()+ " Output files created:\n"+files);
		return ExecutorHelper.compileAndRun(platform, files);
	}

	public static List<File> generateCompile(Platform platform,
			Collection<File> inputFiles, ClassDef classDef) throws Exception {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteJavaToConcretePlatform(platform, inputFiles);
		return ExecutorHelper.compile(files);
	}

	public static String generateCompileRun(Platform platform,
			Collection<File> inputFiles, File file) throws Exception {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.concreteJavaToConcretePlatform(platform,
				inputFiles);
		return ExecutorHelper.compileAndRun(platform, files);
	}

}
