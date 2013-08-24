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

package gool.executor.python;

import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return mainFile;
	}
}
