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





package gool.generator.objc;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodePrinter;
import gool.methods.MethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Provides the basic functionality to generate Objective C code from a list of
 * GOOL classes.
 */
public class ObjcCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generator/objc/templates/";

	public ObjcCodePrinter(File outputDir) {
		super(new ObjcGenerator(), outputDir);
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
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		if (pclass.getParentClass() != null) {
			pclass.setParentClass(pclass.getParentClass());
		}
		
		String headerFile = processTemplate("header.vm", pclass);
		PrintWriter writer;
		
		File dir = new File(getOutputDir().getAbsolutePath(), StringUtils
				.replace(pclass.getPackageName(), ".", File.separator));
		dir.mkdirs();
		File classFile = new File(dir, pclass.getName() + ".h");
		
		writer = new PrintWriter(classFile);
		writer.println(headerFile);
		writer.close();
		
		if (pclass.isEnum() || pclass.isInterface()) {
			List<File> r = new ArrayList<File>();
			r.add(classFile);
			return r;
		} else {
			return super.print(pclass);
		}

	}
	
	@Override
	public List<File> printPersonalLib() throws FileNotFoundException{
		List<File> r = new ArrayList<File>();
		
		for (String s : MethodManager.getMethPerso().keySet()) {
			String headerCode = processTemplate("headerPerso.vm", s);
			String classCode = processTemplate("classPerso.vm", s);
			PrintWriter writer;
			
			System.out.println(MethodManager.getDependencies());
			
			File dir = new File(getOutputDir().getAbsolutePath());
			dir.mkdirs();
			
			File headerFile = new File(dir, s + "OBJC.h");
			writer = new PrintWriter(headerFile);
			writer.println(headerCode);
			writer.close();
			
			File classFile = new File(dir, s + "OBJC.m");
			writer = new PrintWriter(classFile);
			writer.println(classCode);
			writer.close();
			
			r.add(classFile);
		}
		return r;
	}
	
}
