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

import gool.ast.constructs.Language;
import gool.imports.java.util.ArrayList;

public class GoolMatcher{
	
	//contains PATHs to the GOOL libraries enabled for the current translation process
	private static ArrayList<String> GoolLibsEnabled;
	
	//contains PATHs to the GOOL libraries used during the current translation process
	private static ArrayList<String> GoolLibsUsed;
	
	GoolMatcher(){
		GoolLibsEnabled = new ArrayList<String>();
		GoolLibsUsed = new ArrayList<String>();
	}
	
	
	public void clearGoolLibs(){
		this.GoolLibsUsed = new ArrayList<String>();
	}
	
	public void addDefaultLibs(Language inputLanguage){
		
		
	}
	
	
	// the following methods compute the path to match files
	private String getPathOfInputMatchDir(String GoolLibName, Language inputLang){
		return "src/gool/recognizer/" + langToString(inputLang).toLowerCase() + "/matching/"+GoolLibName+"/";
	}
	private String getPathOfInputPackageMatchFile(Language inputLang){
		return "src/gool/recognizer/" + langToString(inputLang).toLowerCase() + "/matching/"
	+ langToString(inputLang) + "PackageMatching.properties";
	}
	private String getPathOfInputClassMatchFile(String GoolLibName, Language inputLang){
		return getPathOfInputMatchDir(GoolLibName, inputLang) + langToString(inputLang) + "ClassMatching.properties";
	}
	private String getPathOfInputMethodMatchFile(String GoolLibName, Language inputLang){
		return getPathOfInputMatchDir(GoolLibName, inputLang) + langToString(inputLang) + "MethodMatching.properties";
	}
	
	private String getPathOfOutputMatchPath(String GoolLibName, Language outputLang){
		return "src/gool/generator/" + langToString(outputLang).toLowerCase() + "/matching/"+GoolLibName+"/";
	}
	private String getPathOfOutputClassMatchFile(String GoolLibName, Language outputLang){
		return getPathOfOutputMatchPath(GoolLibName, outputLang) + langToString(outputLang) + "ClassMatching.properties";
	}
	private String getPathOfOutputMethodMatchFile(String GoolLibName, Language outputLang){
		return getPathOfOutputMatchPath(GoolLibName, outputLang) + langToString(outputLang) + "MethodMatching.properties";
	}
	private String getPathOfOutputDependencyFile(String GoolLibName, Language outputLang){
		return getPathOfOutputMatchPath(GoolLibName, outputLang) + langToString(outputLang) + "Dependencies.properties";
	}
	private String getPathOfOutputCustomizedMethodFile(String GoolLibName, Language outputLang, MethodSignature s){
		return getPathOfOutputMatchPath(GoolLibName, outputLang) + "CustomizedMethods/" + s;
	}
	
	
	// translation of a Language to a String
	private String langToString(Language lang){
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
	
	// removing spaces and tabulations from a String
	private String removeSpaces(String s){
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i)==' ' || s.charAt(i)=='\t'){
				s=s.substring(0,i)+s.substring(i+1);
				i-=1;
			}
		}
		return s;
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