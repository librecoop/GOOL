package gool.ast.constructs;

import gool.ast.type.TypeString;
import gool.generator.GoolGeneratorController;

public class ToStringCall extends MethCall {

	public ToStringCall(Expression target) {
		super(TypeString.INSTANCE, null, target);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
