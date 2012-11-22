package gool.ast.system;

import gool.ast.constructs.GoolCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class SystemOutPrintCall extends GoolCall {

	public SystemOutPrintCall() {
		super(TypeVoid.INSTANCE);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
