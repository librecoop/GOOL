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

package gool.executor.common;

import gool.ast.constructs.ClassMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class SpecificCompiler {

	public static Logger logger = Logger.getLogger(SpecificCompiler.class
			.getName());
	/**
	 * The output directory.
	 */
	private File currentDir;
	private List<File> dependencies = new ArrayList<File>();

	/**
	 * The main class name.
	 */
	private String mainClassName;

	/**
	 * Creates a new compiler with a specific main class and output directory.
	 * 
	 * @param dir
	 * @param deps
	 */
	public SpecificCompiler(File dir, List<File> deps) {
		this.currentDir = dir;
		this.dependencies = deps;
	}

	public static void cleanOutDir(File outDir) {

		File[] binFiles = outDir.listFiles();
		if (binFiles != null) {
			for (File file : binFiles) {
				file.delete();
			}
		}
	}

	public void addDependencies(List<File> files) {
		dependencies.addAll(files);
	}

	public abstract File compileToExecutable(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException;

	public void compileUtils() {
	}

	/**
	 * Writes the generated files into the output directory.
	 * 
	 * @throws FileNotFoundException
	 */
	public void generateFiles(ClassMain mainClass) throws FileNotFoundException {
		getOutputDir().mkdirs();
		cleanOutDir(getOutputDir());
	}

	/**
	 * Returns the list of dependencies that are needed to compile files.
	 * 
	 * @return a list of files.
	 */
	public List<File> getDependencies() {
		return dependencies;
	}

	/**
	 * Gets the main class name.
	 * 
	 * @return the class name.
	 */
	public final String getMainClassName() {
		return mainClassName;
	}

	/**
	 * Gets the output directory.
	 * 
	 * @return the output directory.
	 */
	public final File getOutputDir() {
		if (!currentDir.exists())
			currentDir.mkdirs();
		return currentDir;
	}

	public abstract String getSourceCodeExtension();

	public String run(File file) throws FileNotFoundException {
		return run(file, null);
	}

	public abstract String run(File file, List<File> classPath)
			throws FileNotFoundException;

	/**
	 * Sets the main class.
	 * 
	 * @param mainClassName
	 *            the class name.
	 */
	public final void setMainClassName(String mainClassName) {
		this.mainClassName = mainClassName;
	}

	/**
	 * Sets the output directory.
	 * 
	 * @param outputDir
	 *            a File pointing to an existing directory.
	 */
	public final void setOutputDir(File outputDir) {
		this.currentDir = outputDir;
	}

	public abstract File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException;

}
