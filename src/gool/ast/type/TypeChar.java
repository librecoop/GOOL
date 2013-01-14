package gool.ast.type;

import gool.generator.GoolGeneratorController;

public class TypeChar extends PrimitiveType{
	
	public static final TypeChar INSTANCE = new TypeChar();

	@Override
	public String callGetCode(){
		// TODO Auto-generated method stub
		return getName();
	}

	@Override
	public String getName()  {
		// TODO Auto-generated method stub
		return GoolGeneratorController.generator().getCode(this);
	}

}
