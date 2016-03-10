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

package gool.generator.python;

import gool.executor.common.SpecificCompiler;
import gool.executor.python.PythonCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class PythonPlatform extends Platform {

	/**
	 * Name of the directory where the ouput files will be written in.
	 */
	protected static String outputDir;

	public String getOutputDir() {

		return outputDir;
	}

	protected PythonPlatform(Collection<File> myFile, String outDir) {
		super("PYTHON", myFile);
		if (outDir == null)
			outputDir = "";
		else
			outputDir = outDir;
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new PythonCodePrinter(new File(outputDir), myFileToCopy);
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new PythonCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static PythonPlatform instance = new PythonPlatform(myFileToCopy, outputDir);

	public static PythonPlatform getInstance(Collection<File> myF, String outDir) {
		if (myF == null) {
			myFileToCopy = new ArrayList<File>();
		}
		else{
			myFileToCopy = myF;
		}
		if (outDir == null){
			outputDir = "";
		}
		else{
			outputDir = outDir;
		}
		return instance;
	}

	public static PythonPlatform getInstance(Collection<File> myF) {
		return getInstance(myF, outputDir);
	}

	public static PythonPlatform getInstance(String outDir) {
		return getInstance(myFileToCopy, outDir);
	}

	public static PythonPlatform getInstance() {
		if (myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		if (outputDir == null){
			outputDir = "";
		}
		return instance;
	}

	public static void newInstance() {
		instance = new PythonPlatform(myFileToCopy, outputDir);
	}
}
