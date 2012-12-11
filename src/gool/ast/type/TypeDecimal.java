package gool.ast.type;

import gool.generator.GoolGeneratorController;

public final class TypeDecimal extends PrimitiveType {

	public static final TypeDecimal INSTANCE = new TypeDecimal();

	private TypeDecimal(){}
	
	@Override
	public String callGetCode() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
