
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

/**
 * The class that launches the other ones, thereby controlling the workflow.
 * TODO: further parameterize concreteJavaToConcretePlatform() to have an input platform. 
 * TODO: do the wrapping of the different input formats at this stage rather than in JavaParser.
 * TODO: return packages instead of ClassDefs.
 */

package gool;

import gool.ast.core.ClassDef;
import gool.executor.ExecutorHelper;
import gool.generator.GeneratorHelper;
import gool.generator.android.AndroidPlatform;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.xml.XmlPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.parser.java.JavaParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import logger.Log;

/**
 * This class is the main program. 
 * It runs the gool system to produce output. 
 * To configure this application check the file named "src/gool.properties". 
 */
public class GOOLCompiler {

	/**
	 * The main - gets the folder to open from Settings - opens the files -
	 * creates an instance of this class - triggers it upon the files, with
	 * argument the target platform.
	 */
	public static void main(String[] args) {

		try {
			File folder = new File(Settings.get("java_in_dir"));
			Collection<File> files = getFilesInFolder(folder, "java");
			ArrayList<String> extToNCopy = new ArrayList<String>();

			try {
				File t = new File(Settings.get("java_in_dir") + File.separator
						+ ".goolIgnore");
				FileReader f = new FileReader(t);
				BufferedReader g = new BufferedReader(f);
				String ligne;
				while ((ligne = g.readLine()) != null)
					extToNCopy.add(ligne);
			} catch (Exception e) {
				Log.e(e);
			}

			Collection<File> filesNonChange = getFilesInFolderNonExe(folder,
					extToNCopy);
			/*concreteJavaToConcretePlatform(
					JavaPlatform.getInstance(filesNonChange), files);
			concreteJavaToConcretePlatform(
					CSharpPlatform.getInstance(filesNonChange), files);
			concreteJavaToConcretePlatform(
					CppPlatform.getInstance(filesNonChange), files);
			concreteJavaToConcretePlatform(
					PythonPlatform.getInstance(filesNonChange), files);*/
			//concreteJavaToConcretePlatform(
			//		XmlPlatform.getInstance(filesNonChange), files);
			// TODO: same for android & Objc
			/*
			concreteJavaToConcretePlatform(AndroidPlatform.getInstance(), files);
			concreteJavaToConcretePlatform(ObjcPlatform.getInstance(), files);*/
			
			GOOLCompiler gc=new GOOLCompiler();
			
			// JAVA input -> JAVA output
			gc.runGOOLCompiler(new JavaParser(), JavaPlatform.getInstance(filesNonChange), files);
			//JAVA input -> CSharp output
			gc.runGOOLCompiler(new JavaParser(), CSharpPlatform.getInstance(filesNonChange), files);
			//JAVA input -> CPP output
			gc.runGOOLCompiler(new JavaParser(), CppPlatform.getInstance(filesNonChange), files);
			//JAVA input -> PYTHON output
			gc.runGOOLCompiler(new JavaParser(), PythonPlatform.getInstance(filesNonChange), files);
			//JAVA input -> XML output
			gc.runGOOLCompiler(new JavaParser(), XmlPlatform.getInstance(filesNonChange), files);
			
			// TODO: same for android & Objc
			//JAVA input -> ANDROID output
			//gc.runGOOLCompiler(new JavaParser(), AndroidPlatform.getInstance(), files);
			//JAVA input -> OBJC output
			//gc.runGOOLCompiler(new JavaParser(), ObjcPlatform.getInstance(), files);
			
			
			
		} catch (Exception e) {
			Log.e(e);
		}
	}

