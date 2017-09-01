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

package gool.executor.objc;

import gool.Settings;
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
import org.apache.log4j.Logger;

public class ObjcCompiler extends SpecificCompiler {

	private static Logger logger = Logger.getLogger(ObjcCompiler.class
			.getName());
	@SuppressWarnings("unused")
	private static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toUpperCase().contains("WINDOWS");

	public ObjcCompiler(File outputDir, List<File> deps) {
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

		logger.info("--->" + mainFile);
		String execFileName = mainFile.getName().replace(".m", "");
		params.addAll(Arrays.asList(Settings.get("objc_compiler_cmd")));
		for (File file : files) {
			if (!params.contains(file.toString()))
				params.add(file.toString());
		}
		// the -std=gnu99 option is used to allow initial declarations in 'for'
		// loop
		params.addAll(Arrays.asList("-std=gnu99", "-lgnustep-base", "-o",
				execFileName));

		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add(dependency.getAbsolutePath());
			}
		}

		// The command getObjcFlags returns the flags that have to be added
		// to the ObjC compilation command
		List<String> getObjcFlags = new ArrayList<String>();
		getObjcFlags.addAll(Arrays.asList("gnustep-config", "--objc-flags"));
		String flags = Command.exec(getOutputDir(), getObjcFlags);

		// Here, we add each flag to the command line parameters
		while (flags.contains(" ")) {
			String flag = flags.substring(0, flags.indexOf(" "));
			params.add(flag);
			flags = flags.replace(flag + " ", "");
		}
		if (!flags.isEmpty())
			params.add(flags.substring(0, flags.length() - 1));

		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}

	@Override
	public String getSourceCodeExtension() {
		return "m";
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

	/**
	 * {@inheritDoc}
	 * 
	 * @TODO
	 */
	@Override
	public List<String> compileAndRunWithDocker(Map<String,String> files, String mainFileName, String dockerImage){
		return new ArrayList<String>(Arrays.asList("", ""));
	}
	
	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return null;
	}

}
