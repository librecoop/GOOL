package gool.ast.type;

import gool.generator.GoolGeneratorController;



/**
 * This is the basic type Method of the intermediate language.
 */
public final class TypeBufferedReader extends IType {
	public static final TypeBufferedReader INSTANCE = new TypeBufferedReader();
	public TypeBufferedReader(IType elementType) {
		this();
		addArgument(elementType);
	}
	public TypeBufferedReader() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return callGetCode();
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
}
