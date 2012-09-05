package gool.ast.list;

import gool.GoolGeneratorController;
import gool.ast.Expression;
import gool.ast.ListMethCall;
import gool.ast.type.TypeVoid;

public class ListGetCall extends ListMethCall {

	public ListGetCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
