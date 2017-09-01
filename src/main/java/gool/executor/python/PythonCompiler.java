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

package gool.executor.python;

import gool.executor.Command;
import gool.executor.common.SpecificCompiler;
import logger.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

public class PythonCompiler extends SpecificCompiler {

	public PythonCompiler(File pythonOutputDir, ArrayList<File> arrayList) {
		super(pythonOutputDir, arrayList);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
					throws FileNotFoundException {
		return files.get(0);
	}

	@Override
	public String getSourceCodeExtension() {
		return "py";
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {

		Map<String, String> env = new HashMap<String, String>();
		env.put("PYTHONPATH", getOutputDir().getAbsolutePath());

		List<String> params = new ArrayList<String>();
		params.add("python");
		params.add("-B"); // do not produce compiled files
		params.add(file.getAbsolutePath());
		Log.d("<PythonCompiler - run> python -B " + file.getAbsolutePath());
		return Command.exec(getOutputDir(), params, env);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * note : the docker container must have python > 3 installed.
	 */
	@Override
	public List<String> compileAndRunWithDocker(Map<String,String> files, String mainFileName, String dockerImage){
		if (files.isEmpty())
			return new ArrayList<String>(Arrays.asList("", "No input files found."));
		mainFileName = getMainFileName(files, mainFileName);
		if (mainFileName == null){
			return new ArrayList<String>(Arrays.asList("", "Bad main file name."));
		}
		System.out.println("Main File Name = " + mainFileName);
		Iterator<Map.Entry<String, String>> it = files.entrySet().iterator();
		// Define the docker run command :
		String dockCommand = "docker run " + dockerImage + " /bin/bash -c '";
		// add goolHelper directory
		dockCommand += " mkdir goolHelper && ";
		// copy the files locally
		for (;it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			String content = entry.getValue().replace("'__main__'", "\"__main__\"");
			dockCommand += "echo -e \"" + StringEscapeUtils.escapeJava(content) + "\" > " + entry.getKey() + " && ";

		}

		// The docker container must have the g++ compiler
		dockCommand += " python " + mainFileName + "'";
		return Command.execDocker(dockCommand);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
					throws FileNotFoundException {
		return mainFile;
	}
}
