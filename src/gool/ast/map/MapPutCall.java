package gool.ast.map;

import gool.ast.Expression;
import gool.ast.MapMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class MapPutCall extends MapMethCall {

	public MapPutCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
