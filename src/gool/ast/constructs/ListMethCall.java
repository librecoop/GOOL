package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;


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
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}
	
	public Expression getExpression() {
		return expression;
	}

}
