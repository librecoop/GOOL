package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

public class ThisCall extends InitCall {

	private ThisCall(IType type, Expression target) {
		super(type, target);
	}

	public ThisCall(IType type) {
		super(type, null);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
