package gool.platform.gool;

import gool.platform.common.CodePrinter;

import java.io.File;

/**
 * Provides the basic functionality to generate Java code from a list of GOOL
 * classes.
 */
public class GoolCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generation/templates/gool/";

	public GoolCodePrinter(File outputDir) {
		super(new GoolGenerator(), outputDir);
	}
	
	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}

	@Override
	public String getFileName(String className) {
		return className + ".gool";
	}
}
