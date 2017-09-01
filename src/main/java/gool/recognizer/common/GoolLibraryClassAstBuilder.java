package gool.recognizer.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gool.ast.core.ClassDef;
import gool.ast.core.Modifier;
import gool.ast.core.Package;
import gool.ast.core.RecognizedDependency;
import logger.Log;

public class GoolLibraryClassAstBuilder {

	static private ArrayList<String> AlreadyBuiltClassNames;
	static private ArrayList<ClassDef> AlreadyBuiltAsts;

	static public void init() {
		AlreadyBuiltClassNames = new ArrayList<String>();
		AlreadyBuiltAsts = new ArrayList<ClassDef>();
	}

	/*
	 * methods used by the GoolMatcher to build a ClassDef from a GoolClass
	 */
	static public ClassDef buildGoolClass(String goolClass) {
		if (AlreadyBuiltClassNames.contains(goolClass)
				/*|| !isGoolClass(goolClass)*/)
			return null;

		ClassDef GoolClassAST = new ClassDef(goolClass.substring(goolClass
				.lastIndexOf(".") + 1));
		GoolClassAST.setIsGoolLibraryClass(true);
		GoolClassAST.setIsEnum(false);
		GoolClassAST.setIsInterface(false);
		GoolClassAST.addModifier(Modifier.PUBLIC);
		GoolClassAST.setPpackage(new Package(goolClass.substring(0,
				goolClass.lastIndexOf("."))));

		ArrayList<String> goolClassDependencies = new ArrayList<String>();
		Log.d("<GoolLibraryClassAstBuild - buildGoolClass> Gool Class : " + goolClass);
		String goolClassPath = getPathOfDefinitionFile(goolClass);
		Log.d("<GoolLibraryClassAstBuild - buildGoolClass> Gool Class Path : " + goolClassPath);
		if(isGoolClass(goolClassPath)){
			Log.d("<GoolLibraryClassAstBuild - buildGoolClass> exists.");
			try {
				InputStream ips = ClassLoader.getSystemResourceAsStream(goolClassPath);
				InputStreamReader ipsr = new InputStreamReader(ips);
				BufferedReader br = new BufferedReader(ipsr);
				String line;
				while ((line = br.readLine()) != null) {
					line = removeSpaces(line);
					if (!isCommentLine(line)) {
						if (line.startsWith("[field]")) {
							/*
						String fieldName = line.substring(
								line.lastIndexOf("]") + 1, line.indexOf(":"));
						String fieldTypeName = line
								.substring(line.indexOf(":") + 1);
						IType fieldType = typeNameToNode(fieldTypeName);
						GoolClassAST.addField(new Field(Modifier.PRIVATE,
								fieldName, fieldType));
							 */
						} else {
							if (line.startsWith("[method]")
									|| line.startsWith("[constructor]")) {
								// GoolMethodImplementation goolMethod = new
								// GoolMethodImplementation(goolClass,
								// line.substring(line.lastIndexOf("]")+1));
								/*
							IType returnType;
							String methodName;
							if (line.startsWith("[constructor]")) {
								returnType = TypeNone.INSTANCE;
								methodName = "init";
							} else {
								returnType = typeNameToNode(line.substring(line
										.indexOf(":") + 1));
								methodName = line.substring(
										line.lastIndexOf("]") + 1,
										line.indexOf("("));
							}

							GoolMethodImplementation goolMethod = new GoolMethodImplementation(
									returnType, Modifier.PUBLIC, methodName,
									goolClass, line.substring(line
											.lastIndexOf("]") + 1));
								 */
								/*
								 * List<String> parameterTypes =
								 * Arrays.asList(line.substring
								 * (line.indexOf("(")+1,line
								 * .indexOf(")")).split(",")); for(String
								 * parameterType : parameterTypes)
								 * goolMethod.addParam
								 */

								//goolMethod.setClassDef(GoolClassAST);
								//GoolClassAST.addMethod(goolMethod);
							} else {
								if (line.startsWith("[dependency]")) {
									String depname = line.substring(line.indexOf("]") + 1);
									goolClassDependencies.add(depname);
									Log.d("<GoolLibraryClassAstBuild - buildGoolClass> Adding class dependency : "
											+ depname);
								}
							}
						}
					}
				}
				br.close();
			} catch (Exception e) {
				Log.w(e);
			}
		}
		AlreadyBuiltClassNames.add(goolClass);
		AlreadyBuiltAsts.add(GoolClassAST);
		for (String goolClassDependency : goolClassDependencies) {
			GoolClassAST.addDependency(new RecognizedDependency(
					goolClassDependency));
			buildGoolClass(goolClassDependency);
		}

		return GoolClassAST;
	}

	/*
	 * Converts a textual type into the corresponding GOOL AST type node.
	 */
	/*
	static private IType typeNameToNode(String typeName) {
		if (typeName.equals("TypeString"))
			return TypeString.INSTANCE;
		else {
			if (typeName.startsWith("*")) {
				return new TypeGoolLibraryClass(typeName.substring(1));
			} else {
				return new TypeGoolClassToMatch(typeName);
			}
		}
	}*/

	static private boolean isGoolClass(String goolClass) {
		if (ClassLoader.getSystemResource(goolClass) == null)
			return false;
		return true;
	}

	/*
	 * file access methods
	 */
	static private String getPathOfDefinitionFile(String goolClass) {
		String goolPackageName = goolClass.substring(0,
				goolClass.lastIndexOf("."));
		goolPackageName = goolPackageName.replace('.', '/');
		String goolShortClassName = goolClass.substring(goolClass
				.lastIndexOf(".") + 1);
		return "gool/library/" + goolPackageName + "/"
		+ goolShortClassName;
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

	static public ArrayList<String> getBuiltClassNames() {
		return AlreadyBuiltClassNames;
	}

	static public ArrayList<ClassDef> getBuiltAsts() {
		return AlreadyBuiltAsts;
	}
}
