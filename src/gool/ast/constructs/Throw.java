package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

public class Throw extends Statement{
	/**
	 * The expression after throw.
	 */
	private Expression exp;
	
	public Throw (Expression expr){
		this.exp = expr;
	}
	
	public Expression getExpression() {
		return exp;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
