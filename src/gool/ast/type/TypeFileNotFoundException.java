package gool.ast.type;

import gool.generator.GoolGeneratorController;



public final class TypeFileNotFoundException extends IType {
	public static final TypeFileNotFoundException INSTANCE = new TypeFileNotFoundException();
	
	public TypeFileNotFoundException() {
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