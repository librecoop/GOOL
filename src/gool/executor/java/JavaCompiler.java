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





package gool.executor.java;

import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class JavaCompiler extends SpecificCompiler {
	

	public JavaCompiler(File javaOutputDir, List<File> deps) {
		super(javaOutputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		params.add("javac");
	
		params.add("-d");
		params.add(getOutputDir().getAbsolutePath());
		
		params.add("-d");
		params.add(getOutputDir().getAbsolutePath());
		
		addClasspathArgs(classPath, params);
		if (mainFile == null) {
			mainFile = files.get(0);
		}
		
		for (File file : files) {
			params.add(file.toString());
		}
		if (args != null) {
			params.addAll(args);
		}
		Command.exec(getOutputDir(), params);	
		return (new File(getOutputDir(), mainFile.getName().replace(".java", ".class")));
	}
	
	@Override
	public File compileToObjectFile(List<File> files,  File mainFile,
			List<File> classPath, List<String> args) throws FileNotFoundException {
		return compileToExecutable(files, mainFile, classPath, args);
	}
	

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		params.add("java");

		addClasspathArgs(classPath, params);

		params.add(file.getName().replace(".class", ""));
		return Command.exec(getOutputDir(), params);
	}

	private void addClasspathArgs(List<File> classPath, List<String> params) {
		/*
		 * Add the classpath
		 */
		params.add("-classpath");		
		String cp = ".";
		if (classPath != null && !classPath.isEmpty()) {
			cp+= File.pathSeparator + StringUtils.join(classPath, File.pathSeparator);
		}
		if (!getDependencies().isEmpty()) {
			cp += File.pathSeparator + StringUtils.join(getDependencies(), File.pathSeparator);
		}
		params.add(cp);
	}

	@Override
	public String getSourceCodeExtension() {
		return "java";
	}
}
