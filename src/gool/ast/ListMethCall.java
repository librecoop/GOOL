package gool.ast;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;


public class ListMethCall extends Parameterizable {
	private Expression expression;

	public ListMethCall( IType type) {
		super(type);
	}


	public ListMethCall(IType type, Expression expression) {
		super(type);
		this.expression = expression;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}
	
	public Expression getExpression() {
		return expression;
	}

}
