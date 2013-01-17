package gool.generator.android;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Dependency;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class AndroidCodePrinter extends CodePrinter {	
	

	/**
	 * Provides the basic functionality to generate Android code from a list of GOOL
	 * classes.
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
			 * As the implementation of Android necessitates that an additional
			 * file "Activity" be created if the class contains the main method
			 * this method is overridden to check for this
			 */
			String code = pclass.getCode();

			// file separator is just a slash in Unix
			// so the second argument to File() is just the directory 
			// that corresponds to the package name
			// the first argument is the default output directory of the platform
			// so the directory name ends up being something like
			// GOOLOUPTUTTARGET/pack/age
			File dir = new File(getOutputDir().getAbsolutePath(), StringUtils
					.replace(pclass.getPackageName(), ".", File.separator));
			//Typically the outputdir was created before, but not the package subdirs
			dir.mkdirs(); 
			//Create the file for the class, fill it in, close it
			File classFile = new File(dir, getFileName(pclass.getName()));
			System.out.println(String.format("Writing to file %s", classFile));
			PrintWriter writer = new PrintWriter(classFile);
			writer.println(code);
			writer.close();
			//This part checks whether the class contains a main method/entry point
			if(pclass.isMainClass()) { //Check if class contains main method and if yes create activity class as well
				/**
				 * As standard each .java file with a main method will have a corresponding *Activity.java file
				 */
				File activityFile = new File(dir, getFileName(pclass.getName()).replace(".java", "Activity.java"));
				System.out.println(String.format("Writing to file %s", activityFile));
				writer = new PrintWriter(activityFile);
				String activityClassCode = processTemplate("activity.vm", pclass);
				writer.println(activityClassCode);
				writer.close();
				
			}
			
			//Remember that you did the generation for this one abstract GOOL class
			printedClasses.add(pclass);
			//Put the generated class into the result list
			List<File> result = new ArrayList<File>();
			result.add(classFile);
			//Go through the dependencies
			//If they are abstract GOOL classes and have not been generated, do that
			//And add the generated classes into the result list
			for (Dependency dependency : pclass.getDependencies()) {
				if (!printedClasses.contains(dependency)
						&& dependency instanceof ClassDef) {
					result.addAll(print((ClassDef) dependency));
				}
			}
			return result;
		}
	}