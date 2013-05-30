/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
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





package gool.generator.android;


import gool.Settings;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Dependency;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AndroidCodePrinter extends CodePrinter {

	/**
	 * Provides the basic functionality to generate Android code from a list of
	 * GOOL classes.
	 */
	private static final String TEMPLATE_DIR = "gool/generator/android/templates/";

	public AndroidCodePrinter(File outputDir) {
		super(new AndroidGenerator(), outputDir);
	}

	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}

	@Override
	public String getFileName(String className) {
		return className + ".java";
	}

	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		/*
		 * As the implementation of Android necessitates that an additional file
		 * "Activity" be created if the class contains the main method this
		 * method is overridden to check for this
		 */
		String code = pclass.getCode();
		List<File> result = new ArrayList<File>();

		// file separator is just a slash in Unix
		// so the second argument to File() is just the directory
		// that corresponds to the package name
		// the first argument is the default output directory of the platform
		// so the directory name ends up being something like
		// GOOLOUPTUTTARGET/pack/age
		File dir = new File(getOutputDir().getAbsolutePath(),
				StringUtils.replace(pclass.getPackageName(), ".",
						File.separator));
		// Typically the outputdir was created before, but not the package
		// subdirs
		dir.mkdirs();
		// Create the file for the class, fill it in, close it
		File classFile = new File(dir, getFileName(pclass.getName()));
		PrintWriter writer = new PrintWriter(classFile);
		writer.println(code);
		writer.close();
		/*
		 *  This part checks whether the class contains a main method/entry point
		 *  as well as creating the class that will be equivalent to sysOut.
		 */
		if (pclass.isMainClass()) { // Check if class contains main method and
									// if yes create activity class as well
			/**
			 * As standard each .java file with a main method will have a
			 * corresponding *Activity.java file
			 */
			File activityFile = new File(dir, getFileName(pclass.getName())
					.replace(".java", "Activity.java"));
			writer = new PrintWriter(activityFile);
			String activityClassCode = processTemplate("activity.vm", pclass);
			writer.println(activityClassCode);
			writer.close();
			// Put the generated class into the result list
			result.add(activityFile);
			/*
			 * After this it checks whether a PrintOut class has been made yet and
			 * if not creates it. The reason why it is done here is because most of the
			 * time there will only be one main class and this step won't be repeated
			 * Unnecessarily 
			 */
			File printOutFile = new File(dir, "PrintOut.java");
			if(!printOutFile.exists()) {
			writer = new PrintWriter(printOutFile);
			String printOutFileCode = processTemplate("printout.vm", pclass);
			writer.println(printOutFileCode);
			writer.close();
			// Put the generated class into the result list
			result.add(printOutFile);
			}
			

		}

		// Remember that you did the generation for this one abstract GOOL class
		printedClasses.add(pclass);
		// Put the generated class into the result list
		result.add(classFile);
		// Go through the dependencies
		// If they are abstract GOOL classes and have not been generated, do
		// that
		// And add the generated classes into the result list
		for (Dependency dependency : pclass.getDependencies()) {
			if (!printedClasses.contains(dependency)
					&& dependency instanceof ClassDef) {
				result.addAll(print((ClassDef) dependency));
			}
		}
		return result;
	}

	public List<File> createAndroidProject(List<File> currentFilelist) {

		File oldAndroidFolder = new File(Settings.get("android_out_dir"));
		File newAndroidFolder = new File(Settings.get("android_out_dir_final"));
		if (newAndroidFolder.exists()) {
			deleteFolder(newAndroidFolder); // Removes all files and the
											// directory as this causes problems for android project creation
		}
		File newAndroidFolderSource = new File(
				Settings.get("android_out_dir_final") + "//src");

		List<File> mainClassFiles = new ArrayList<File>();
		populateMainMethodClasses(oldAndroidFolder, mainClassFiles);
		// Below is done just to determine the package naming and main activity
		// file to create a Android project
		String mainMethodFolderString = mainClassFiles.get(0).getParentFile()
				.getAbsolutePath(); // Get the first main method class
		Settings.setAndroidMainActivity(mainClassFiles.get(0).getName()); //Sets this for later use by the Compiler class
		String folderString = oldAndroidFolder.getAbsolutePath();
		String packageDirectory = mainMethodFolderString
				.replace(folderString, "").replace("/", ".")
				.replaceFirst(".", "");
		//Below ensures that the code is within a package as required by android, if not creates one and
		//updates folders accordingly
		if(packageDirectory.equals("")) {
			packageDirectory = "com.test";
			newAndroidFolderSource = new File(
					Settings.get("android_out_dir_final") + "//src//com//test");
		}
		Settings.setAndroidPackage(packageDirectory);
		String activityString = mainClassFiles.get(0).getName()
				.replace(".java", "");

		// First creates a new Android project
		try {
			String executeCommand = "android create project --target 1 --name AndroidProject --path "
					+ newAndroidFolder.getAbsolutePath()
					+ " --activity "
					+ activityString + " --package " + packageDirectory;
			Process waitForProcess = Runtime.getRuntime().exec(executeCommand);
			try {
				waitForProcess.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Then Copies and replaces all files from temporary directory to
		// android project and deletes the old files
		copyFolder(oldAndroidFolder, newAndroidFolderSource);
		deleteFolder(oldAndroidFolder); // This can be commented out for
										// debugging purposes

		return null; // TODO return a list of all the android files in
						// newAndroidFolderSource
						// return value not used at present but may be needed in
						// future
	}

	private void deleteFolder(File deleteFolder) {
	
		 String[] content;      
	      if(deleteFolder.isDirectory()){  
	          content = deleteFolder.list();  
	          for (int i=0; i<content.length; i++) { 
	         	       	 
	              File myFile = new File(deleteFolder, content[i]);   
	              if (myFile.isDirectory())
	              {
	            	  deleteFolder(myFile);
	              }
	              myFile.delete();  
	          }  
	       }  
	      deleteFolder.delete();
	}

	private void copyFolder(File source, File destination) {
		if (source.isDirectory()) { // Then create a new directory and
									// recursively look at files inside old
									// directory
			if (!destination.exists()) {
				destination.mkdir();
			}

			// All files in directory including sub directories
			String files[] = source.list();

			for (String file : files) {
				// make new source and destination files
				File newSourceFile = new File(source, file);
				File newDestinationFile = new File(destination, file);
				// copy recursively
				copyFolder(newSourceFile, newDestinationFile);
			}
		}

		else {// Otherwise copy the file

			try {
				InputStream is = new FileInputStream(source);
				OutputStream os = new FileOutputStream(destination);
				byte[] buf = new byte[1024];

				int length;
				// copy the file content in bytes
				while ((length = is.read(buf)) > 0) {
					os.write(buf, 0, length);
				}

				is.close();
				os.close();
			} catch (FileNotFoundException e) {

			} catch (IOException ioe) {
				
			}
		}

	}

	/**
	 * Returns a list of all the classes containing main methods
	 * 
	 * @param mainFolder
	 * @param mainClassFiles
	 * @return
	 */
	private void populateMainMethodClasses(File mainFolder, List<File> mainClassFiles) {
		File[] tempFileList = mainFolder.listFiles();
		for (File tempFile : tempFileList) {
			if (tempFile.isDirectory()) {
				populateMainMethodClasses(tempFile, mainClassFiles);
			} else {
				if (tempFile.toString().endsWith("Activity.java")) {
					mainClassFiles.add(tempFile);
				}
			}
		}

	}
}
