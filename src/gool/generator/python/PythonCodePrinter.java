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
