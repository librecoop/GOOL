package gool.methods;

import gool.ast.constructs.Language;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;

public class MethodManager {
	
	private static class MethDef{
		@SuppressWarnings("unused")
		public String name;
		@SuppressWarnings("unused")
		public String corps;
		@SuppressWarnings("unused")
		public String returnType;
		@SuppressWarnings("unused")
		public String comment;
		
		public MethDef(String name, String corps, String returnType, String comment) {
			this.name = name;
			this.corps = corps;
			this.returnType = returnType;
			this.comment = comment;
		}
	}
	
	public static HashMap<String, MethDef> methPerso = new HashMap<String, MethDef>();
	
	public static void addMeth(String name, String corps, String library, String arg){
		String retType = corps.substring(corps.indexOf(":")+1);
		corps = corps.replaceAll("[$]arg", arg);
		corps = corps.replaceAll("[$]s", " ");
		corps = corps.replaceAll("[{}]", "");
		corps = corps.substring(0,corps.indexOf(":"));
		methPerso.put(library, new MethDef(name, corps, retType, "test comment"));
	}

	public static String getGeneralName(String formatedName, String methodLibrary, Language l){
		String fileName = getFileName(l.name(), methodLibrary);
		String res = "";
		try{
			InputStream ips= new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			formatedName = formatedName.replaceAll("\\s","");
			while ((ligne=br.readLine())!=null){
				ligne = ligne.replaceAll("\\s","");
				if(ligne.contains(formatedName)){
					res = ligne;
					res = res.substring(0,res.indexOf("="));
					break;
				}
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return res;
	}
	
	public static String getGeneralName(String retType, String name, List<Type> list, String methodLibrary, Language l){
		String formatedName = String.format("%s(%s) : %s", name, StringUtils.join(list, ", "), retType);		
		return MethodManager.getGeneralName(formatedName, methodLibrary, l);
	}
	
	public static String getSpecificName(String generalName, String methodLibrary, Language l){
		String fileName = getFileName(l.name(), methodLibrary);
		String res = "";
		
		try{
			InputStream ips= new FileInputStream(fileName); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				if(ligne.contains(generalName)){
					res = ligne.replaceAll("\\s","");
					res = res.substring(res.indexOf("=")+1);
					break;
				}
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return res;
	}
	
	public static boolean isAbsent(String name){
		return name.equals("");
	}
	
	public static boolean isMethPerso(String name){
		return name.matches("[{].*[}].*");
	}
	
	private static String getFileName(String language, String methodLibrary){
		return "src/gool/methods/" + language.toLowerCase()+"/"+methodLibrary.toLowerCase()+".method";
	}
	
	@SuppressWarnings("unused")
	private static String getCommentFileName(String methodLibrary){
		return "src/gool/methods/comment."+methodLibrary.toLowerCase()+".method";
	}
	
}
