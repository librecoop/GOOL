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

package gool.generator.python;

import gool.ast.core.ClassDef;
import gool.generator.common.CodePrinter;
import logger.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides the basic functionality to generate Python code from a list of GOOL
 * classes.
 */
public class PythonCodePrinter extends CodePrinter {

	//	private void createGoolHelperModule(File outputDir) {
	//		FileOutputStream goolHelperOut;
	//		byte[] buffer = new byte[1024];
	//		int noOfBytes;
	//
	//		// Helpers to create by copying the resource
	//		List<String> goolHelperIn = new ArrayList<String>();
	//		goolHelperIn.add("goolHelper/__init__.py");
	//		goolHelperIn.add("goolHelper/IO.py");
	//		goolHelperIn.add("goolHelper/Util.py");
	//
	//		// create the directory
	//		File dir = new File(outputDir + "/goolHelper");
	//		if (!dir.isDirectory() && !dir.mkdirs()) {
	//			Log.e(String.format(
	//					"Impossible to create the module '%s/goolHelper'",
	//					outputDir));
	//		} else {
	//			// Print helpers
	//			for (String in : goolHelperIn) {
	//				InputStream helper;
	//				try {
	//					helper = PythonPlatform.class.getResource(in).openStream();
	//
	//					goolHelperOut = new FileOutputStream(outputDir + "/" + in);
	//					while ((noOfBytes = helper.read(buffer)) != -1) {
	//						goolHelperOut.write(buffer, 0, noOfBytes);
	//					}
	//					goolHelperOut.close();
	//					helper.close();
	//				} catch (IOException e) {
	//					Log.e(String.format("Impossible to create the file '%s'",
	//							in));
	//				}
	//			}
	//		}
	//
	//	}

	public PythonCodePrinter(File outputDir, Collection<File> myF) {
		super(new PythonGenerator(), outputDir, myF);
		//createGoolHelperModule(outputDir);
	}

	@Override
	public String getFileName(String className) {
		return className + ".py";
	}

	@Override
	public String getTemplateDir() {
		return "";
	}

	@Override
	public Map<String, String> print(ClassDef pclass) {
		Map<String, String> res = super.print(pclass);
		addInitFile(res);
		return res;
	}

	private void addInitFile(Map<String, String> pyFiles) {
		Set<String> subDirs = new HashSet<String>();
		for(String fullName : pyFiles.keySet()){
			int sepIndex = fullName.lastIndexOf(File.separator);
			String dirName = "";
			if (sepIndex != -1){
				dirName = fullName.substring(0, sepIndex+1);
			}
			subDirs.add(dirName);
		}
		for(String subDir : subDirs){
			pyFiles.put(subDir + "__init__.py", "");
		}
	}
}
