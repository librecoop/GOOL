package gool.ast.constructs;

import gool.ast.type.IType;
import gool.ast.type.TypeUnknown;
import gool.generator.GoolGeneratorController;

public class GoolMethodImplementation extends Meth {

	private String goolClass;
	private String methodSignature;
	
	public GoolMethodImplementation(IType returnType, Modifier modifier, String name, String goolClass, String methodSignature) {
		//TODO: set properly the return type of these methods
		//super(new TypeUnknown(methodSignature.substring(methodSignature.indexOf(":")+1)), Modifier.PUBLIC, methodSignature.substring(0, methodSignature.indexOf("(")));
		super(returnType, modifier, name);
		this.goolClass=goolClass;
		this.methodSignature=methodSignature;	
	}
	
	public String getGoolClass(){
		return this.goolClass;
	}
	public String getMethodSignature(){
		return this.methodSignature;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public String getHeader(){
		return GoolGeneratorController.generator().getHeader(this);
	}
	
}
