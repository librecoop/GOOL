package gool.ast.type;

import gool.generator.GoolGeneratorController;



/**
 * This is the basic type Method of the intermediate language.
 */
public final class TypeFileReader extends IType {
	public static final TypeFileReader INSTANCE = new TypeFileReader();
	//private IType textualtype;
	
	//public IType getTextualtype() {
	//	return textualtype;
	//}

	//public TypeFileReader (IType textualtype) {this.textualtype=textualtype;}

	//public TypeFileReader() {
		// TODO Auto-generated constructor stub
//	}

	@Override
	public String getName() {
		return callGetCode();
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
}