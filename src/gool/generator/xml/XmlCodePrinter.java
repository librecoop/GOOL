package gool.generator.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import javax.xml.parsers.*;
import logger.Log;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodePrinter;
import gool.generator.java.JavaGenerator;

public class XmlCodePrinter extends CodePrinter {
	
	private java.util.HashMap<String, String[]> nodeexclude = new java.util.HashMap<String, String[]>();
	
	int nbNode=0;
	private boolean classdefok = true;

	public XmlCodePrinter(File outputDir) {
		super(new JavaGenerator(), outputDir);
	}
	
	private Set<ClassDef> printedClasses = new HashSet<ClassDef>();


	@Override
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		Document document = null;
		DocumentBuilderFactory fabrique = null;
		List<File> result = new ArrayList<File>();
		String[] letableau2 = {"gool.ast.type.TypeClass"};
		nodeexclude.put("gool.ast.constructs.ClassDef", letableau2);
		String[] letableau3 = {"gool.ast.constructs.ClassDef"};
		nodeexclude.put("gool.ast.constructs.Constructor", letableau3);
		String[] letableau4 = {"gool.ast.constructs.Constant"};
		nodeexclude.put("gool.ast.type.TypeClass", letableau4);
		
		
		System.out.println(pclass.getClass().getName());
		if (nodeexclude.containsKey(pclass.getClass().getName())) {
			System.out.println("Ok");
		}
		
		try {
			//creat document structure
			fabrique = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fabrique.newDocumentBuilder();
			document = builder.newDocument();
			Element racine = (Element) document.createElement("class");
			Element  el = NodeToElement(pclass, document);
			if (el!=null)
				racine.appendChild(el);
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
			
            
            OutputFormat format = new OutputFormat(document);
            format.setEncoding("UTF-8");
            format.setLineWidth(80);
            format.setIndenting(true);
            format.setIndent(4);

            Writer out = new PrintWriter(classFile);
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            
			//Remember that you did the generation for this one abstract GOOL class
			printedClasses.add(pclass);
			result.add(classFile);
		} catch (Exception e) {
			Log.e(e);
			System.exit(1);
		}
		
		return result;
	}

	private Element NodeToElement(Object node, Document document) {
		Element newElement = null;
		if (node==null||node.getClass().getName().equals("gool.generator.xml.XmlPlatform"))
			return null;
		if (node.getClass().isAssignableFrom(gool.ast.constructs.Node.class)) {
			return null;
		}
		if (node.getClass().getName().equals("gool.ast.constructs.ClassDef")) {
			if (classdefok)
				classdefok = false;
			else
				return null;
		}
		newElement = document.createElement(node.getClass().getName().substring(9));
//		newElement.setTextContent(node.toString());

		Method[] meths = node.getClass().getMethods();
		for (Method meth: meths) {
			Class<?> laCl = meth.getReturnType();
			if (gool.ast.constructs.Node.class.isAssignableFrom(laCl) && (meth.getParameterTypes().length==0)) {
				nbNode++;
				System.out.println(laCl.getName() + "\n" + nbNode);
				try {
					gool.ast.constructs.Node newNode = (gool.ast.constructs.Node)meth.invoke(node);
					boolean test = (newNode==node)?true:false;
					if(nodeexclude.containsKey(node.getClass().getName())) {
						for (String cla : nodeexclude.get(node.getClass().getName()) ) {
							if (newNode==null||newNode.getClass().getName().equals(cla))
								test=true;
						}
					}
					if (test) {
						Element newElement2 = document.createElement(node.getClass().getName().substring(9));
						newElement2.setTextContent(node.toString());
						newElement.appendChild(newElement2);
					} else {
						Element el = NodeToElement(newNode, document);
						if (el!=null) {
							newElement.appendChild(el);
						}
					}
				} catch (Exception e) {
					Log.e(e);
					System.exit(1);
				}
			}
			else if (java.util.List.class.isAssignableFrom(laCl)) {
				try {
					java.util.List<gool.ast.constructs.Node> newNodes = (java.util.List<gool.ast.constructs.Node>)meth.invoke(node);
					for (gool.ast.constructs.Node n: newNodes) {
						Element el = NodeToElement(n, document);
						if (el!=null)
							newElement.appendChild(el);
					}
				} catch (Exception e) {
					Log.e(e);
					System.exit(1);
				}
				
			}
			else if (meth.getName().startsWith("get") && !meth.getName().equals("getCode")  && (meth.getParameterTypes().length==0)) {
				try {
					newElement.setAttribute(meth.getName().substring(3), meth.invoke(node)==null?"null":meth.invoke(node).toString());
				}  catch (Exception e) {
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
