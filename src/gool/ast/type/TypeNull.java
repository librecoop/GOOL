package gool.ast.type;

import gool.ast.printer.GoolGeneratorController;

public class TypeNull extends PrimitiveType {

	public static final IType INSTANCE = new TypeNull();

	@Override
	public String callGetCode() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
