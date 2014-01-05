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
 * Creates C++ files during the C++ generation process
 */

package gool.generator.cpp;

import gool.ast.core.ClassDef;
import gool.generator.common.CodePrinter;
import gool.generator.common.GeneratorMatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import logger.Log;

import org.apache.commons.lang.StringUtils;

/**
 * Provides the basic functionality to generate C++ code from a list of GOOL
 * classes.
 */
public class CppCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generator/cpp/templates/";

	private void createFinallyInclude(File outputDir) {
		FileOutputStream goolHelperOut;
		byte[] buffer = new byte[1024];
		int noOfBytes;

		// Helpers to create by copying the resource
		List<String> goolHelperIn = new ArrayList<String>();
		goolHelperIn.add("finally.h");
		if (!outputDir.isDirectory() && !outputDir.mkdirs()) {
			Log.e(String.format("Impossible to create the directory '%s'",
					outputDir));
		} else {
			// Print finally
			for (String in : goolHelperIn) {
				InputStream helper;
				try {
					helper = CppPlatform.class.getResource(in).openStream();

					goolHelperOut = new FileOutputStream(outputDir + "/" + in);
					while ((noOfBytes = helper.read(buffer)) != -1) {
						goolHelperOut.write(buffer, 0, noOfBytes);
					}
					goolHelperOut.close();
					helper.close();
				} catch (IOException e) {
					Log.e(String.format("Impossible to create the file '%s'",
							in));
				}
			}
		}
	}

	public CppCodePrinter(File outputDir, Collection<File> myF) {
		super(new CppGenerator(), outputDir, myF);
		createFinallyInclude(outputDir);
	}

	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}

	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {

		// GOOL library classes are printed in a different manner
		if (pclass.isGoolLibraryClass()) {
			return printGoolLibraryClass(pclass);
		}
		/*
		 * In C++ the parent class and the interfaces are used in the same
		 * statement. Example: class Foo : public ClassBar1, InterfaceBar2 ...
		 * {}
		 */

		if (pclass.getParentClass() != null) {
			pclass.getInterfaces().add(0, pclass.getParentClass());
		}

		String headerFile = processTemplate("header.vm", pclass);
		PrintWriter writer;

		File dir = new File(getOutputDir().getAbsolutePath(),
				StringUtils.replace(pclass.getPackageName(), ".",
						File.separator));
		dir.mkdirs();
		File classFile = new File(dir, pclass.getName() + ".h");

		writer = new PrintWriter(classFile);
		writer.println(headerFile);
		writer.close();

		/*
		 * Only generate header files if this element is an interface or an
		 * enumeration.
		 */
		if (pclass.isEnum() || pclass.isInterface()) {
			List<File> r = new ArrayList<File>();
			r.add(classFile);
			return r;
		} else {
			return super.print(pclass);
		}
	}

	@Override
	public List<File> printGoolLibraryClass(ClassDef pclass)
			throws FileNotFoundException {
		String goolClass = pclass.getPackageName() + "." + pclass.getName();

		ArrayList<String> goolClassImplems = new ArrayList<String>();
		for (String Import : GeneratorMatcher.matchImports(goolClass))
			if (Import.startsWith("+"))
				goolClassImplems.add(Import.substring(1));

		List<File> result = new ArrayList<File>();
		for (String goolClassImplem : goolClassImplems) {
			String goolClassImplemName = goolClassImplem
					.substring(goolClassImplem.lastIndexOf(".") + 1);
			String goolClassImplemPackage = goolClassImplem.substring(0,
					goolClassImplem.lastIndexOf("."));
			String implemFileName = goolClassImplemName+".cpp";
			String headerFileName = goolClassImplemName+".h";
			String codeImplem = GeneratorMatcher.matchGoolClassImplementation(
					goolClass, implemFileName);
			String codeHeader = GeneratorMatcher.matchGoolClassImplementation(
					goolClass, headerFileName);
			File dir = new File(getOutputDir().getAbsolutePath(),
					StringUtils.replace(goolClassImplemPackage, ".",
							File.separator));
			dir.mkdirs();
			File implemFile = new File(dir, implemFileName);
			File headerFile = new File(dir, headerFileName);
			
			//print implementation file
			PrintWriter writer = new PrintWriter(implemFile);
			writer.println(codeImplem);
			writer.close();
			
			//print header file
			writer = new PrintWriter(headerFile);
			writer.println(codeHeader);
			writer.close();
		}
		printedClasses.add(pclass);
		return result;
	}

	@Override
	public String getFileName(String className) {
		return className + ".cpp";
	}
}
