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

/**
 * Creates C++ files during the C++ generation process
 */

package gool.generator.cpp;

import gool.ast.core.ClassDef;
import gool.ast.core.Dependency;
import gool.ast.core.Field;
import gool.ast.core.Meth;
import gool.ast.core.VarDeclaration;
import gool.ast.type.IType;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodePrinter;
import gool.generator.common.GeneratorMatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import logger.Log;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.texen.Generator;

/**
 * Provides the basic functionality to generate C++ code from a list of GOOL
 * classes.
 */
public class CppCodePrinter extends CodePrinter {
	private static final String TEMPLATE_DIR = "gool/generator/cpp/templates/";

	private Map<String, String> getFinallyHeader(String outputDir) {
		String code = "";
		Map<String, String> res = new HashMap<String, String>();
		try{
			InputStream ips = ClassLoader.getSystemResourceAsStream("gool/generator/cpp/finally.h");
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("/*") && !line.startsWith(" *") && !line.endsWith("*/"))
					code += line + "\n";
			}
		} catch (Exception e) {
			Log.e(e);
			return res;
		}
		res.put(outputDir + "finally.h", code);
		return res;
	}

	public CppCodePrinter(File outputDir, Collection<File> myF) {
		super(new CppGenerator(), outputDir, myF);
	}

	@Override
	public String getTemplateDir() {
		return TEMPLATE_DIR;
	}

	@Override
	public Map<String, String> print(ClassDef pclass){

		// GOOL library classes are printed in a different manner
		if (pclass.isGoolLibraryClass()) {
			return printGoolLibraryClass(pclass);
		}

		Map<String, String> completeClassList = new HashMap<String, String>();
		String outPutDir = ""; 
		if (!getOutputDir().getName().isEmpty())
			outPutDir = getOutputDir().getAbsolutePath() + File.separator +
			StringUtils.replace(pclass.getPackageName(), ".", File.separator) + 
			File.separator;

		Log.d("<CppCodePrinter - print> outputdir : " + outPutDir);

		//completeClassList.put(outPutDir + pclass.getName() + ".h", processTemplate("header.vm", pclass));

		completeClassList.put(outPutDir + pclass.getName() + ".h",
				printClassHeader(pclass));

		/*
		 * Only generate header files if this element is an interface or an
		 * enumeration.
		 */
		if (!pclass.isEnum() && !pclass.isInterface()) {
			completeClassList.putAll(super.print(pclass));
		}
		//completeClassList.putAll(getFinallyHeader(outPutDir));
		return completeClassList;
	}

	@Override
	public Map <String, String> printGoolLibraryClass(ClassDef pclass){
		Map<String, String> result = new HashMap<String, String>();

		String goolClass = pclass.getPackageName() + "." + pclass.getName();

		ArrayList<String> goolClassImplems = new ArrayList<String>();
		for (String Import : GeneratorMatcher.matchImports(goolClass))
			if (Import.startsWith("+"))
				goolClassImplems.add(Import.substring(1));

		for (String goolClassImplem : goolClassImplems) {
			int dotIndex = goolClassImplem.lastIndexOf(".");
			String goolClassImplemName = goolClassImplem;
			String goolClassImplemPackage = "";
			if (dotIndex != -1){
				goolClassImplemName = goolClassImplem.substring(dotIndex + 1);	
				goolClassImplemPackage = goolClassImplem.substring(0, dotIndex);
			}
			Log.d("<CppCodePrinter - printGoolLibraryClass2Strings> " + goolClassImplemName);
			Log.d("<CppCodePrinter - printGoolLibraryClass> " + goolClassImplemPackage);

			String implemFileName = goolClassImplemName+".cpp";
			String headerFileName = goolClassImplemName+".h";
			String codeImplem = GeneratorMatcher.matchGoolClassImplementation(
					goolClass, implemFileName);
			String codeHeader = GeneratorMatcher.matchGoolClassImplementation(
					goolClass, headerFileName);

			String outputDir = ""; 
			if (!getOutputDir().getName().isEmpty()){
				if (dotIndex != -1){
					outputDir = getOutputDir().getAbsolutePath() + File.separator +
							StringUtils.replace(goolClassImplemPackage, ".",
									File.separator) + File.separator;
				}else{
					outputDir = getOutputDir().getAbsolutePath() + File.separator;
				}
			}

			if (codeImplem != null){
				result.put(outputDir + implemFileName, codeImplem);
			}
			if (codeHeader != null){
				result.put(outputDir + headerFileName, codeHeader);
			}
		}
		printedClasses.add(pclass);
		return result;
	}


	private String printInterfaces(ClassDef classDef){
		String toReturn = "";
		int size = classDef.getInterfaces().size();
		if (size > 0){ 
			for(IType iface : classDef.getInterfaces()){
				toReturn = String.format(": public %s, ",
						iface.toString().replaceAll("[ *]*$", ""));
			}
			toReturn = toReturn.substring(0, toReturn.lastIndexOf(","));
		}
		if (classDef.getParentClass() != null){
			if(size > 0)
				toReturn += ", ";
			else
				toReturn += ": public ";
			toReturn += classDef.getParentClass().getName();
		}
		return toReturn;
	}

	public String printClassHeader(ClassDef classDef) {
		Log.MethodIn(Thread.currentThread());

		String header = String.format("#ifndef __%s_H\n", classDef.getName().toUpperCase());
		header += String.format("#define __%s_H\n\n\n", classDef.getName().toUpperCase());

		String body = "";
		if (classDef.isEnum()){
			body += String.format("enum %s{\n",classDef.getName());
			Iterator<Field> it = classDef.getFields().iterator();
			while(it.hasNext()) {
				String fieldName = it.next().getName();
				if(it.hasNext())
					body += String.format("%s,\n", fieldName);
				else
					body += String.format("%s\n", fieldName);
			}
			body += "\n};\n";
		}else{
			body += "class " + classDef.getName() + printInterfaces(classDef) + "{\n";
			for(Field field : classDef.getFields()){
				body += String.format("%s;\n",field.callGetCode());
			}
			for(Meth method : classDef.getMethods()){
				if(method.isGoolMethodImplementation()){
					body += method.getHeader();
				}else if(method.getName().isEmpty()){
					// Constructor
					body += String.format("\t%s : %s(",method.getAccessModifier(),
							classDef.getName());
					Iterator<VarDeclaration> it = method.getParams().iterator();
					while(it.hasNext()) {
						VarDeclaration varDec = it.next();
						if(it.hasNext())
							body += String.format("%s %s,\n", varDec.getType(),
									varDec.getName());
						else
							body += String.format("%s %s\n", varDec.getType(),
									varDec.getName());
					}
					body += ");\n";
				}else if(!method.isMainMethod()){
					body += String.format("\t%s: %s %s(",
							method.getAccessModifier(), method.getType(), method.getName());
					Iterator<VarDeclaration> it = method.getParams().iterator();
					while(it.hasNext()) {
						VarDeclaration varDec = it.next();
						if(it.hasNext())
							body += String.format("%s %s,\n", varDec.getType(),
									varDec.getName());
						else
							body += String.format("%s %s\n", varDec.getType(),
									varDec.getName());
					}
					body += ");\n";
				}
			}
			body +="};\n";
		}

		for(Dependency dep : classDef.getDependencies()){
			if (dep.isHeaderDependency()){
				String depStr = dep.toString();
				if(depStr.startsWith("/*") || depStr.startsWith("//") ||
						depStr.startsWith("#include"))
					header += depStr + "\n";
				else if(!depStr.equals("noprint"))
					header += String.format("#include <%s>\n",depStr);
			}
		}
		
		header += GeneratorHelper.printRecognizedDependencies(classDef) + "\n\n";
		
		return header + body + "\n\n#endif\n";
	}

	@Override
	public String getFileName(String className) {
		return className + ".cpp";
	}
}
