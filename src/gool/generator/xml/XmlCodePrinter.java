package gool.generator.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
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

	/**
	 * list every type child node for every node with recursion risk.
	 */
	private static final java.util.HashMap<String, String[]> nodeexclude = new java.util.HashMap<String, String[]>();
	static {
		String[] letableau4 = { "gool.ast.constructs.Constant" };
		nodeexclude.put("gool.ast.type.TypeClass", letableau4);
	}

	/**
	 * list ignored getter for attribute.
	 */
	private static final java.util.ArrayList<String> attrexclude = new java.util.ArrayList<String>();
	static {
		attrexclude.add("getClass");
		attrexclude.add("getAccessModifier");
		attrexclude.add("getHeader");
	}

	/**
	 * node counter for debugging
	 */
	int nbNode = 0;

	/**
	 * Authorize only one ClassDef
	 */
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

		try {
			// creat document structure
			fabrique = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = fabrique.newDocumentBuilder();
			document = builder.newDocument();
			Element racine = (Element) document.createElement("file");
			Element el = NodeToElement(pclass, document);
			if (el != null)
				racine.appendChild(el);
			document.appendChild(racine);

			// file separator is just a slash in Unix
			// so the second argument to File() is just the directory
			// that corresponds to the package name
			// the first argument is the default output directory of the
			// platform
			// so the directory name ends up being something like
			// GOOLOUPTUTTARGET/pack/age
			File dir = new File(getOutputDir().getAbsolutePath(),
					StringUtils.replace(pclass.getPackageName(), ".",
							File.separator));
			// Typically the outputdir was created before, but not the package
			// subdirs
			dir.mkdirs();
			// Create the file for the class, fill it in, close it
			File classFile = new File(dir, getFileName(pclass.getName()));
			Log.i(String.format("Writing to file %s", classFile));

			// Create formating output
			OutputFormat format = new OutputFormat(document);
			format.setEncoding("UTF-8");
			format.setLineWidth(80);
			format.setIndenting(true);
			format.setIndent(4);

			// save to output file
			Writer out = new PrintWriter(classFile);
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(document);

			// Remember that you did the generation for this one abstract GOOL
			// class
			printedClasses.add(pclass);
			result.add(classFile);
			classdefok = true;
		} catch (Exception e) {
			Log.e(e);
			System.exit(1);
		}

		return result;
	}

	/**
	 * Translate GOOL node to XML Element.
	 * 
	 * @param node
	 *            GOOL node
	 * @param document
	 *            XML Document
	 * @return XML Element
	 */
	private Element NodeToElement(Object node, Document document) {
		// element create for return
		Element newElement = null;

		// check if the parameter does not cause trouble
		if (node == null
				|| node.getClass().getName()
						.equals("gool.generator.xml.XmlPlatform"))
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

		// Create the new Element
		newElement = document.createElement(node.getClass().getName()
				.substring(9));

		// find every method to find every child node
		Method[] meths = node.getClass().getMethods();
		for (Method meth : meths) {
			Class<?> laCl = meth.getReturnType();
			// check if the method return type is a node.
			if (gool.ast.constructs.Node.class.isAssignableFrom(laCl)
					&& (meth.getParameterTypes().length == 0)) {
				// Debug for recursion
				// nbNode++;
				// Log.d(laCl.getName() + "\n" + nbNode);
				try {
					gool.ast.constructs.Node newNode = (gool.ast.constructs.Node) meth
							.invoke(node);
					// detect recursion risk.
					boolean recursionRisk = (newNode == node) ? true : false;
					if (nodeexclude.containsKey(node.getClass().getName())) {
						for (String cla : nodeexclude.get(node.getClass()
								.getName())) {
							if (newNode == null
									|| newNode.getClass().getName().equals(cla))
								recursionRisk = true;
						}
					}
					if (recursionRisk) {
						Element newElement2 = document.createElement(node
								.getClass().getName().substring(9));
						newElement2.setTextContent("recursion risk detected!");
						newElement.appendChild(newElement2);
					} else {
						Element el = NodeToElement(newNode, document);
						if (el != null) {
							el.setAttribute("getterName", meth.getName());
							newElement.appendChild(el);
						}
					}
				} catch (Exception e) {
					Log.e(e);
					System.exit(1);
				}
			}
			// if the method return node list.
			else if (java.util.List.class.isAssignableFrom(laCl)) {
				try {
					java.util.List<Object> listObj = (java.util.List<Object>) meth
							.invoke(node);
					for (Object o : listObj) {
						Element el = NodeToElement(o, document);
						if (el != null) {
							el.setAttribute("getterName", meth.getName());
							newElement.appendChild(el);
						}
					}
				} catch (Exception e) {
					Log.e(e);
					System.exit(1);
				}

			}
			// generate XML attribute for getter
			else if (meth.getName().startsWith("get")
					&& !meth.getName().equals("getCode")
					&& (meth.getParameterTypes().length == 0)) {
				try {
					if (!attrexclude.contains(meth.getName()))
						newElement.setAttribute(meth.getName().substring(3),
								meth.invoke(node) == null ? "null" : meth
										.invoke(node).toString());
				} catch (Exception e) {
					Log.e(e);
					System.exit(1);
				}
			}
			// generate XML attribute for iser
						else if (meth.getName().startsWith("is")
								&& !meth.getName().equals("getCode")
								&& (meth.getParameterTypes().length == 0)) {
							try {
								if (!attrexclude.contains(meth.getName()))
									newElement.setAttribute(meth.getName().substring(2),
											meth.invoke(node) == null ? "null" : meth
													.invoke(node).toString());
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
