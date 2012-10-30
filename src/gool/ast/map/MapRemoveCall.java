package gool.ast.map;

import gool.ast.Expression;
import gool.ast.MapMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class MapRemoveCall extends MapMethCall {

	public MapRemoveCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
