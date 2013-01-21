package gool.ast.constructs;

import gool.ast.type.IType;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;


public class BufferedReaderMethCall extends Parameterizable {
	private Expression expression;

	public BufferedReaderMethCall( IType type) {
		super(type);
	}


	public BufferedReaderMethCall(IType type, Expression expression) {
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
