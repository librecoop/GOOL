package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.IType;



public final class This extends Expression {

	public This(IType type) {
		super(type);
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
