package gool.generator.common;

import gool.ast.core.ClassDef;
import gool.ast.core.Field;
import gool.ast.core.Language;
import gool.ast.core.Meth;
import gool.ast.core.Modifier;
import gool.ast.core.VarDeclaration;
import gool.ast.type.IType;
import gool.ast.type.TypeUnknown;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeneratorMatcher {

	private static Platform OutputLang;

	static public void init(Platform outputLang) {
		OutputLang = outputLang;
	}

	public static String matchGoolClass(String goolClass) {
		try {
			InputStream ips = new FileInputStream(
					getPathOfOutputClassMatchFile(goolClass));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				line = removeSpaces(line);
				if (isOutputMatchLine(line)) {
					String currentGoolClass = getLeftPartOfOutputMatchLine(line);
					String currentOutputClass = getRightPartOfOutputMatchLine(line);
					if (currentGoolClass.equals(goolClass))
						return currentOutputClass;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	public static String matchGoolMethod(String goolMethod) {
		try {
			InputStream ips = new FileInputStream(
					getPathOfOutputMethodMatchFile(goolMethod.substring(0, goolMethod.lastIndexOf("."))));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				line = removeSpaces(line);
				if (isOutputMatchLine(line)) {
					String currentGoolMethod = getLeftPartOfOutputMatchLine(line);
					String currentOutputMethod = getRightPartOfOutputMatchLine(line);
					if (currentGoolMethod.equals(goolMethod))
						return currentOutputMethod;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	public static String matchGoolClassImplementation(String goolClass,
			String implementationFileName) {
		
		String classImplementation = null;
		try {
			InputStream ips = new FileInputStream(
					getPathOfOutputClassImplementationFile(goolClass,
							implementationFileName));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			classImplementation = "";
			while ((line = br.readLine()) != null) {
				classImplementation += (line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return classImplementation;
	}
	
	public static ArrayList<String> matchImports(String goolClass){
		try {
			InputStream ips = new FileInputStream(
					getPathOfOutputImportMatchFile(goolClass));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			while ((line = br.readLine()) != null) {
				line = removeSpaces(line);
				if (isOutputMatchLine(line)) {
					String currentGoolClass = getLeftPartOfOutputMatchLine(line);
					ArrayList<String> currentImports = parseCommaSeparatedValues(getRightPartOfOutputMatchLine(line));
					if (currentGoolClass.equals(goolClass))
						return currentImports;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	static private String getPathOfOutputMatchDir(String goolClass) {
		String goolPackageName = goolClass.replace('.', '/');
		return "src/gool/generator/" + OutputLang.toString().toLowerCase()
				+ "/matching/" + goolPackageName + "/";
	}

	static private String getPathOfOutputClassMatchFile(String goolClass) {
		return getPathOfOutputMatchDir(goolClass.substring(0,
				goolClass.lastIndexOf(".")))
				+ "ClassMatching.properties";
	}
	
	static private String getPathOfOutputMethodMatchFile(String goolClass) {
		return getPathOfOutputMatchDir(goolClass.substring(0,
				goolClass.lastIndexOf(".")))
				+ "MethodMatching.properties";
	}
	
	static private String getPathOfOutputClassImplementationFile(String goolClass, String implementationFileName) {
		return getPathOfOutputMatchDir(goolClass.substring(0,
				goolClass.lastIndexOf(".")))
				+ implementationFileName;
	}

	static private String getPathOfOutputImportMatchFile(String goolClass) {
		return getPathOfOutputMatchDir(goolClass.substring(0,
				goolClass.lastIndexOf(".")))
				+ "ImportMatching.properties";
	}

	static private String removeSpaces(String line) {
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
				line = line.substring(0, i) + line.substring(i + 1);
				i -= 1;
			}
		}
		return line;
	}

	static private ArrayList<String> parseCommaSeparatedValues(String csv) {
		ArrayList<String> parsedValues = new ArrayList<String>();
		csv+=";";
		while (!csv.isEmpty()) {
			int ind1 = csv.indexOf(",");
			int ind2 = csv.indexOf(";");
			if (ind1 != -1) {
				parsedValues.add(csv.substring(0, ind1));
				csv = csv.substring(ind1 + 1);
			} else {
				parsedValues.add(csv.substring(0, ind2));
				csv = csv.substring(ind2 + 1);
			}
		}
		return parsedValues;
	}
	
	static private boolean isCommentLine(String line) {
		return line.startsWith("#");
	}

	static private boolean isOutputMatchLine(String line) {
		return !isCommentLine(line) && line.contains("->");
	}

	static private String getLeftPartOfOutputMatchLine(String OutputMatchLine) {
		return OutputMatchLine.substring(0, OutputMatchLine.indexOf("->"));
	}

	static private String getRightPartOfOutputMatchLine(String OutputMatchLine) {
		return OutputMatchLine.substring(OutputMatchLine.indexOf("->") + 2);
	}

}
