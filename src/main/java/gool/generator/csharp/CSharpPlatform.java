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

package gool.generator.csharp;

//import gool.executor.common.SpecificCompiler;
//import gool.executor.csharp.CSharpCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public final class CSharpPlatform extends Platform {

	private CSharpPlatform(Collection<File> myFile, String outputDir) {
		super("CSHARP", myFile, outputDir);
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new CSharpCodePrinter(new File(outputDir), myFileToCopy);
	}

	/*@Override
	protected SpecificCompiler initializeCompiler() {
		return new CSharpCompiler(new File(outputDir), new ArrayList<File>());
	}*/

	private static CSharpPlatform instance = new CSharpPlatform(myFileToCopy, outputDir);

	public static CSharpPlatform getInstance(Collection<File> myF, String outDir) {
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

	public static CSharpPlatform getInstance(Collection<File> myF) {
		return getInstance(myF, outputDir);
	}
	
	public static CSharpPlatform getInstance(String outDir) {
		return getInstance(myFileToCopy, outDir);
	}
	
	public static CSharpPlatform getInstance() {
		if (myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		if (outputDir == null){
			outputDir = "";
		}
		return instance;
	}

	public static void newInstance() {
		instance = new CSharpPlatform(myFileToCopy, outputDir);
	}
}
