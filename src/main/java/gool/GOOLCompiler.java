
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
import gool.parser.java.JavaParser;
import gool.generator.GeneratorHelper;
import gool.generator.GoolGeneratorController;
import gool.generator.common.GeneratorMatcher;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.generator.python.PythonPlatform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import logger.Log;

/**
 * This class is the main program. 
 * It runs the gool system to produce output. 
 * To configure this application check the file named "src/gool.properties". 
 */
public class GOOLCompiler {

	/**
	 * The main - parse input arguments and launch the translation
	 */
	public static void main(String[] args) {
		Boolean isGuiActive = false;
		Log.DEBUG_LOG = true;
		for(int i = 0; i < args.length; i++){
			if(args[i].equalsIgnoreCase("-gui"))
				isGuiActive = true;
		}
		if (isGuiActive)
			Settings.launchGuiSetter();
		else{
			launchTranslation();
		}
	}

	/**
	 * Launch the translation that prints output files.
	 */
	public static void launchTranslation(){
		String inputLanguage = getInputLanguage();
		if (inputLanguage.isEmpty()){
			Log.e("No input language specified");
			return;
		}
		//parse input		
		try{	
			//Read input files
			String inputFolder = Settings.get(inputLanguage + "_in_dir");
			//The list of the files not to process
			ArrayList<File> filesToExclude = getIgnoredInputFiles(inputFolder);
			//The list of the files to process
			Map<String, String> filesToProcess = getInputStrings(inputLanguage,
					filesToExclude);
			
			//Recognition step
			ParseGOOL parser = null;
			if (inputLanguage.equalsIgnoreCase("java")) {
				parser = new JavaParser();
			}
			/*else if(inputLang.equalsIgnoreCase("cpp")){
				parser = new CppParser();
			}*/
			else{
				throw new Exception("Unknown input language.");
			}
			
			Collection<ClassDef> goolPort = GOOLCompiler.concreteToAbstractGool(parser,
					filesToProcess);
					
			//Set the desired platform and generate files
			Platform plt = null;
			Map<String, String> outputFiles = new HashMap<String, String>();
//			/**** C++ ****/
//			plt = CppPlatform.getInstance(filesToExclude, Settings.get("cpp_out_dir"));
//			outputFiles.putAll(abstractGool2Target(goolPort, plt));
//			/**** C# ****/
//			plt = CSharpPlatform.getInstance(filesToExclude, Settings.get("csharp_out_dir"));
//			outputFiles.putAll(abstractGool2Target(goolPort, plt));
//			/**** java ****/
//			plt = JavaPlatform.getInstance(filesToExclude, Settings.get("java_out_dir"));
//			outputFiles.putAll(abstractGool2Target(goolPort, plt));
//			/**** ObjC ****/
			plt = ObjcPlatform.getInstance(filesToExclude, Settings.get("objc_out_dir"));
			outputFiles.putAll(abstractGool2Target(goolPort, plt));
//			/**** Python ****/
//			plt = PythonPlatform.getInstance(filesToExclude, Settings.get("python_out_dir"));
//			outputFiles.putAll(abstractGool2Target(goolPort, plt));
			
			//print files
			printFiles(outputFiles);
			
		}
		catch (Exception e) {
			String mess = e.toString();
			mess += "\n";
			for(StackTraceElement se : e.getStackTrace()){
				mess += se.toString() + "\n";
			}
			Log.e(mess);
		}
	}

	/**
	 * Launch the translation with input parameters. It does not print output
	 * files but returns them within a map structure.
	 * 
	 * @param inputLang : input language
	 * @param outputLang : output language
	 * @param input : input map of files to translate (name as key and code as value)
	 */

	public static Map<String, String> launchTranslation(String inputLang,
			String outputLang, Map<String, String> input)
					throws Exception {

		ParseGOOL parser = null;
		Collection<ClassDef> goolPort = null;
		Platform plt = null;

		if (inputLang.equalsIgnoreCase("java")) {
			parser = new JavaParser();
		}
		/*else if(inputLang.equalsIgnoreCase("cpp")){
			parser = new CppParser();
		}*/
		else{
			throw new Exception("Unknown input language.");
		}
		
		//Recognition
		goolPort = GOOLCompiler.concreteToAbstractGool(parser, input);

		
		if (outputLang.equalsIgnoreCase("cpp")) {
			plt = CppPlatform.getInstance();
		}
		else if(outputLang.equalsIgnoreCase("c#")){
			plt = CSharpPlatform.getInstance();
		}
		else{
			throw new Exception("Unknown output language.");
		}
		
		//Generation
		return GOOLCompiler.abstractGool2Target(goolPort, plt);
	}

	
	
	/**
	 * Print a list of files
	 * @param files : map with absolute file's names as key and code as values
	 * @throws FileNotFoundException
	 */
	public static void printFiles(Map<String, String> files) throws FileNotFoundException{
		for (Entry<String, String> entry : files.entrySet()){
			File f = new File(entry.getKey());
			PrintWriter writer = new PrintWriter(f);
			writer.println(entry.getValue());
			writer.close();
		}
	}
	
