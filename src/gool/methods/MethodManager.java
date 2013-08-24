package gool.methods;

import gool.ast.constructs.Expression;
import gool.ast.constructs.Language;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MethodManager {

	public static class Param {
		public String name;
		public String type;

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public Param(String name, String type) {
			this.name = name;
			this.type = type;
		}
	}

	public static class MethDef {
		public String name;
		public ArrayList<Param> param;
		public String corps;
		public String returnType;
		public String comment;

		public String getName() {
			return name;
		}

		public ArrayList<Param> getParam() {
			return param;
		}

		public String getCorps() {
			return corps;
		}

		public String getReturnType() {
			return returnType;
		}

		public String getComment() {
			return comment;
		}

		public MethDef(String name, ArrayList<Param> param, String corps,
				String returnType, String comment) {
			this.name = name;
			this.corps = corps;
			this.returnType = returnType;
			this.comment = comment;
			this.param = param;
		}

		@Override
		public int hashCode() {
			return this.name.length() + this.corps.length();
		}

		@Override
		public boolean equals(Object obj) {
			MethDef m = (MethDef) obj;
			return m.name.equals(this.name);
		}
	}

	private static HashMap<String, HashSet<MethDef>> methPerso = new HashMap<String, HashSet<MethDef>>();
	private static HashSet<String> dependencies = new HashSet<String>();

	public static HashSet<MethDef> getMethInLib(String libName) {
		return methPerso.get(libName);
	}

	public static HashMap<String, HashSet<MethDef>> getMethPerso() {
		return methPerso;
	}

	public static HashSet<String> getDependencies() {
		return dependencies;
	}

	public static boolean isParam(Expression s, String corps) {
		String paramReq;
		String type = s.getType().toString().replaceAll("\\s", "");
		paramReq = corps.substring(corps.indexOf("#") + 1);
		paramReq = paramReq.substring(0, paramReq.indexOf("#"));
		paramReq = paramReq.replaceAll("[()]", "");
		String[] array = paramReq.split("[,]");

		for (int j = 0; j < array.length; j++) {
			array[j] = array[j].replaceAll("\\s", "");
			if (type.equalsIgnoreCase((array[j]))) {
				return true;
			}
		}

		return false;
	}

	public static void addMeth(String name, String corps, String library,
			ArrayList<String> typeParam) {
		String retType = corps.substring(corps.indexOf("#:") + 2);
		String comment = "TODO";
		ArrayList<Param> param = new ArrayList<Param>();

		dependencies.add(library);

		try {
			InputStream ips = new FileInputStream(
					getCommentFileName(library.toLowerCase()));
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				if (ligne.contains(name)) {
					comment = ligne;
					comment = comment.substring(comment.indexOf("=") + 1);
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		for (int i = 0; i < typeParam.size(); i++) {
			param.add(new Param("p" + (i + 1), typeParam.get(i)));
		}
		corps = corps.replaceAll("[$]s", " ");
		corps = corps.replaceAll("[{}]", "");
		corps = corps.replaceAll("[;]", ";\n\t\t");
		corps = corps.substring(0, corps.indexOf("#"));

		if (methPerso.get(library) == null)
			methPerso.put(library, new HashSet<MethDef>());
		MethDef m = new MethDef(name, param, corps, retType, comment);
		methPerso.get(library).add(m);
	}

	public static String getGeneralName(String formatedName,
			String methodLibrary, Language l) {
		String fileName = getFileName(l.name(), methodLibrary);
		String res = "";

		try {
			InputStream ips = new FileInputStream(fileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			formatedName = formatedName.replaceAll("\\s", "");
			while ((ligne = br.readLine()) != null) {
				ligne = ligne.replaceAll("\\s", "");
				if (ligne.contains(formatedName)) {
					res = ligne;
					res = res.substring(0, res.indexOf("="));
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e.toString());
			res = null;
		}

		return res;
	}

	public static String getGeneralName(String retType, String name,
			List<String> list, String methodLibrary, Language l) {
		/*
		 * String namestring[] = retType.split("\\.");
		 * retType=namestring[namestring.length-1];
		 */
		String formatedName = String.format("%s(%s) : %s", name,
				StringUtils.join(list, ", "), retType);
		return MethodManager.getGeneralName(formatedName, methodLibrary, l);
	}

	public static String getSpecificName(String generalName,
			String methodLibrary, Language l) {
		String fileName = getFileName(l.name(), methodLibrary);
		String res = "";

		try {
			InputStream ips = new FileInputStream(fileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {
				if (ligne.contains(generalName)) {
					res = ligne.replaceAll("\\s", "");
					res = res.substring(res.indexOf("=") + 1);
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return res;
	}

	public static boolean isAbsent(String name) {
		return name.equals("");
	}

	public static boolean isMethPerso(String name) {
		return name.matches("[{].*[}].*");
	}

	public static String getFileName(String language, String methodLibrary) {
		return "src/gool/methods/" + language.toLowerCase() + "/"
				+ methodLibrary.toLowerCase() + ".method";
	}

	public static String getCommentFileName(String methodLibrary) {
		return "src/gool/methods/comment." + methodLibrary.toLowerCase()
				+ ".method";
	}

	public static void reset() {
		methPerso.clear();
		dependencies.clear();
	}
}
