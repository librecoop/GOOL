package gool.generator.common;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Field;
import gool.ast.constructs.Language;
import gool.ast.constructs.Meth;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.VarDeclaration;
import gool.ast.type.IType;
import gool.ast.type.TypeUnknown;
import gool.recognizer.common.MethodSignature;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeneratorMatcher {
	
	private static ArrayList<String> EnabledGoolLibs;
	private static Platform OutputLang;
	private static HashMap<String, MethodSignature> RecognizedGoolMethods;
	
	static public void init(Platform outputLang){
		OutputLang = outputLang;
		EnabledGoolLibs = new ArrayList<String>();
		RecognizedGoolMethods = new HashMap<String, MethodSignature>();
	}
	
	
	
	
	public static String matchGoolType(String GoolClass){
		return getMatchedOutputClass(GoolClass);
	}
	
	

	static private String getMatchedOutputClass(String GoolClass){
		String res = null;
		boolean matchFound=false;
		for(String GoolLib: EnabledGoolLibs){
			try{
				InputStream ips= new FileInputStream(getPathOfOutputClassMatchFile(GoolLib)); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String line;
				while ((line=br.readLine())!=null){
					line=removeSpaces(line);
					if(isOutputMatchLine(line)){
						String GoolClassName=getLeftPartOfOutputMatchLine(line);
						String OutputClassName=getRightPartOfOutputMatchLine(line);
						//TODO: this has to be improved
						if(GoolClassName.equals(GoolClass)){
							res = OutputClassName;
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

	static private String getMatchedOutputMethodName(String GoolMethod){
		String res = null;
		boolean matchFound=false;
		for(String GoolLib: EnabledGoolLibs){
			try{
				InputStream ips= new FileInputStream(getPathOfOutputMethodMatchFile(GoolLib)); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String line;
				while ((line=br.readLine())!=null){
					line=removeSpaces(line);
					if(isOutputMatchLine(line)){
						String GoolFullMethodName=getLeftPartOfOutputMatchLine(line);
						String OutputMethodName=getRightPartOfOutputMatchLine(line);
						if(GoolFullMethodName.equals(GoolMethod)){
							res = OutputMethodName;
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
	static private String getPathOfOutputCustomizedMethodFile(String GoolLibName, String GoolMethodName){
		return getPathOfOutputMatchDir(GoolLibName) + "CustomizedMethods/" + GoolMethodName;
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

		String OutputClassName = getMatchedOutputClass(GoolClass);
		GoolClassAST.addField(new Field(Modifier.PRIVATE, GoolClass.toLowerCase()+OutputLang.getName().toLowerCase(), new TypeUnknown(OutputClassName)));
		GoolClassAST.createDefaultConstructor();

		//Set<String> RecognizedMethods = RecognizedGoolMethods.keySet();

		for(String GoolMethodName : getAllGoolMethodNames(GoolClass)){
			String OutputMethodName = getMatchedOutputMethodName(GoolMethodName);
			if(!OutputMethodName.equals("CUSTOMIZEDMETHOD")){
			MethodSignature MethSign = RecognizedGoolMethods.get(GoolMethodName);
			Meth GoolMethod = new Meth(MethSign.getGoolreturntype(), Modifier.PUBLIC, OutputMethodName);
			GoolMethod.setClassDef(GoolClassAST);
			for(int i=0; i<MethSign.getGoolparamtypes().size(); i++){
				GoolMethod.addParameter(new VarDeclaration(MethSign.getGoolparamtypes().get(i), "param"+i));
			}
			//construction du corps de la methode
			//if(GoolMethod.getType() instanceof TypeVoid)
				//GoolMethod.addStatement(new MethodCall());
			
			
			GoolClassAST.addMethod(GoolMethod);
			}
		}

		return GoolClassAST;
	}
	

	static private ArrayList<String> getAllGoolMethodNames(String GoolClass){
		ArrayList<String> res = new ArrayList<String>();
		boolean matchFound=false;
		for(String GoolLib: EnabledGoolLibs){
			try{
				InputStream ips= new FileInputStream(getPathOfOutputMethodMatchFile(GoolLib)); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String line;
				while ((line=br.readLine())!=null){
					line=removeSpaces(line);
					if(isOutputMatchLine(line)){
						String GoolFullMethodName=getLeftPartOfOutputMatchLine(line);
						String GoolClassName=GoolFullMethodName.substring(0, GoolFullMethodName.lastIndexOf("."));
						if(GoolClassName.equals(GoolClass)){
							res.add(GoolFullMethodName);
							matchFound=true;
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

	
}
