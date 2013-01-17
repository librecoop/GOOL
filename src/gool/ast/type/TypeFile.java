package gool.ast.type;

import gool.generator.GoolGeneratorController;



/**
 * This is the basic type Method of the intermediate language.
 */
public final class TypeFile extends IType {
	public static final TypeFile INSTANCE = new TypeFile();
	private String textualtype;
	
	public String getTextualtype() {
		return textualtype;
	}

	public TypeFile(String textualtype) {this.textualtype=textualtype;}

	public TypeFile() {
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
