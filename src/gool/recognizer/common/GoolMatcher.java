/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
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





package gool.recognizer.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Language;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.type.TypeUnknown;
import gool.generator.common.Platform;
import gool.imports.java.util.ArrayList;

public class GoolMatcher{

	private static Language InputLang;
	private static Platform OutputLang;
	private static ArrayList<String> EnabledGoolLibs;
	private static ArrayList<String> BuiltGoolClasses;

	
	
	/*
	 *  methods called by the input language recognizer to modify the nodes they constructs
	 */
	static public void init(Language inputLang, Platform outputLang){
		
		InputLang = inputLang;
		OutputLang = outputLang;
		EnabledGoolLibs = new ArrayList<String>();
		BuiltGoolClasses = new ArrayList<String>();
		for(String lib: getDefaultGoolLibs())
			EnabledGoolLibs.add(lib);
	}

	public static void matchImport(String InputLangImport){
		String GoolLib = getMatchedGoolLib(InputLangImport);
		if(GoolLib!=null)
			EnabledGoolLibs.add(GoolLib);
	}
	
	public static ClassDef matchClassNew(ClassNew ClassNew){
		String InputLangClass = ClassNew.getType().callGetCode();
		String GoolClass = getMatchedGoolClass(InputLangClass);
		ClassNew.setType(new TypeUnknown(GoolClass));
		if(!BuiltGoolClasses.contains(GoolClass)){
			ClassDef GoolClassAST = buildGoolClass(GoolClass);
			return GoolClassAST;
		}
		else
			return null;
	}
	
	public static ClassDef matchMethCall(MethCall MethCall){
		return null;
	}


