package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

public class Throw extends Statement {
	/**
	 * Throw expression.
	 */
	private Expression expression;
	
	/**
	 * 
	 * @param expression
	 */
	public Throw(Expression expression) {
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
