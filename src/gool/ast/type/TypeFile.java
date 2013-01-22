package gool.ast.type;

import gool.generator.GoolGeneratorController;

public class TypeFile extends ReferenceType {

	/**
	 * A static instance to avoid the creation of new objects.
	 */
	public static final TypeFile INSTANCE = new TypeFile();
	
	private TypeFile(){}

	@Override
	public String callGetCode() {
		return getName();
	}
	
	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

}