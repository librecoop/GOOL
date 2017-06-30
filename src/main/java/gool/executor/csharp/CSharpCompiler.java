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

package gool.executor.csharp;

import gool.Settings;
import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import logger.Log;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class CSharpCompiler extends SpecificCompiler {
	private static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toUpperCase().contains("WINDOWS");

	public CSharpCompiler(File outputDir, List<File> deps) {
		super(outputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
					throws FileNotFoundException {
		List<String> params = new ArrayList<String>();

		String execFileName = "";
		
		if (mainFile == null) {
			mainFile = files.get(0);
			execFileName += mainFile.getName().replace(".cs", ".exe") + " ";
		}

		params.addAll(Arrays.asList(Settings.get("csharp_compiler_cmd"),
				"-debug+", "/t:exe", "/out:" + execFileName));

		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add("/r:" + dependency.getAbsolutePath());
			}
		}

		for (File dependency : getDependencies()) {
			params.add("/r:" + dependency.getAbsolutePath());
		}

		for (File file : files) {
			params.add(file.toString());
		}
		Log.d("-----------");
		for (String p : params)
			Log.d(p);
		Log.d("-----------");
		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * note : the docker container must have the mcs compiler installed.
	 */
	@Override
	public List<String> compileToExecutableWithDocker(Map<String,String> files, String mainFileName, String dockerImage){
		if (files.isEmpty())
			return new ArrayList();
		String mainFileContent = "";
		Iterator<Map.Entry<String, String>> it = files.entrySet().iterator();
		if (mainFileName == null) {
			Map.Entry<String, String> firstFile = it.next();
			mainFileName = firstFile.getKey();
			mainFileContent = firstFile.getValue();
			it.remove();
		}else{
			for(;it.hasNext();){
				Map.Entry<String, String> entry = it.next();
				if(entry.getKey().equals(mainFileName)) {
					mainFileContent = entry.getValue();
			        it.remove();
			    }
			}
		}		
		Log.i("--->" + mainFileName);
		// Define the docker run command :
		String dockCommand = "docker run " + dockerImage + " /bin/bash -c '";
		// add the main file
		dockCommand += "echo -e \"" + StringEscapeUtils.escapeJava(mainFileContent) + "\" > " +mainFileName + " && ";
		// add the other cpp files if some
		for (;it.hasNext();) {
			Map.Entry<String, String> entry = it.next();
			dockCommand += "echo -e \"" + StringEscapeUtils.escapeJava(entry.getValue()) + "\" > " + entry.getKey() + " && ";
		}

		// The docker container must have the g++ compiler
		dockCommand += " mcs -debug+ " + mainFileName + " && mono " + mainFileName.replace(".cs", ".exe") + "'";
		System.out.println(dockCommand);
		return Command.execDocker(dockCommand);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
					throws FileNotFoundException {
		// TODO Duplicate code in compile and compileAll

		if (mainFile == null) {
			Log.i(files.toString());
			mainFile = files.get(0);
		}

		String execFileName = mainFile.getName().replace(".cs", ".dll");

		List<String> params = new ArrayList<String>();
		params.addAll(Arrays.asList(Settings.get("csharp_compiler_cmd"),
				"-debug+", "/t:library"));

		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add("/r:" + dependency.getAbsolutePath());
			}
		}

		for (File dependency : getDependencies()) {
			params.add("/r:" + dependency.getAbsolutePath());
		}

		for (File file : files) {
			params.add(file.toString());
		}
		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}

	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		String[] runTest = IS_WINDOWS ? new String[] { new File(getOutputDir(),
				file.getName()).getAbsolutePath() } : new String[] { "mono",
						file.getName() };
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
		env.put("MONO_PATH", StringUtils.join(deps, File.separator));

		params.addAll(Arrays.asList(runTest));
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public String getSourceCodeExtension() {
		return "cs";
	}

}
