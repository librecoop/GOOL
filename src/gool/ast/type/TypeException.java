package gool.ast.type;

import gool.generator.GoolGeneratorController;



/**
 * This is the basic type Method of the intermediate language.
 */
public final class TypeException extends IType {
	public static final TypeException INSTANCE = new TypeException();
	private String textualtype;
	
	public String getTextualtype() {
		return textualtype;
	}

	public TypeException(String textualtype) {this.textualtype=textualtype;}

	public TypeException() {
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
