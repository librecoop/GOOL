package gool.generator.objc;

import gool.generator.GeneratorHelper;
import gool.generator.common.CodePrinter;
import gool.generator.common.exception.VelocityException;
import gool.methods.MethodManager;

import gool.ast.constructs.ClassDef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * Provides the basic functionality to generate Objective C code from a list of
 * GOOL classes.
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
		return className + ".m";
	}
	
	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		if (pclass.getParentClass() != null) {
			pclass.setParentClass(pclass.getParentClass());
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
		
		if (pclass.isEnum() || pclass.isInterface()) {
			List<File> r = new ArrayList<File>();
			r.add(classFile);
			return r;
		} else {
			return super.print(pclass);
		}

	}
	
	
	public List<File> printPersonalLib(){
		for (String s : MethodManager.methPerso.keySet()) {
			
		}
		return null;
	}
	
}
