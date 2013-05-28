package gool.matcher;

import com.sun.tools.javac.util.List;

import gool.ast.constructs.Language;
import gool.imports.java.util.ArrayList;

public class GoolMatcher{
	
	//contains PATHs to the GOOL libraries used for the current translation process
	private static ArrayList<String> GoolLibsUsed;
	
	GoolMatcher(){
		GoolLibsUsed = new ArrayList<String>();
	}
	
	
	public void clearGoolLibs(){
		this.GoolLibsUsed = new ArrayList<String>();
	}
	
	public void addDefaultLibs(Language inputLanguage){
		
		
	}
	
	
	// the following methods compute the path to GoolLibs files
	private String getMethodMatchFile(String GoolClassPath, Language lang){
		return GoolClassPath + "methodmatching/" + langToString(lang) + "MethodMatching.properties";
	}
	private String getClassMatchFile(String GoolClassPath){
		return GoolClassPath + "ClassMatching.properties";
	}
	private String getPackageMatchFile(String GoolClassPath){
		return GoolClassPath + "PackageMatching.properties";
	}
	private String getDefaultLibsPath(Language lang){
		return "src/matcher/" + langToString(lang) + "DefaultLibs.properties";
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
	
	// this class is used by the GoolMatcher to compare method signatures in between them
	class MethodSignature{
		String name;
		List<String> paramtypes;
		String returntype;
		MethodSignature(String name, List<String> paramtypes, String returntype){
			this.name=name;
			this.paramtypes=paramtypes;
			this.returntype=returntype;
		}
		// a method signature can be created from a raw string such as: "methodName(int,char):int"
		MethodSignature(String RawSignature){
			String s=removeSpaces(RawSignature);
			this.name=s.substring(0, s.indexOf("("));
			s=s.substring(s.indexOf("(")+1);
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
		boolean isCompatibleWith(MethodSignature s){
			if(!this.returntype.equals(s.returntype))
				return false;
			if(this.paramtypes.size()!=s.paramtypes.size())
				return false;
			for(int i=0; i<this.paramtypes.size(); i++)
				if(!this.paramtypes.get(i).equals(s.paramtypes.get(i)))
					return false;
			return true;
		}
	}
}