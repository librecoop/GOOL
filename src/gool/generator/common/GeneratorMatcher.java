package gool.generator.common;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Field;
import gool.ast.constructs.Language;
import gool.ast.constructs.Meth;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.VarDeclaration;
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

	public static String matchGoolMethod(String goolClass,
			String goolMethodSignature) {
		String methodImplementation = null;
		try {
			InputStream ips = new FileInputStream(
					getPathOfOutputMethodImplementationFile(goolClass,
							goolMethodSignature));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			methodImplementation = "";
			while ((line = br.readLine()) != null) {
				methodImplementation += (line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return methodImplementation;
	}

	public static String matchImportDependency(String goolClass) {
		String imports = null;
		try {
			InputStream ips = new FileInputStream(
					getPathOfOutputImportFile(goolClass));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			imports = "";
			while ((line = br.readLine()) != null) {
				imports += (line + "\n");
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return imports;
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

	static private String getPathOfOutputMethodImplementationFile(
			String goolClass, String goolMethodSignature) {
		return getPathOfOutputMatchDir(goolClass) + goolMethodSignature;
	}

	static private String getPathOfOutputImportFile(String goolClass) {
		return getPathOfOutputMatchDir(goolClass.substring(0,
				goolClass.lastIndexOf(".")))
				+ goolClass.substring(goolClass.lastIndexOf(".") + 1)
				+ ".imports";
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
