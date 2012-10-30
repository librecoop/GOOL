package gool.ast.map;

import gool.ast.Expression;
import gool.ast.MapEntryMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class MapEntryGetValueCall extends MapEntryMethCall {

	public MapEntryGetValueCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
