package gool.ast.constructs;

import gool.ast.type.TypeNone;
import gool.generator.GoolGeneratorController;

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
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public Expression getIndex() {
		return index;
	}
}
