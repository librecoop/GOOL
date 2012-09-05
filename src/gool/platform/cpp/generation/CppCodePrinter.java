package gool.platform.cpp.generation;

import gool.ast.ClassDef;
import gool.ast.ClassMain;
import gool.ast.MainMeth;
import gool.ast.Meth;
import gool.platform.common.CodePrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Provides the basic functionality to generate C++ code from a list of GOOL
 * classes.
 */
public class CppCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/platform/cpp/generation/templates/";

	public CppCodePrinter(File outputDir) {
		super(new CppGenerator(), outputDir);
	}

	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}

	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		/*
		 * In C++ the parent class and the interfaces are used in the same
		 * statement. Example: class Foo : public ClassBar1, InterfaceBar2 ...
		 * {}
		 */
		if (pclass.getParentClass() != null) {
			pclass.getInterfaces().add(0, pclass.getParentClass());
		}

		String headerFile = processTemplate("header.vm", pclass);
		PrintWriter writer;

		File dir = new File(getOutputDir().getAbsolutePath(), StringUtils
				.replace(pclass.getPackageName(), ".", File.separator));
		dir.mkdirs();
		File classFile = new File(dir, pclass.getName() + ".h");

		writer = new PrintWriter(classFile);
		writer.println(headerFile);
		writer.close();

		/*
		 * Only generate header files if this element is an interface or an
		 * enumeration.
		 */
		if (pclass.isEnum() || pclass.isInterface()) {
			List<File> r = new ArrayList<File>();
			r.add(classFile);
			return r;
		} else {
			return super.print(pclass);
		}
	}

	@Override
	public String getFileName(String className) {
		return className + ".cpp";
	}

}
