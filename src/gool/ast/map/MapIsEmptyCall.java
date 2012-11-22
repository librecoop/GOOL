package gool.ast.map;

import gool.ast.constructs.Expression;
import gool.ast.constructs.MapMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class MapIsEmptyCall extends MapMethCall {

	public MapIsEmptyCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
