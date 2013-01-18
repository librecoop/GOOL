package gool.ast.type;

import gool.generator.GoolGeneratorController;



public final class TypeBufferedWriter extends IType {
	public static final TypeBufferedWriter INSTANCE = new TypeBufferedWriter();
	
	//public TypeFileWriter() {
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