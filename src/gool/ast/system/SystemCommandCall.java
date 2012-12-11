package gool.ast.system;

import gool.ast.constructs.GoolCall;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;

public class SystemCommandCall extends GoolCall {

	public SystemCommandCall() {
		super(TypeVoid.INSTANCE);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
