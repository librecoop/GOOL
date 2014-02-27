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

package gool.test;

import gool.GOOLCompiler;
import gool.ast.core.ClassDef;
import gool.executor.ExecutorHelper;
import gool.generator.common.Platform;
import gool.parser.java.JavaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class TestHelper {

	public static String surroundWithClassMain(String input, String className) {
		return surroundWithClass("public static void main(String[] args){"
				+ input + " } ", className, "");
	}

	public static String surroundWithClass(String input, String className,
			String accessModifier) {
		return accessModifier + " class " + className + " { public "
				+ className + "(){} " + input + " } ";
	}

	public static String generateCompileRun(Platform platform, String input,
			String mainClassName) throws Exception, FileNotFoundException {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.runGOOLCompiler(new JavaParser(), platform, input);
		return ExecutorHelper.compileAndRun(platform, files);
	}

	public static List<File> generateCompile(Platform platform,
			Collection<File> inputFiles, ClassDef classDef) throws Exception {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.runGOOLCompiler(new JavaParser(),
				platform, inputFiles);
		return ExecutorHelper.compile(files);
	}

	public static String generateCompileRun(Platform platform,
			Collection<File> inputFiles, File file) throws Exception {
		GOOLCompiler gc = new GOOLCompiler();
		Map<Platform, List<File>> files = gc.runGOOLCompiler(new JavaParser(),
				platform, inputFiles);
		return ExecutorHelper.compileAndRun(platform, files);
	}

}
