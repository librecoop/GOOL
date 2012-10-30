package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeNone;

/**
 * 
 */
public class ArrayAccess extends Expression{
	private Expression expression;
	private Expression index;

	public ArrayAccess(Expression expr, Expression index) {
		super(TypeNone.INSTANCE);
		this.expression = expr;
		this.index = index;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public Expression getIndex() {
		return index;
	}
}
