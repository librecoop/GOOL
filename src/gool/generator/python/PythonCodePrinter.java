package gool.generator.python;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Node;
import gool.generator.common.CodePrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PythonCodePrinter extends CodePrinter {
	
	public String processTemplate(String templateFilename, Node classDef) {
		return classDef.toString();
	}
	
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
	
	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {

		String code = pclass.getCode();

		PrintWriter writer;

		File dir = new File(getOutputDir().getAbsolutePath(), StringUtils
				.replace(pclass.getPackageName(), ".", File.separator));
		dir.mkdirs();
		File classFile = new File(dir, getFileName(pclass.getName()));

		writer = new PrintWriter(classFile);
		writer.println(code);
		writer.close();

		List<File> r = new ArrayList<File>();
		r.add(classFile);
		return r;
	}
}
