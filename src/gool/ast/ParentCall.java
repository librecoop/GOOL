package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.IType;

public class ParentCall extends InitCall {

	private ParentCall(IType type, Expression target) {
		super(type, target);
	}

	public ParentCall(IType type) {
		super(type, null);
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
