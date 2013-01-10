package gool.ast.type;

import gool.generator.GoolGeneratorController;


/**
 * This is the basic type Bool of the intermediate language.
 */
public final class TypeChar extends PrimitiveType {
	
	public static final TypeChar INSTANCE = new TypeChar();

	private TypeChar() {}
	
	@Override
	public String callGetCode() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
