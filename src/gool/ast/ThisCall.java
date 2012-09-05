package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.IType;

public class ThisCall extends InitCall {

	private ThisCall(IType type, Expression target) {
		super(type, target);
	}

	public ThisCall(IType type) {
		super(type, null);
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
