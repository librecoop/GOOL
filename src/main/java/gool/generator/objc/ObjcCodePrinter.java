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

package gool.generator.objc;

import gool.ast.core.ClassDef;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Provides the basic functionality to generate Objective C code from a list of
 * GOOL classes.
 */
public class ObjcCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generator/objc/templates/";

	public ObjcCodePrinter(File outputDir, Collection<File> myF) {
		super(new ObjcGenerator(), outputDir, myF);
	}

	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}

	@Override
	public String getFileName(String className) {
		return className + ".m";
	}

	@Override
	public Map<String, String> print(ClassDef pclass){
		if (pclass.getParentClass() != null) {
			pclass.setParentClass(pclass.getParentClass());
		}
		Map<String, String> completeClassList = new HashMap<String, String>();
		String outPutDir = ""; 
		if (!getOutputDir().getName().isEmpty())
			outPutDir = getOutputDir().getAbsolutePath() + 
			StringUtils.replace(pclass.getPackageName(), ".", File.separator) + 
			File.separator;
		
		completeClassList.put(outPutDir + pclass.getName() + ".h", 
				processTemplate("header.vm", pclass));
		
		if (!pclass.isEnum() && !pclass.isInterface()) {
			completeClassList.putAll(super.print(pclass));
		}
		 return completeClassList;
	}

}
