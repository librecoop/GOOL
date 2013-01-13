package gool.ast.type;

import gool.generator.GoolGeneratorController;

/**
 * This is the basic type String of the intermediate language.
 */
public class TypeChar extends PrimitiveType {

	public static final TypeChar INSTANCE = new TypeChar();

	@Override
	public String callGetCode() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}
}