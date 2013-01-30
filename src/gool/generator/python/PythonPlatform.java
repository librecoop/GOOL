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
	public static void newInstance() {
		instance = new PythonPlatform(myFileToCopy);
	}
}
