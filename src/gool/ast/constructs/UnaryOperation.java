package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;

/**
 * Allows to compute the value of an unary operation. It may be used on boolean or
 * integer expressions.
 */
public class UnaryOperation extends Operation {

	private Expression expression;

	public UnaryOperation(Operator operator, Expression expr) {
		super(operator);
		this.expression = expr;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
