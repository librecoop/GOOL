package gool.generator.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import logger.Log;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodePrinter;

public class XmlCodePrinter extends CodePrinter {
	
	

	public XmlCodePrinter(File outputDir) {
		super(null, outputDir);
	}
	
	private Set<ClassDef> printedClasses = new HashSet<ClassDef>();


	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		Document document = null;
		DocumentBuilderFactory fabrique = null;
		
		try {
			fabrique = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fabrique.newDocumentBuilder();
			document = builder.newDocument();
			Element racine = (Element) document.createElement("class");
			document.appendChild(racine);
			// file separator is just a slash in Unix
			// so the second argument to File() is just the directory 
			// that corresponds to the package name
			// the first argument is the default output directory of the platform
			// so the directory name ends up being something like
			// GOOLOUPTUTTARGET/pack/age
			File dir = new File(getOutputDir().getAbsolutePath(), StringUtils
					.replace(pclass.getPackageName(), ".", File.separator));
			//Typically the outputdir was created before, but not the package subdirs
			dir.mkdirs(); 
			//Create the file for the class, fill it in, close it
			File classFile = new File(dir, getFileName(pclass.getName()));
			Log.i(String.format("Writing to file %s", classFile));
			PrintWriter writer = new PrintWriter(classFile);
			writer.println(document);
			writer.close();
			//Remember that you did the generation for this one abstract GOOL class
			printedClasses.add(pclass);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return super.print(pclass);
	}

	@Override
	public Collection<File> print(Collection<ClassDef> generatedClassDefs,
			boolean isGool) throws FileNotFoundException {
		return super.print(generatedClassDefs, isGool);
	}

	@Override
	public String getFileName(String className) {
		return className + ".xml";
	}

	@Override
	public String getTemplateDir() {
		return "";
	}

}
