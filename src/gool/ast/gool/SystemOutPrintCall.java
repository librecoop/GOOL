package gool.ast.gool;

import gool.GoolGeneratorController;
import gool.ast.GoolCall;
import gool.ast.type.TypeVoid;

public class SystemOutPrintCall extends GoolCall {

	public SystemOutPrintCall() {
		super(TypeVoid.INSTANCE);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
