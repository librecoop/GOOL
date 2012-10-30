package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;


public class MapMethCall extends Parameterizable{

	private Expression expression;

	protected MapMethCall( IType type) {
		super(type);
	}

	public MapMethCall(IType type, Expression expression) {
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
