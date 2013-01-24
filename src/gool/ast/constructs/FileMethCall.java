package gool.ast.constructs;

import gool.ast.type.IType;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;


public class FileMethCall extends Parameterizable {
	private Expression expression;

	public FileMethCall( IType type) {
		super(type);
	}


	public FileMethCall(IType type, Expression expression) {
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
