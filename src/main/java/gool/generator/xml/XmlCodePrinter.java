/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool.generator.xml;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.*;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import javax.xml.parsers.*;
import logger.Log;

import gool.ast.core.ClassDef;
import gool.generator.common.CodePrinter;
import gool.generator.java.JavaGenerator;

public class XmlCodePrinter extends CodePrinter {

	/**
	 * list every risked methods for every node with recursion risk.
	 */
	private static final java.util.HashMap<String, String[]> methexclude = new java.util.HashMap<String, String[]>();
	static {
		String[] letableau = { "getClassReference" };
		methexclude.put("gool.ast.type.TypeClass", letableau);
	}

	/**
	 * list ignored getter for attribute.
	 */
	private static final java.util.ArrayList<String> attrexclude = new java.util.ArrayList<String>();
	static {
		attrexclude.add("getClass");
		attrexclude.add("getAccessModifier");
		attrexclude.add("getHeader");
		attrexclude.add("getBlock");
	}

	/**
	 * node counter for debugging
	 */
	int nbNode = 0;

	/**
	 * Authorize only one ClassDef
	 */
	private boolean classdefok = true;

	public XmlCodePrinter(File outputDir, Collection<File> myF) {
		// Chose you're favorite Generator for recognized.
		super(new JavaGenerator(), outputDir, myF);
	}

	private Set<ClassDef> printedClasses = new HashSet<ClassDef>();

	@Override
	public Map<String, String> print(ClassDef pclass){
		Document document = null;
		DocumentBuilderFactory fabrique = null;
		Map<String, String> result = new HashMap<String, String>();
		// Debugging info
		// nbNode = 0;

		try {
			// create document structure
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
			String outPutDir = "";
			if (!getOutputDir().getName().isEmpty())
				outPutDir = getOutputDir().getAbsolutePath() + File.separator +
				StringUtils.replace(pclass.getPackageName(), ".", File.separator) + 
				File.separator;

			Log.d("<XmlCodePrinter - print> outputdir : " + outPutDir);
			StringWriter stringWriter = new StringWriter();

			// Create formating output
			OutputFormat format = new OutputFormat(document);
			format.setEncoding("UTF-8");
			format.setLineWidth(80);
			format.setIndenting(true);
			format.setIndent(4);

			// save to output string
			Writer out = new PrintWriter(stringWriter);
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(document);

			// Remember that you did the generation for this one abstract GOOL
			// class
			printedClasses.add(pclass);
			result.put(outPutDir + pclass.getName() + ".xml", stringWriter.toString());
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
		if (node.getClass().isAssignableFrom(gool.ast.core.Node.class)) {
			return null;
		}
		if (node.getClass().getName().equals("gool.ast.core.ClassDef")) {
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
			if (gool.ast.core.Node.class.isAssignableFrom(laCl)
					&& (meth.getParameterTypes().length == 0) /* && nbNode<1000 */) {
				// Debug for recursion
				// nbNode++;
				// Log.d(laCl.getName() + "\n" + nbNode);
				try {
					gool.ast.core.Node newNode = (gool.ast.core.Node) meth
							.invoke(node);
					// detect recursion risk.
					boolean recursionRisk = (newNode == node) ? true : false;
					if (methexclude.containsKey(node.getClass().getName())) {
						for (String exmeth : methexclude.get(node.getClass()
								.getName())) {
							if (newNode == null
									|| meth.getName().equals(exmeth))
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
	public Map<String, String> print2Strings(Collection<ClassDef> generatedClassDefs,
			boolean isGool){
		return super.print2Strings(generatedClassDefs, isGool);
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
