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

package gool.executor;

import gool.ast.core.ClassDef;
import gool.executor.common.SpecificCompiler;
import gool.generator.common.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import logger.Log;

import org.apache.commons.lang.StringUtils;

public final class ExecutorHelper {

	public static Iterable<? extends JavaFileObject> getJavaFileObjects(
			Collection<? extends File> inputFiles) {
		return ToolProvider.getSystemJavaCompiler()
				.getStandardFileManager(null, null, null)
				.getJavaFileObjectsFromFiles(inputFiles);
	}

	public static ClassDef getMainClass(Collection<ClassDef> classDefs) {
		for (ClassDef classDef : classDefs) {
			if (classDef.isMainClass()) {
				return classDef;
			}
		}
		return null;
	}

	public static String run(Platform platform, File file)
			throws FileNotFoundException {
		return platform.getCompiler().run(file);
	}

	public static String compileAndRun(Platform platform,
			Map<Platform, List<File>> files) throws FileNotFoundException {
		StringBuilder result = new StringBuilder();

		List<File> compiledFiles = ExecutorHelper.compile(files);

		Log.i(compiledFiles.toString());
		for (File f : compiledFiles){
			result.append(platform.getCompiler().run(f));
		}
		return result.toString();
	}

	public static File getClassDefFile(ClassDef classDef) {
		return new File(classDef.getPlatform().getCodePrinter().getOutputDir()
				.getAbsolutePath(), StringUtils.replace(
						classDef.getPackageName(), ".", File.separator));
	}

	public static List<File> compile(Map<Platform, List<File>> files)
			throws FileNotFoundException {
		List<File> result = new ArrayList<File>();
		for (Entry<Platform, List<File>> item : files.entrySet()) {
			SpecificCompiler compiler = item.getKey().getCompiler();
			File outputFile = compiler.compileToExecutable(item.getValue(),
					null, null, null);
			result.add(outputFile);
		}

		return result;
	}
}
