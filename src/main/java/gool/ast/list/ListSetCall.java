package gool.ast.list;

import gool.ast.core.Expression;
import gool.ast.core.ListMethCall;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;

/**
 * This class captures the invocation of the set method on a list.
 */
public class ListSetCall extends ListMethCall {

	/**
	 * The constructor of a "list set call" representation.
	 * @param target
	 * 		: The target expression used in the call.
	 */
	public ListSetCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
