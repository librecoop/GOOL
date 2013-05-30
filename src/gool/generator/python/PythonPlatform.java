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





package gool.generator.python;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.python.PythonCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


public class PythonPlatform extends Platform {

	private final String outputDir = Settings.get("python_out_dir");
	
	protected PythonPlatform(Collection<File> myFile) {
		
		
		
		super("PYTHON", myFile);

		
		//Test output folder exists
		File folder = new File(outputDir);
		if(!folder.exists()) {
			folder.mkdir();
		}		
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new PythonCodePrinter(new File(outputDir), myFileToCopy);
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new PythonCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static PythonPlatform instance = new PythonPlatform(myFileToCopy);

	public static PythonPlatform getInstance(Collection<File> myF) {
		myFileToCopy = myF;
		return instance;
	}
	
	public static PythonPlatform getInstance() {
		if(myFileToCopy == null) {
			myFileToCopy = new ArrayList<File>();
		}
		return instance;
	}
	
	public static void newInstance() {
		instance = new PythonPlatform(myFileToCopy);
	}
}
