package gool.ast.type;

import gool.generator.GoolGeneratorController;



public final class TypeFileWriter extends IType {
	public static final TypeFileWriter INSTANCE = new TypeFileWriter();
	
	public TypeFileWriter() {
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