	/*
	 *  methods used by the GoolMatcher to parse files and get the matching informations
	 */
	static private ArrayList<String> getDefaultGoolLibs(){
		ArrayList<String> res = new ArrayList<String>();
		try{
			InputStream ips= new FileInputStream(getPathOfInputImportMatchFile()); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){
				line=removeSpaces(line);
				if(!isInputMatchLine(line)){
					String GoolLib=getLeftPartOfInputMatchLine(line);
					String Import=getRightPartOfInputMatchLine(line);
					if(Import.equals("default"))
						res.add(GoolLib);
				}	
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		return res;
	}

	static private String getMatchedGoolLib(String InputLangImport){
		String res = null;
		try{
			InputStream ips= new FileInputStream(getPathOfInputImportMatchFile()); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){
				line=removeSpaces(line);
				if(!isInputMatchLine(line)){
					String GoolLib=getLeftPartOfInputMatchLine(line);
					String Import=getRightPartOfInputMatchLine(line);
					if(Import.equals(InputLangImport)){
						res = GoolLib;
						break;
					}
				}	
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		return res;
	}

	static private String getMatchedGoolClass(String InputLangClass){
		String res = null;
		boolean matchFound=false;
		for(String GoolLib: EnabledGoolLibs){
			try{
				InputStream ips= new FileInputStream(getPathOfInputClassMatchFile(GoolLib)); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String line;
				while ((line=br.readLine())!=null){
					line=removeSpaces(line);
					if(!isInputMatchLine(line)){
						String GoolClass=getLeftPartOfInputMatchLine(line);
						String Class=getRightPartOfInputMatchLine(line);
						if(Class.equals(InputLangClass)){
							res = GoolLib;
							matchFound=true;
							break;
						}
					}	
				}
				br.close(); 
				if(matchFound)
					break;
			}		
			catch (Exception e){
				System.out.println(e.toString());
			}
		}
		return res;
	}
	static private String getMatchedGoolMethod(String InputLangMethod){
		return "";
	}

	
	
	
	
	/*
	 *  methods used by the GoolMatcher to build a ClassDef from a GoolClass
	 */
	static private ClassDef buildGoolClass(String GoolClass){
		ClassDef GoolClassAST = new ClassDef(GoolClass);
		GoolClassAST.setIsEnum(false);
		GoolClassAST.setIsInterface(false);
		GoolClassAST.setPlatform(OutputLang);
		GoolClassAST.addModifier(Modifier.PUBLIC);
		
		
		return GoolClassAST;
	}
	
	
	
	
	
	
	


	/*
	 *  methods used by the GoolMatcher to parse each line of a match file
	 */
	static private String removeSpaces(String line){
		for(int i=0; i<line.length(); i++){
			if(line.charAt(i)==' ' || line.charAt(i)=='\t'){
				line=line.substring(0,i)+line.substring(i+1);
				i-=1;
			}
		}
		return line;
	}
	static private boolean isCommentLine(String line){
		return line.startsWith("#");
	}
	static private boolean isInputMatchLine(String line){
		return !isCommentLine(line) && line.contains("<-");
	}
	static private boolean isOutputMatchLine(String line){
		return !isCommentLine(line) && line.contains("->");
	}
	static private String getLeftPartOfInputMatchLine(String InputMatchLine){
		return InputMatchLine.substring(0, InputMatchLine.indexOf("<-"));
	}
	static private String getRightPartOfInputMatchLine(String InputMatchLine){
		return InputMatchLine.substring(InputMatchLine.indexOf("<-")+2);
	}
	static private String getLeftPartOfOutputMatchLine(String OutputMatchLine){
		return OutputMatchLine.substring(0, OutputMatchLine.indexOf("->"));
	}
	static private String getRightPartOfOutputMatchLine(String OutputMatchLine){
		return OutputMatchLine.substring(OutputMatchLine.indexOf("->")+2);
	}




	/*
	 *  methods used by the GoolMatcher to compute the path to match files
	 */
	static private String getPathOfInputMatchDir(String GoolLibName){
		return "src/gool/recognizer/" + langToString(InputLang).toLowerCase() + "/matching/" + GoolLibName + "/";
	}
	static private String getPathOfInputImportMatchFile(){
		return "src/gool/recognizer/" + langToString(InputLang).toLowerCase() + "/matching/ImportMatching.properties";
	}
	static private String getPathOfInputClassMatchFile(String GoolLibName){
		return getPathOfInputMatchDir(GoolLibName) + "ClassMatching.properties";
	}
	static private String getPathOfInputMethodMatchFile(String GoolLibName){
		return getPathOfInputMatchDir(GoolLibName) + "MethodMatching.properties";
	}
	static private String getPathOfOutputMatchDir(String GoolLibName){
		return "src/gool/generator/" + OutputLang.getName().toLowerCase() + "/matching/"+GoolLibName+"/";
	}
	static private String getPathOfOutputClassMatchFile(String GoolLibName){
		return getPathOfOutputMatchDir(GoolLibName) + "ClassMatching.properties";
	}
	static private String getPathOfOutputMethodMatchFile(String GoolLibName){
		return getPathOfOutputMatchDir(GoolLibName) + "MethodMatching.properties";
	}
	static private String getPathOfOutputDependencyFile(String GoolLibName){
		return getPathOfOutputMatchDir(GoolLibName) + "Dependencies.properties";
	}
	static private String getPathOfOutputCustomizedMethodFile(String GoolLibName, MethodSignature s){
		return getPathOfOutputMatchDir(GoolLibName) + "CustomizedMethods/" + s;
	}


	// translation of a Language to a String
	static private String langToString(Language lang){
		String res = "";
		switch(lang){
		case JAVA:
			res = "Java";
			break;
		case CPP:
			res = "Cpp";
			break;
		case CSHARP:
			res = "CSharp";
			break;
		case OBJC:
			res = "Objc";
			break;
		case PYTHON:
			res = "Python";
			break;
		case ANDROID:
			res = "Android";
			break;
		}
		return res;
	}

	// this class is used by the GoolMatcher to compare methods
	class MethodSignature{
		String classname;
		String methodname;
		ArrayList<String> paramtypes;
		String returntype;
		MethodSignature(String className, String methodName, ArrayList<String> paramtypes, String returntype){
			this.classname=className;
			this.methodname=methodName;
			this.paramtypes=paramtypes;
			this.returntype=returntype;
		}
		// a method signature can be created from a raw string such as: "methodName(int,char):int"
		MethodSignature(String RawSignature){
			String s=removeSpaces(RawSignature);
			String sname=s.substring(0, s.indexOf("("));

			System.out.println("Partie nom de classe et nom de méthode: "+sname);

			this.classname=sname.substring(0, sname.lastIndexOf("."));
			sname=sname.substring(sname.lastIndexOf("."));
			this.methodname=sname.substring(1);
			s=s.substring(s.indexOf("(")+1);
			System.out.println("Nom de classe: "+classname);
			System.out.println("Nom de methode: "+methodname);
			System.out.println("Partie paramètres et valeur de retour après la première parenthèse: "+s);
			paramtypes=new ArrayList<String>();
			for(int i=0; s.charAt(0)!=':'; i++){
				int ind1=s.indexOf(",");
				int ind2=s.indexOf(")");
				if(ind1!=-1){
					this.paramtypes.add(s.substring(0, ind1));
					s=s.substring(ind1+1);
				}
				else{
					this.paramtypes.add(s.substring(0, ind2));
					s=s.substring(ind2+1);
				}
			}
			s=s.substring(1);
			this.returntype=s;
		}
		// 2 signatures are compatible if they have exactly the same return type and the same parameter types
		public boolean isCompatibleWith(MethodSignature s){
			if(!this.returntype.equals(s.returntype))
				return false;
			if(this.paramtypes.size()!=s.paramtypes.size())
				return false;
			for(int i=0; i<this.paramtypes.size(); i++)
				if(!this.paramtypes.get(i).equals(s.paramtypes.get(i)))
					return false;
			return true;
		}
		// 2 signatures are equal if they have exactly the same class name, method name, parameter types and return type
		public boolean equals(MethodSignature s){
			if(!this.classname.equals(s.classname))
				return false;
			if(!this.methodname.equals(s.classname))
				return false;
			if(!this.isCompatibleWith(s))
				return false;
			return true;
		}
		public String toString(){
			String res = this.classname + "." + this.methodname + "(";
			for(String paramtype: this.paramtypes)
				res += paramtype + ",";
			if(!this.paramtypes.isEmpty())
				res = res.substring(0,res.length()-1);
			res += "):" + this.returntype;
			return res;
		}
	}
}