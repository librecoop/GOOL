package gool.generator.android;

import gool.generator.common.CodePrinter;

import java.io.File;

public class AndroidCodePrinter extends CodePrinter {	
	

	/**
	 * Provides the basic functionality to generate Android code from a list of GOOL
	 * classes.
	 */
private static final String TEMPLATE_DIR = "gool/generator/android/templates/";

		public AndroidCodePrinter(File outputDir) {
			super(new AndroidGenerator(), outputDir);
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