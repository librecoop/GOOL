package gool.ast.gool;

import gool.GoolGeneratorController;
import gool.ast.GoolCall;
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
