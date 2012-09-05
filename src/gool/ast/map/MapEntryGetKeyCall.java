package gool.ast.map;

import gool.GoolGeneratorController;
import gool.ast.Expression;
import gool.ast.MapEntryMethCall;
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
