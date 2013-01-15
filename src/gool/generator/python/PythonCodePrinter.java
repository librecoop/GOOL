package gool.generator.python;

import gool.generator.common.CodePrinter;

import java.io.File;

public class PythonCodePrinter extends CodePrinter {
	
	public PythonCodePrinter(File outputDir) {
		super(new PythonGenerator(), outputDir);
	}
	
	@Override
	public String getFileName(String className) {
		return className + ".py";
	}

	@Override
	public String getTemplateDir() {
		return "";
	}
	
}
