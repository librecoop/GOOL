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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides the basic functionality to generate Python code from a list of GOOL
 * classes.
 */
public class PythonCodePrinter extends CodePrinter {
	
	private static final String TEMPLATE_DIR = "gool/generator/python/templates/";
	private boolean addgoolHelper = false;
	
	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}
	
	private Map<String, String> getGoolHelperModule(String outputDir) {

		Map<String, String> res = new HashMap<String, String>();

		try{
			File gooHelperDir = new File(ClassLoader.getSystemResource(
					"gool/generator/python/goolHelper").getFile());
			for (File f : gooHelperDir.listFiles()){
				if (f.isFile()){
					String code = "";
					FileReader fr = new FileReader(f);
					BufferedReader br = new BufferedReader(fr);
					String line;
					while ((line = br.readLine()) != null) {
						if (!line.startsWith("#"))
							code += line + "\n";
					}
					res.put(outputDir + "goolHelper" + File.separator +
							f.getName(), code);
					br.close();
				}
			}
		} catch (Exception e) {
			Log.e(e);
		}
		return res;
	}

	public PythonCodePrinter(File outputDir, Collection<File> myF) {
		super(new PythonGenerator(), outputDir, myF);
	}

	@Override
	public String getFileName(String className) {
		return className + ".py";
	}

	@Override
	public Map<String, String> print(ClassDef pclass) {
		Map<String, String> res = super.print(pclass);
		addInitFile(res);
		String outPutDir = ""; 
		if (!getOutputDir().getName().isEmpty())
			outPutDir = getOutputDir().getAbsolutePath() +
			File.separator;
		if (isAddgoolHelper())
			res.putAll(getGoolHelperModule(outPutDir));
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
			String initName = subDir + "__init__.py";
			if (!pyFiles.containsKey(initName))
				pyFiles.put(initName, "");
		}
	}

	public boolean isAddgoolHelper() {
		return addgoolHelper;
	}

	public void setAddgoolHelper(boolean addgoolHelper) {
		this.addgoolHelper = addgoolHelper;
	}
}
