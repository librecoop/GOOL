package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;



public final class This extends Expression {

	public This(IType type) {
		super(type);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
