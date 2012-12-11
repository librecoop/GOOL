package gool.ast.constructs;

import gool.ast.type.TypeBool;
import gool.generator.GoolGeneratorController;

public class EqualsCall extends Parameterizable {

	private Expression target;

	public EqualsCall(Expression target) {
		super(TypeBool.INSTANCE);
		this.target = target;
	}

	public Expression getTarget() {
		return target;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
