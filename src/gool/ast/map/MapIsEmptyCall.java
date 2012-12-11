package gool.ast.map;

import gool.ast.constructs.Expression;
import gool.ast.constructs.MapMethCall;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;

public class MapIsEmptyCall extends MapMethCall {

	public MapIsEmptyCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
