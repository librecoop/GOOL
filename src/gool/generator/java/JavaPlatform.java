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

package gool.generator.java;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.java.JavaCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the default Java Platform. It uses the output folders specified in
 * Settings. It uses JavaCodePrinter for concrete Java generation. It uses
 * JavaCompiler for compiling and executing the generated concrete Java. Since
 * it is fully specified, and since there should only ever be one instance of
 * it, this could almost have been a static class. But since platforms in
 * general are objects, this is not the case. Still, we make sure that there is
 * always only one instance of this object.
 */
public final class JavaPlatform extends Platform {

	private final String outputDir = Settings.get("java_out_dir");

	private JavaPlatform(Collection<File> myFile) {
		super("JAVA", myFile);
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new JavaCodePrinter(new File(outputDir), myFileToCopy);
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new JavaCompiler(new File(outputDir), new ArrayList<File>());
	}

	/**
	 * What follows is to make sure that we always use the same instance of this
	 * object.
	 */

	private static JavaPlatform instance = new JavaPlatform(myFileToCopy);

	public static JavaPlatform getInstance(Collection<File> myF) {
		myFileToCopy = myF;
		return instance;
	}

	public static JavaPlatform getInstance() {
		if (myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		return instance;
	}

	public static void newInstance() {
		instance = new JavaPlatform(myFileToCopy);
	}
}
