package gool.ast.type;

import gool.generator.GoolGeneratorController;

public class TypeInputStream extends ReferenceType{

	@Override
	public String getName() {
		return this.callGetCode();
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
