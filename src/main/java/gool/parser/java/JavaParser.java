/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
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

package gool.parser.java;

import gool.ParseGOOL;
import gool.Settings;
import gool.ast.core.ClassDef;
import gool.generator.GoolGeneratorController;
import gool.generator.common.GeneratorMatcher;
import gool.generator.common.Platform;
import gool.recognizer.common.RecognizerMatcher;
import gool.recognizer.java.JavaRecognizer;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import logger.Log;

import org.apache.commons.lang.StringUtils;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;


/**
 * This class parses concrete Java into abstract GOOL. For this purpose it
 * relies on Sun's Java parser.
 */
public class JavaParser extends ParseGOOL {

	/**
	 * Parsing concrete Java into abstract GOOL is done in three steps. - We
	 * call Sun's java parser to produce abstract Java; - We visit abstract Java
	 * with the JavaRecognizer to produce abstract GOOL; - We annotate the
	 * abstract GOOL so that it carries the Target language.
	 * 
	 * @param defaultPlatform
	 *            : specifies the Target language of the code generation that
	 *            will later be applied to the abstract GOOL, once this Java
	 *            parsing is performed. 
	 * @param compilationUnits
	 *            : An Iterable of JavaFileObject, which are Sun's java parser's
	 *            representation of the files to be parsed.
	 * @param dependencies
	 *            : specifies imported libraries
	 * @param visitor
	 *            : this is the class that transforms Sun's abstract java, into
	 *            abstract GOOL, i.e. the JavaRecognizer.
	 * @return a list of classdefs, i.e. of abstract GOOL classes.
	 * @throws Exception
	 */

	public static Collection<ClassDef> parseGool(Platform defaultPlatform,
			Iterable<? extends JavaFileObject> compilationUnits,
			JavaRecognizer visitor) throws Exception 
	{
		if (visitor == null) {
			throw new IllegalArgumentException("The gool visitor is null.");
		}

		/**
		 * Setup the options to Sun's
		 * java compiler This requires working out the dependencies
		 */
		// Put dependencies into a list of file paths
		List<String> stringDependencies = new ArrayList<String>();
		// Add the GOOL library as a dependency
		// so that the program can use gool.imports.java
		stringDependencies.add(Settings.get("gool_library").toString());
		// with the dependencies all set, we can make up the options
		List<String> options = Arrays.asList("-classpath",
				StringUtils.join(stringDependencies, File.pathSeparator));

		/**
		 * We now parse using Sun's java compiler
		 */
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		JavacTask task = (JavacTask) compiler.getTask(null, null, null,
				options, null, compilationUnits);
		Iterable<? extends CompilationUnitTree> asts = task.parse();
		visitor.setTypes(task.getTypes());
		/**
		 * We now analyze using Sun's java compiler so as to get a Java abstract
		 * type tree.
		 */
		Iterable<? extends Element> elts = task.analyze();
		for (Element elt : elts){
			System.out.println(elt.toString());
		}
		Trees typetrees = Trees.instance(task);
	
		/**
		 * We now prepare the JavaRecognizer for conversion of abstract Java to
		 * abstract GOOL.
		 */

		// The visitor needs to know what the Target language is
		// Because it will annotate the abstract GOOL with this information.
		//visitor.setDefaultPlatform(defaultPlatform);
		GoolGeneratorController.setCodeGenerator(defaultPlatform
				.getCodePrinter().getCodeGenerator());
		
		// Adding by Denis A.
		GeneratorMatcher.init(defaultPlatform);
		
		// The visitor might need Sun's analyzed Java abstract type tree.
		visitor.setTrees(typetrees);

		/**
		 * We launch the JavaRecognizer against each abstract Java AST
		 */
		for (CompilationUnitTree ast : asts) {
			visitor.setCurrentCompilationUnit(ast);
			Log.d("\n\n****************** Start Scan *******************\n\n");
			visitor.scan();
			Log.d("\n\n****************** End Scan *******************\n\n");
		}

		Log.d(RecognizerMatcher.printMatchTables());
		
		return visitor.getGoolClasses();
	}
	
	
	public static Iterable<? extends JavaFileObject> getJavaFileObjects(
			Collection<? extends File> inputFiles) {
		return ToolProvider.getSystemJavaCompiler()
				.getStandardFileManager(null, null, null)
				.getJavaFileObjectsFromFiles(inputFiles);
	}
	
	/**
	 * Initially, call the parser with input files.
	 */
	public Collection<ClassDef> parseGool(Platform defaultPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		return parseGool(defaultPlatform, getJavaFileObjects(inputFiles));
	}

	/**
	 * Then, call the parser with no dependency yet, and with the
	 * JavaRecognizer.
	 */
	public Collection<ClassDef> parseGool(Platform defaultPlatform, Iterable<? extends JavaFileObject> compilationUnits)
			throws Exception {
		return parseGool(defaultPlatform, compilationUnits,	new JavaRecognizer());
	}

	/**
	 * Sun's java parser takes his inputs as compilation units, which themselves
	 * are the source files loaded into objects called SimpleJavaFileObject
	 * Those objects have a name and: - a kind to say that they are source
	 * files, - a content, retrieved by getCharContent. When we wrap a string
	 * input into a file, we create directly such a SimpleJavaFileObject; with
	 * getCharContent overriden to yield that input.
	 */
	static class MyFileObject extends SimpleJavaFileObject {

		private String input;

		public MyFileObject(String input, String name) {
			super(URI.create(name), JavaFileObject.Kind.SOURCE);
			this.input = input;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return input;
		}
	}

}
