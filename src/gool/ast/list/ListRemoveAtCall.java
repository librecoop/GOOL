package gool.ast.list;

import gool.ast.constructs.Expression;
import gool.ast.constructs.ListMethCall;
import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeVoid;

public class ListRemoveAtCall extends ListMethCall {

	public ListRemoveAtCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
