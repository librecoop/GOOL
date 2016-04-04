/**
 * 
 */
package gool.ast.list;

import gool.ast.core.Expression;
import gool.ast.core.ListMethCall;
import gool.ast.type.IType;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 * This class captures the invocation of the indexOf method on a list.
 */
public class ListIndexOfCall extends ListMethCall {

	/**
	 * The constructor of a "list indexOf call" representation.
	 * @param target
	 * 		: The target expression used in the call.
	 */
	public ListIndexOfCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}

	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName();
		}
		return cg.getCode(this);
	}

}