	/**
	 * Return the input language
	 * @return "java" or "cpp"
	 */
	public static String getInputLanguage(){
		if(Settings.get("input_langage").equalsIgnoreCase("java"))
			return "java";
		if(Settings.get("input_langage").equalsIgnoreCase("c++") ||
				Settings.get("input_langage").equalsIgnoreCase("cpp"))
			return "cpp";
		return "";
	}

	/**
	 * Return the input files to ignore as specified in the .goolIgnore file
	 * @param inputFolder : input folder containing the .goolIgnore file (if not, an empty
	 *  list i returned)
	 * @return The list of the file's names not to process.
	 * @throws Exception
	 */
	public static ArrayList<File> getIgnoredInputFiles(String inputFolder) throws Exception{
		ArrayList<File> fileToExclude = new ArrayList<File>();
		try {
			File t = new File(inputFolder + File.separator
					+ ".goolIgnore");
			if (t.canRead()){
				FileReader f = new FileReader(t);
				BufferedReader g = new BufferedReader(f);
				String line;
				while ((line = g.readLine()) != null){
					line = line.trim();					
					if (line.startsWith("*.")){
						String ext = line.substring(2);
						fileToExclude.addAll(getFilesInFolder(
								new File(inputFolder), ext, 
								new ArrayList<File>()));
					}
					else{
						File tp = new File(inputFolder + File.separator + line);
						if (!tp.exists())
							continue;
						if(tp.isFile()){
							fileToExclude.add(tp);
						}else if(tp.isDirectory()){
							fileToExclude.addAll(getAllFilesInFolder(tp));
						}
					}
				}
				g.close();
			}
		} catch (Exception e) {
			Log.e(e);
			throw(e);
		}
		return fileToExclude;
	}

	
	/**
	 * Return the input codes to process
	 * @param inputLanguage : "java" or "cpp"
	 * @param fileToExclude : list of file's names not to process.
	 * @return a map with input file's names as keys and associated codes as values.
	 * @throws Exception
	 */
	public static Map<String, String> getInputStrings(String inputLanguage,
			ArrayList<File> fileToExclude) throws Exception{

		Map<String, String> input = new HashMap<String, String>();
		File folder = new File(Settings.get(inputLanguage + "_in_dir"));

		//Get string to parse

		ArrayList<File> infiles = (ArrayList<File>) getFilesInFolder(folder, inputLanguage,
				fileToExclude);
		for (File f:infiles){
			String code = "";
			try{
				if (f.canRead()){
					FileReader fr = new FileReader(f);
					BufferedReader gr = new BufferedReader(fr);
					String ligne;
					while ((ligne = gr.readLine()) != null){
						code += ligne + "\n";
					}
					gr.close();
				}
			}catch (Exception e) {
				Log.e(e);
			}
			input.put(f.getName(), code);
		}

		return input;
	}

	/**
	 * Gets all files in a folder tree.
	 * @param folder 
	 * 			: The root name of the folder tree.
	 * @return 
	 * 			The collection of files with the specific extension in the folder tree.
	 */
	public static Collection<File> getAllFilesInFolder(File folder) {
		Collection<File> files = new ArrayList<File>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getAllFilesInFolder(f));
			} else if (f.isFile()) {
				files.add(f);
			}
		}
		return files;
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
	public static Collection<File> getFilesInFolder(File folder, String ext, 
			ArrayList<File> fileToExclude) {
		Collection<File> files = new ArrayList<File>();
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(getFilesInFolder(f, ext, fileToExclude));
			} else if (f.getName().endsWith(ext)) {
				if (!fileToExclude.contains(f))
					files.add(f);
			}
		}
		return files;
	}

	
	/**
	 * Parsing the concrete Language into abstract GOOL is done by a Parser.
	 * 
	 * @param parserIn
	 * 			  : the Parser of the source language (It extends ParseGOOL)
	 * @param input
	 *            : the concrete Java, as a string
	 * @return abstract GOOL classes
	 * @throws Exception
	 */
	private static Collection<ClassDef> concreteToAbstractGool(
			ParseGOOL parserIn, Map<String, String> input) throws Exception {
		return parserIn.parseGool(input);
	}


	
	/**
	 * Flattening the abstract GOOL into concrete Target is done by
	 * GeneratorHelper.
	 * @param plt : target platform
	 * @param classDefs : input classDefs
	 * @return a map of the file names and associated codes	 
	 * */
	private static Map<String, String> abstractGool2Target(
			Collection<ClassDef> classDefs, Platform plt){
		GeneratorMatcher.init(plt);
		GoolGeneratorController.setCodeGenerator(plt.getCodePrinter().getCodeGenerator());	
		for(ClassDef cl : classDefs){
			cl.setPlatform(plt);
		}
		return GeneratorHelper.printClassDefs(classDefs);
	}

}
