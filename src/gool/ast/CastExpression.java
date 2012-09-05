package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.IType;


/**
 * Represents the conversion of an expression to a target type. In object
 * oriented languages, this construct represents an explicit cast.
 * 
 * @param <T> the target type.
 */
public final class CastExpression extends Expression {

	/**
	 * The expression to be casted.
	 */
	private Expression expression;

	/**
	 * Creates a new cast using the target type and the expression to be casted.
	 * @param targetType the target type.
	 * @param expression the expression to be casted.
	 */
	public CastExpression(IType targetType, Expression expression) {
		super(targetType);
		this.expression = expression;
	}

	/**
	 * Gets the expression to be casted.
	 * @return the expression to be casted.
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * Generates the code in the target language.
	 */
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
