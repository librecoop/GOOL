package gool.ast.type;

import gool.GoolGeneratorController;

/**
 * This is the basic type String of the intermediate language.
 */
public class TypeString extends PrimitiveType {

	public static final TypeString INSTANCE = new TypeString();

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
