package gool.parser.java;

import gool.Settings;
import gool.ast.constructs.ClassDef;
import gool.generator.GoolGeneratorController;
import gool.generator.common.Platform;
import gool.recognizer.java.JavaRecognizer;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.lang.StringUtils;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;

public final class JavaParser {

	private JavaParser() {
	}
	public static Collection<ClassDef> parseGool(Platform defaultPlatform,
			Iterable<? extends JavaFileObject> compilationUnits,
			List<File> dependencies, JavaRecognizer visitor) throws Exception {
		if (visitor == null) {
			throw new IllegalArgumentException("The gool visitor is null.");
		}
		
		JavacTask task = null;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		List<String> options = null;

		List<String> stringDependencies = new ArrayList<String>();
		if (dependencies != null && !dependencies.isEmpty()) {
			for (File file : dependencies) {
				stringDependencies.add(file.getAbsolutePath());
			}
		}
		/*
		 * Add the GOOL library to allow the use of classes and annotations
		 * like Gool.Map, Gool.List.
		 */
//		for (File d : GoolPlatform.getInstance().getCompiler().getDependencies()) {
//			stringDependencies.add(d.getAbsolutePath());
//		}
		stringDependencies.add(Settings.get("gool_library")
				.toString());
		options = Arrays.asList("-classpath", StringUtils.join(
				stringDependencies, File.pathSeparator));
		
		List<MyFileObject> files = new ArrayList<MyFileObject>();
		for (JavaFileObject file : compilationUnits) {
			files
					.add(new MyFileObject(file.getCharContent(true).toString(),
							file.getName().replaceAll("\\.(gool|java)$", "")
									+ ".java"));
		}
		task = (JavacTask) compiler.getTask(null, null, null, options, null,
				files);
		Iterable<? extends CompilationUnitTree> asts = task.parse();

		task.analyze();
//		Iterable<? extends JavaFileObject> compiledClasses = task.generate();
//		GoolClassLoader loader = new GoolClassLoader();
//		
//		for (JavaFileObject f : compiledClasses) {
//			InputStream is = f.openInputStream();
//			
//			byte[] bytes = new byte[is.available()];
//			
//			is.read(bytes);
//			is.close();
//			
//			loader.loadClass(f.getName(), bytes);
//		}
		Trees trees = Trees.instance(task);

		// Sets gool generator as the default generator
		GoolGeneratorController.reset();

		visitor.setDefaultPlatform(defaultPlatform);
		visitor.setTrees(trees);
		//visitor.setClassLoader(loader);
		
		for (CompilationUnitTree ast : asts) {
			visitor.setCurrentCompilationUnit(ast);
			visitor.scan(ast, null);
		}
		
		// TODO It is okay to add custom dependencies only in the default
		// platform?
		for (ClassDef classDef : visitor.getGoolClasses()) {
			classDef.getPlatform().registerCustomDependency(classDef.getFullName(),
					classDef);
		}
		return visitor.getGoolClasses();
	}

	public static Collection<ClassDef> parseGool(Platform defaultPlatform,
			String input) throws Exception {
		ArrayList<JavaFileObject> compilationUnits = new ArrayList<JavaFileObject>();
		compilationUnits.add(new MyFileObject(
		 input, "Random.gool"));
		System.out.println(input);
		return parseGool(defaultPlatform, compilationUnits);
	}

	public static Collection<ClassDef> parseGoolFiles(Platform defaultPlatform,
			List<File> dirs) throws Exception {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager
				.getJavaFileObjectsFromFiles(dirs);
		return parseGool(defaultPlatform, compilationUnits);
	}

	static class MyFileObject extends SimpleJavaFileObject {

		private String input;

		public MyFileObject(String input, String name) {
			super(URI.create(name), JavaFileObject.Kind.SOURCE);
			this.input = input;
		}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return input;
		}
	}

	public static Collection<ClassDef> parseGool(Platform defaultPlatform,
			Iterable<? extends JavaFileObject> compilationUnits)
			throws Exception {
		return parseGool(defaultPlatform, compilationUnits, null, new JavaRecognizer());
	}

}
