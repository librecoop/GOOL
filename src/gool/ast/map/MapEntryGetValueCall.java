package gool.ast.map;

import gool.ast.constructs.Expression;
import gool.ast.constructs.MapEntryMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class MapEntryGetValueCall extends MapEntryMethCall {

	public MapEntryGetValueCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
