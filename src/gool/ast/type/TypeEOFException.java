package gool.ast.type;

import gool.generator.GoolGeneratorController;



public final class TypeEOFException extends IType {
	public static final TypeEOFException INSTANCE = new TypeEOFException();
	
	public TypeEOFException() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return callGetCode();
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
}