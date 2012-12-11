package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

/**
 * Allows to compute the value of an unary operation. It may be used on boolean or
 * integer expressions.
 */
public class UnaryOperation extends Operation {

	private Expression expression;

	public UnaryOperation(Operator operator, Expression expr, IType type, String symbol) {
		super(operator,type,symbol);
		this.expression = expr;
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
