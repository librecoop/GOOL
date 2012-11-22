package gool.ast.type;

import gool.ast.printer.GoolGeneratorController;


/**
 * This is the basic type Bool of the intermediate language.
 */
public final class TypeBool extends PrimitiveType {
	
	public static final TypeBool INSTANCE = new TypeBool();

	private TypeBool() {}
	
	@Override
	public String callGetCode() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
