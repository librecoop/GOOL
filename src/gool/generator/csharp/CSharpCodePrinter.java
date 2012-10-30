package gool.generator.csharp;

import gool.generator.common.CodePrinter;

import java.io.File;

/**
 * Provides the basic functionality to generate C# code from a list of GOOL
 * classes.
 */
public class CSharpCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generator/csharp/templates/";

	public CSharpCodePrinter(File outputDir) {
		super(new CSharpGenerator(), outputDir);
	}
	
	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}
	
//	@Override
//	public Collection<? extends File> print(ClassDef pclass) throws FileNotFoundException {
//		if (pclass.getParentClass() != null) {
//			pclass.getInterfaces().add(0, pclass.getParentClass());
//		}
//		super.print(pclass);
//		return null;
//	}

	@Override
	public String getFileName(String className) {
		return className + ".cs";
	}
}
