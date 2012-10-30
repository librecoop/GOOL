package gool.ast.map;

import gool.ast.constructs.Expression;
import gool.ast.constructs.MapEntryMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class MapEntryGetKeyCall extends MapEntryMethCall {

	public MapEntryGetKeyCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
