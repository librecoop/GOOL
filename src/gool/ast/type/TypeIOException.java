package gool.ast.type;

import gool.generator.GoolGeneratorController;



/**
 * This is the basic type Method of the intermediate language.
 */
public final class TypeIOException extends IType {
	public static final TypeIOException INSTANCE = new TypeIOException();
	private String textualtype;
	
	public String getTextualtype() {
		return textualtype;
	}

	public TypeIOException(String textualtype) {this.textualtype=textualtype;}

	public TypeIOException() {
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
