package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.TypeString;

/**
 * Allows to compute the value of a boolean or integer operation with two operands.
 */
public class BinaryOperation extends Operation {

	/**
	 * The left operand.
	 */
	private Expression left;
	/**
	 * The right operand.
	 */
	private Expression right;

	/**
	 * Creates a new instance of BinaryOperator.
	 * 
	 * @param operator
	 *            the operator to be used to compute the operation.
	 * @param left
	 *            the left operand.
	 * @param right
	 *            the right operand.
	 */
	public BinaryOperation(Operator operator, Expression left, Expression right) {
		super(operator);
		/*
		 * If both expressions have different types and the '+' operator is
		 * used, we assume that it is a string concatenation. Thus, we force the
		 * type of the binary operation node to be a string.
		 */
		if (operator == Operator.PLUS
				&& (left.getType().equals(TypeString.INSTANCE) || right
						.getType().equals(TypeString.INSTANCE))) {
			setType(TypeString.INSTANCE);
		}
		this.left = left;
		this.right = right;
	}

	/**
	 * Gets the left operand.
	 * 
	 * @return the left operand expression.
	 */
	public Expression getLeft() {
		return left;
	}

	/**
	 * Gets the right operand.
	 * 
	 * @return the right operand expression.
	 */
	public Expression getRight() {
		return right;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
