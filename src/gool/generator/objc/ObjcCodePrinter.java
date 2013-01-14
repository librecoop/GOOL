package gool.generator.objc;

import gool.generator.common.CodePrinter;

import java.io.File;

/**
 * Provides the basic functionality to generate Objective C code from a list of GOOL
 * classes.
 */
public class ObjcCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generator/objc/templates/";

	public ObjcCodePrinter(File outputDir) {
		super(new ObjcGenerator(), outputDir);
	}

	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}
	
	@Override
	public String getFileName(String className) {
		return className + ".java";
	}

}
