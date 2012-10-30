package gool.ast.system;

import gool.ast.constructs.GoolCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class SystemCommandCall extends GoolCall {

	public SystemCommandCall() {
		super(TypeVoid.INSTANCE);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
