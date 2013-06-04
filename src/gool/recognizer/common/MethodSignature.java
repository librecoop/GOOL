package gool.recognizer.common;

import gool.ast.constructs.Expression;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.MethCall;
import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;
import gool.generator.java.JavaGenerator;
import gool.imports.java.util.ArrayList;



// this class is used by the GoolMatcher to compare methods
public class MethodSignature{
	String methodname;
	ArrayList<String> paramtypes;
	String returntype;
	ArrayList<IType> goolparamtypes;
	IType goolreturntype;
	MethodSignature(String className, String methodName, ArrayList<String> paramtypes, String returntype){
		this.methodname=methodName;
		this.paramtypes=paramtypes;
		this.returntype=returntype;
	}
	// a method signature can be created from a raw string such as: "methodName(int,char):int"
	MethodSignature(String RawSignature){
		String s=RawSignature;

		this.methodname=s.substring(0, s.indexOf("("));
		s=s.substring(s.indexOf("(")+1);
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
	// 
	MethodSignature(MethCall MethCall){
		methodname=((MemberSelect)MethCall.getTarget()).getIdentifier();
		
		paramtypes=new ArrayList<String>();
		goolparamtypes=new ArrayList<IType>();
		CodeGenerator CurrentGen = GoolGeneratorController.generator();
		GoolGeneratorController.setCodeGenerator(new JavaGenerator());
		for(Expression e: MethCall.getParameters()){
			paramtypes.add(e.getType().getName());
			goolparamtypes.add(e.getType());
		}
		returntype=MethCall.getType().getName();
		goolreturntype=MethCall.getType();
		GoolGeneratorController.setCodeGenerator(CurrentGen);
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
		if(!this.methodname.equals(s.methodname))
			return false;
		if(!this.isCompatibleWith(s))
			return false;
		return true;
	}
	public String toString(){
		String res = this.methodname + "(";
		for(String paramtype: this.paramtypes)
			res += paramtype + ",";
		if(!this.paramtypes.isEmpty())
			res = res.substring(0,res.length()-1);
		res += "):" + this.returntype;
		return res;
	}
	public String getMethodname() {
		return methodname;
	}
	public ArrayList<String> getParamtypes() {
		return paramtypes;
	}
	public String getReturntype() {
		return returntype;
	}
	public ArrayList<IType> getGoolparamtypes() {
		return goolparamtypes;
	}
	public IType getGoolreturntype() {
		return goolreturntype;
	}
	
}
