package gool.methods;

import gool.ast.constructs.Language;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;

public class MethodManager {
	
	private static class MethDef{
		String name;
		@SuppressWarnings("unused")
		String corps;
		
		public MethDef(String name, String corps) {
			this.name = name;
			this.corps = corps;
		}
		
		@Override
		public boolean equals(Object obj) {
			return ((MethDef)obj).name.equals(this.name);
		}
	}
	
	private static HashSet<MethDef> methodPerso = new HashSet<MethDef>();
	
	public static void addMethodPerso(String name, String corps){
		MethodManager.methodPerso.add(new MethDef(name, corps));
	}
	
	public static HashSet<MethDef> getMethodPerso(){
		return MethodManager.methodPerso;
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
	
	private static String getFileName(String language, String methodLibrary){
		return "src/gool/methods/" + language.toLowerCase()+"/"+methodLibrary.toLowerCase()+".method";
	}
	
	@SuppressWarnings("unused")
	private static String getCommentFileName(String methodLibrary){
		return "src/gool/methods/comment."+methodLibrary.toLowerCase()+".method";
	}
	
}
