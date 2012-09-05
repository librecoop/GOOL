package gool.ast.type;

import gool.GoolGeneratorController;


/**
 * This is the basic type Bool of the intermediate language.
 */
public final class TypeByte extends PrimitiveType {
	
	public static final TypeByte INSTANCE = new TypeByte();

	private TypeByte() {}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
