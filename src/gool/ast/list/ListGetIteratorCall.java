package gool.ast.list;

import gool.ast.Expression;
import gool.ast.ListMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class ListGetIteratorCall extends ListMethCall {

	public ListGetIteratorCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
