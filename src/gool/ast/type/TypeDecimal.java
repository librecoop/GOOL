package gool.ast.type;

import gool.GoolGeneratorController;

public final class TypeDecimal extends PrimitiveType {

	public static final TypeDecimal INSTANCE = new TypeDecimal();

	private TypeDecimal(){}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
