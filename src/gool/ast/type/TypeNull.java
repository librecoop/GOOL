package gool.ast.type;

import gool.GoolGeneratorController;

public class TypeNull extends PrimitiveType {

	public static final IType INSTANCE = new TypeNull();

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
