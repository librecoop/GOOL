package gool.generator.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;

import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import logger.Log;

import gool.ast.constructs.ClassDef;
import gool.ast.type.TypeClass;
import gool.generator.common.CodePrinter;
import gool.generator.java.JavaGenerator;
import gool.imports.java.util.HashMap;

public class XmlCodePrinter extends CodePrinter {
	
	private final HashMap<String, String[]> nodeexclude = new HashMap<String, String[]>();

	int nbNode=0;

	public XmlCodePrinter(File outputDir) {
		super(new JavaGenerator(), outputDir);
	}
	
	private Set<ClassDef> printedClasses = new HashSet<ClassDef>();


	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		Document document = null;
		DocumentBuilderFactory fabrique = null;
		List<File> result = new ArrayList<File>();
		String[] letableau = {ClassDef.class.getName()};
		nodeexclude.put(TypeClass.class.getName(), letableau);
		String[] letableau2 = {TypeClass.class.getName()};
		nodeexclude.put(ClassDef.class.getName(), letableau2);
		
		if(nodeexclude.containsKey(pclass.getClass().getName())) {
			System.out.println("Coucou Couroucou Cou Paloma");
		}
		try {
			//creat document structure
			fabrique = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fabrique.newDocumentBuilder();
			document = builder.newDocument();
			Element racine = (Element) document.createElement("class");
			racine.appendChild(NodeToElement(pclass, document));
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
			
			//save to XML File
			TransformerFactory XML_Fabrique_Transformeur = TransformerFactory.newInstance();
            Transformer XML_Transformeur = XML_Fabrique_Transformeur.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult resultat = new StreamResult(classFile);
            XML_Transformeur.setOutputProperty(OutputKeys.INDENT, "yes");
            XML_Transformeur.transform(source, resultat); 
            
			//Remember that you did the generation for this one abstract GOOL class
			printedClasses.add(pclass);
			result.add(classFile);
		} catch (Exception e) {
			Log.e(e);
			System.exit(1);
		}
		
		return result;
	}

	private Element NodeToElement(gool.ast.constructs.Node node, Document document) {
		Element newElement = null;
		newElement = document.createElement(node.getClass().getName());
		newElement.setTextContent(node.toString());

		Method[] meths = node.getClass().getMethods();
		for (Method meth: meths) {
			Class<?> laCl = meth.getReturnType();
			if (gool.ast.constructs.Node.class.isAssignableFrom(laCl) && (meth.getParameterTypes().length==0)
					&& meth.getName().startsWith("get") && nbNode < 1000) {
				nbNode++;
				try {
					gool.ast.constructs.Node newNode = (gool.ast.constructs.Node)meth.invoke(node);
					boolean test = false;
					if(nodeexclude.containsKey(node.getClass())) {
						System.out.println("Coucou Couroucou Cou Paloma");
						for (String cla : nodeexclude.get(node.getClass().getName()) ) {
							if (newNode.getClass().getName().equals(cla))
								test=true;
						}
					}
					if (test) {
						
					} else {
						System.out.println(newNode.getClass() + "\n" + nbNode);
						newElement.appendChild(NodeToElement(newNode, document));
					}
				} catch (Exception e) {
					Log.e(e);
					System.exit(1);
				}
			}
		}
        return newElement;
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
