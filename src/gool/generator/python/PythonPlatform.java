package gool.generator.python;

import java.io.File;
import java.util.ArrayList;

import gool.Settings;
import gool.executor.common.SpecificCompiler;
import gool.executor.python.PythonCompiler;
import gool.generator.common.CodePrinter;
import gool.generator.common.Platform;

public class PythonPlatform extends Platform {

	private final String outputDir = Settings.get("python_out_dir");
	
	protected PythonPlatform() {
		super("PYTHON");
	}

	@Override
	protected CodePrinter initializeCodeWriter() {
		return new PythonCodePrinter(new File(outputDir));
	}

	@Override
	protected SpecificCompiler initializeCompiler() {
		return new PythonCompiler(new File(outputDir), new ArrayList<File>());
	}

	private static PythonPlatform instance = new PythonPlatform();

	public static PythonPlatform getInstance() {
		return instance;
	}
	public static void newInstance() {
		instance = new PythonPlatform();
	}
	
}
