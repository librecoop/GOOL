/**
 * The class that launches the other ones, thereby controlling the workflow.
 * TODO: further parameterize concreteJavaToConcretePlatform() to have an input platform. 
 * TODO: do the wrapping of the different input formats at this stage rather than in JavaParser.
 * TODO: return packages instead of ClassDefs.
 */

package gool;

import gool.ast.constructs.ClassDef;
import gool.executor.ExecutorHelper;
import gool.generator.GeneratorHelper;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.xml.XmlPlatform;
import gool.parser.java.JavaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import logger.Log;

public class GOOLCompiler {

	/**
	 * The main
	 * - gets the folder to open from Settings
	 * - opens the files
	 * - creates an instance of this class
	 * - triggers it upon the files, with argument the target platform.
	 */
	public static void main(String[] args) {
		try {
			File folder = new File(Settings.get("java_in_dir"));
			Collection<File> files = Arrays.asList(folder.listFiles());
			Log.i(files.toString());
			GOOLCompiler gc = new GOOLCompiler();
			Map<Platform, List<File>> f = gc.concreteJavaToConcretePlatform(JavaPlatform.getInstance(), files);
			Log.i(f.toString());
			gc.concreteJavaToConcretePlatform(CSharpPlatform.getInstance(),
					files);
			gc.concreteJavaToConcretePlatform(CppPlatform.getInstance(), files);
			gc.concreteJavaToConcretePlatform(PythonPlatform.getInstance(), files);
		} catch (Exception e) {
			Log.e(e);
		}
	}

	/**
	 * Taking concrete Java into concrete Target is done in two steps:
	 * - we parse the concrete Java into abstract GOOL;
	 * - we flatten the abstract GOOL into concrete Target.
	 * Notice that the Target is specified at this stage already: it will be carried kept in the abstract GOOL.
	 * This choice is justified if we want to do multi-platform compilation, 
	 * i.e. have some pieces of the abstract GOOL to compile in some Target, and another piece is some other Target.
	 * @param destPlatform: the Target language
	 * @param input: the concrete Java, as a string
	 * @return a map of the compiled files for the different platforms
	 * @throws Exception
	 */
	public Map<Platform, List<File>> concreteJavaToConcretePlatform(
			Platform destPlatform, String input) throws Exception {
		Collection<ClassDef> classDefs = concreteJavaToAbstractGool(
				destPlatform, input);
		return abstractGool2Target(classDefs);
	}

	public Map<Platform, List<File>> concreteJavaToConcretePlatform(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		Collection<ClassDef> classDefs = concreteJavaToAbstractGool(
				destPlatform, inputFiles);
		return abstractGool2Target(classDefs);
	}

		
	/**
	 * Parsing the concrete Java into abstract GOOL is done by JavaParser.
	 * @param destPlatform: the Target language
	 * @param input: the concrete Java, as a string
	 * @return abstract GOOL classes
	 * @throws Exception
	 */
	private Collection<ClassDef> concreteJavaToAbstractGool(
			Platform destPlatform, String input) throws Exception {
		return JavaParser.parseGool(destPlatform, input);
	}

	private Collection<ClassDef> concreteJavaToAbstractGool(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		return JavaParser.parseGool(destPlatform,
				ExecutorHelper.getJavaFileObjects(inputFiles));
	}

	
	/**
	 * Flattening the abstract GOOL into concrete Target is done by GeneratorHelper.
	 * @param classDefs
	 * @return a map of the compiled files for the different platforms
	 * @throws FileNotFoundException
	 */
	private Map<Platform, List<File>> abstractGool2Target(
			Collection<ClassDef> classDefs) throws FileNotFoundException {
		return GeneratorHelper.printClassDefs(classDefs);
	}


}
