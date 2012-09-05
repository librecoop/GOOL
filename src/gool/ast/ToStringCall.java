package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.TypeString;

public class ToStringCall extends MethCall {

	public ToStringCall(Expression target) {
		super(TypeString.INSTANCE, target);
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
