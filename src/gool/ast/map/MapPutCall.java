package gool.ast.map;

import gool.ast.constructs.Expression;
import gool.ast.constructs.MapMethCall;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;

public class MapPutCall extends MapMethCall {

	public MapPutCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