	/**
	 * Gets the files with a specific extension in a folder tree.
	 * @param folder 
	 * 			: The root name of the folder tree.
	 * @param ext 
	 * 			: The extension pattern.
	 * @return 
	 * 			The collection of files with the specific extension in the folder tree.
	 */
	public static Collection<File> getFilesInFolder(File folder, String ext) {
		Collection<File> files = new ArrayList<File>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getFilesInFolder(f, ext));
			} else if (f.getName().endsWith(ext)) {
				files.add(f);
			}
		}
		return files;
	}

	/**
	 * Gets the files with specific extensions in a folder tree.
	 * @param folder
	 * 			: The root name of the folder tree.
	 * @param ext 
	 * 			: The extensions patterns.
	 * @return 
	 * 			The collection of files with the specific extensions in the folder tree.
	 */
	private static Collection<File> getFilesInFolderNonExe(File folder,
			ArrayList<String> ext) {

		Collection<File> files = new ArrayList<File>();

		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getFilesInFolderNonExe(f, ext));
			} else {
				boolean trouve = false;
				for (String s : ext) {
					if (f.getName().endsWith(s))
						trouve = true;
				}
				if (!trouve)
					files.add(f);
				trouve = false;

			}
		}
		return files;
	}
	
	/**
	 * Taking concrete Language into concrete Target is done in two steps: - we
	 * parse the concrete Language into abstract GOOL; - we flatten the abstract
	 * GOOL into concrete Target. Notice that the Target is specified at this
	 * stage already: it will be carried kept in the abstract GOOL. This choice
	 * is justified if we want to do multi-platform compilation, i.e. have some
	 * pieces of the abstract GOOL to compile in some Target, and another piece
	 * is some other Target.
	 * 
	 * @param parserIn
	 * 			  : the Parser of the source language (It extends ParseGOOL)
	 * @param outPlatform
	 *            : the Target language
	 * @param input
	 *            : the concrete Language, as a string
	 * @return a map of the compiled files for the different platforms
	 * @throws Exception
	 */
	public Map<Platform, List<File>> runGOOLCompiler(ParseGOOL parserIn, Platform outPlatform, String input) throws Exception {
		Collection<ClassDef> classDefs = concretePlatformeToAbstractGool(parserIn,outPlatform, input);
		return abstractGool2Target(classDefs);
}

	/**
	 * Taking concrete Language into concrete Target is done in two steps: - we
	 * parse the concrete Language into abstract GOOL; - we flatten the abstract
	 * GOOL into concrete Target. Notice that the Target is specified at this
	 * stage already: it will be carried kept in the abstract GOOL. This choice
	 * is justified if we want to do multi-platform compilation, i.e. have some
	 * pieces of the abstract GOOL to compile in some Target, and another piece
	 * is some other Target.
	 * 
	 * @param parserIn
	 * 			  : the Parser of the source language (It extends ParseGOOL)
	 * @param outPlatform
	 *            : the Target language
	 * @param input
	 *            : the concrete Language, as files
	 * @return a map of the compiled files for the different platforms
	 * @throws Exception
	 */
	public Map<Platform, List<File>> runGOOLCompiler(ParseGOOL parserIn, Platform outPlatform, Collection<? extends File> inputFiles) throws Exception {
		Collection<ClassDef> classDefs = concretePlatformeToAbstractGool(parserIn,outPlatform, inputFiles);
		return abstractGool2Target(classDefs);
	}

	/**
	 * Parsing the concrete Language into abstract GOOL is done by a Parser.
	 * 
	 * @param parserIn
	 * 			  : the Parser of the source language (It extends ParseGOOL)
	 * @param outPlatform
	 *            : the Target language
	 * @param input
	 *            : the concrete Java, as a string
	 * @return abstract GOOL classes
	 * @throws Exception
	 */
	private static Collection<ClassDef> concretePlatformeToAbstractGool(
			ParseGOOL parserIn, Platform outPlatform, String input) throws Exception {
		return parserIn.parseGool(outPlatform, input);
	}
	
	/**
	 * Parsing the concrete Language into abstract GOOL is done by a Parser.
	 * 
	 * @param parserIn
	 * 			  : the Parser of the source language (It extends ParseGOOL)
	 * @param outPlatform
	 *            : the Target language
	 * @param input
	 *            : the concrete Java, as files
	 * @return abstract GOOL classes
	 * @throws Exception
	 */
	private static Collection<ClassDef> concretePlatformeToAbstractGool(
			ParseGOOL parserIn, Platform outPlatform, Collection<? extends File> inputFiles) throws Exception {
		return parserIn.parseGool(outPlatform,inputFiles);
	}
	
	/**
	 * Taking concrete Java into concrete Target is done in two steps: - we
	 * parse the concrete Java into abstract GOOL; - we flatten the abstract
	 * GOOL into concrete Target. Notice that the Target is specified at this
	 * stage already: it will be carried kept in the abstract GOOL. This choice
	 * is justified if we want to do multi-platform compilation, i.e. have some
	 * pieces of the abstract GOOL to compile in some Target, and another piece
	 * is some other Target.
	 * 
	 * @param destPlatform
	 *            : the Target language
	 * @param input
	 *            : the concrete Java, as a string
	 * @return a map of the compiled files for the different platforms
	 * @throws Exception
	 */
	/*public static Map<Platform, List<File>> concreteJavaToConcretePlatform(
			Platform destPlatform, String input) throws Exception {
		Collection<ClassDef> classDefs = concreteJavaToAbstractGool(
				destPlatform, input);
		return abstractGool2Target(classDefs);
	}*/
	
	/**
	 * Taking concrete Java into concrete Target is done in two steps: - we
	 * parse the concrete Java into abstract GOOL; - we flatten the abstract
	 * GOOL into concrete Target. Notice that the Target is specified at this
	 * stage already: it will be carried kept in the abstract GOOL. This choice
	 * is justified if we want to do multi-platform compilation, i.e. have some
	 * pieces of the abstract GOOL to compile in some Target, and another piece
	 * is some other Target.
	 * 
	 * @param destPlatform
	 *          : The Target language.
	 * @param inputFiles
	 *          : The concrete Java, as files.
	 * @return
	 * 			A map of the compiled files for the different platforms.
	 * @throws Exception
	 */
	/*public static Map<Platform, List<File>> concreteJavaToConcretePlatform(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		Collection<ClassDef> classDefs = concreteJavaToAbstractGool(
				destPlatform, inputFiles);
		return abstractGool2Target(classDefs);
	}*/

	/**
	 * Parsing the concrete Java into abstract GOOL is done by JavaParser.
	 * 
	 * @param destPlatform
	 *            : the Target language
	 * @param input
	 *            : the concrete Java, as a string
	 * @return abstract GOOL classes
	 * @throws Exception
	 */
	/*private static Collection<ClassDef> concreteJavaToAbstractGool(
			Platform destPlatform, String input) throws Exception {
		return JavaParser.parseGool(destPlatform, input);
	}*/

	/**
	 * Parsing the concrete Java into abstract GOOL is done by JavaParser.
	 * 
	 * @param destPlatform
	 *          : The Target language.
	 * @param input
	 *          : The concrete Java, as a files.
	 * @return 
	 * 			Abstract GOOL classes.
	 * @throws Exception
	 */
	/*private static Collection<ClassDef> concreteJavaToAbstractGool(
			Platform destPlatform, Collection<? extends File> inputFiles)
			throws Exception {
		return JavaParser.parseGool(destPlatform,
				ExecutorHelper.getJavaFileObjects(inputFiles));
	}*/

	/**
	 * Flattening the abstract GOOL into concrete Target is done by
	 * GeneratorHelper.
	 * 
	 * @param classDefs
	 * @return a map of the compiled files for the different platforms
	 * @throws FileNotFoundException
	 */
	private static Map<Platform, List<File>> abstractGool2Target(
			Collection<ClassDef> classDefs) throws FileNotFoundException {
		return GeneratorHelper.printClassDefs(classDefs);
	}

}
