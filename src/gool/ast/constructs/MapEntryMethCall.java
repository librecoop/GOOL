package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;


public class MapEntryMethCall extends Parameterizable{

	private Expression expression;


	protected MapEntryMethCall( IType type) {
		super(type);
	}

	public MapEntryMethCall(IType type, Expression expression) {
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
