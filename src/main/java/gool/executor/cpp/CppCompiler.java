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

package gool.executor.cpp;

import gool.Settings;
import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import logger.Log;

public class CppCompiler extends SpecificCompiler {

	@SuppressWarnings("unused")
	private static final boolean IS_WINDOWS = System.getProperty("os.name")
	.toUpperCase().contains("WINDOWS");

	public CppCompiler(File outputDir, List<File> deps) {
		super(outputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
					throws FileNotFoundException {
		List<String> params = new ArrayList<String>();
		if (mainFile == null) {
			mainFile = files.get(0);
		}
		Log.i("--->" + mainFile);

		String execFileName = mainFile.getName().replace(".cpp", ".bin");
		params.addAll(Arrays.asList(Settings.get("cpp_compiler_cmd"), "-I",
				Settings.get("boost_lib_dir"), "-o", execFileName));
		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add(dependency.getAbsolutePath());
			}
		}

		for (File dependency : getDependencies()) {
			params.add(dependency.getAbsolutePath());
		}

		for (File file : files) {
			if (!params.contains(file.toString()))
				params.add(file.toString());
		}

		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * note : the docker container must have the g++ compiler and the libboost-dev installed.
	 */
	@Override
	public List<String> compileAndRunWithDocker(Map<String,String> files, String mainFileName, String dockerImage){
		if (files.isEmpty())
			return new ArrayList<String>(Arrays.asList("", "No input files found."));
		mainFileName = getMainFileName(files, mainFileName);
		if (mainFileName == null){
			return new ArrayList<String>(Arrays.asList("", "Bad main file name."));
		}	
		Iterator<Map.Entry<String, String>> it = files.entrySet().iterator();
		// Define the docker run command :
		String dockCommand = "docker run " + dockerImage + " /bin/bash -c '";
		// copy the files locally
		for (;it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			dockCommand += "echo -e \"" + StringEscapeUtils.escapeJava(entry.getValue()) + "\" > " + entry.getKey() + " && ";
		}

		// The docker container must have the g++ compiler
		dockCommand += "g++ " + mainFileName + " -I/usr/include/" + "&& ./a.out'";
		return Command.execDocker(dockCommand);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();

		List<String> deps = new ArrayList<String>();
		if (classPath != null) {
			for (File dependency : classPath) {
				deps.add(dependency.getParent());
			}
		}
		for (File dependency : getDependencies()) {
			deps.add(dependency.getParent());
		}

		Map<String, String> env = new HashMap<String, String>();

		params.addAll(Arrays.asList("./" + file.getName()));
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public String getSourceCodeExtension() {
		return "cpp";
	}
}