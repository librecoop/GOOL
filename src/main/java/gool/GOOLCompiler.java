
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		else
			launchTranslation();
	}

	/**
	 * Launch the translation : gets the folder to open from Settings - opens the files -
	 * creates an instance of this class - triggers it upon the files, with
	 * argument the target platform.
	 */

	public static void launchTranslation(){
		//------------------------------------//
		//------------ JAVA INPUT ------------//
		if(Settings.get("input_langage").equalsIgnoreCase("java")){
			try {
				File folder = new File(Settings.get("java_in_dir"));
				Collection<File> files = getFilesInFolder(folder, "java");
				ArrayList<String> extToNCopy = new ArrayList<String>();

				try {
					File t = new File(Settings.get("java_in_dir") + File.separator
							+ ".goolIgnore");
					if (t.canRead()){
						FileReader f = new FileReader(t);
						BufferedReader g = new BufferedReader(f);
						String ligne;
						while ((ligne = g.readLine()) != null)
							extToNCopy.add(ligne);
					}
				} catch (Exception e) {
					Log.e(e);
				}
				Collection<File> filesNonChange = getFilesInFolderNonExe(folder,
						extToNCopy);
				for(File f : filesNonChange){
					System.out.println(f.getName());
				}
				

				GOOLCompiler gc=new GOOLCompiler();

				Collection<ClassDef> classDefs = 
						gc.concretePlatformeToAbstractGool(new JavaParser(), files);
				for(ClassDef cl : classDefs){
					System.out.println(cl.getName());
				}
				

			} catch (Exception e) {
				String mess = e.toString();
				mess += "\n";
				for(StackTraceElement se : e.getStackTrace()){
					mess += se.toString() + "\n";
				}
				Log.e(mess);
			}
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
			ParseGOOL parserIn, Collection<? extends File> inputFiles) throws Exception {
		return parserIn.parseGool(inputFiles);
	}

}
