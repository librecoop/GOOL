package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeString;

public class ToStringCall extends MethCall {

	public ToStringCall(Expression target) {
		super(TypeString.INSTANCE, target);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
