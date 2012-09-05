package gool.ast;

import gool.GoolGeneratorController;


/**
 * This is the basic type Bool of the intermediate language.
 */
public class Return extends Statement {

	/**
	 * The expression to be returned.
	 */
	private Expression expression;
	
	public Return (Expression exp){
		this.expression = exp;
	}
	
	/**
	 * Gets the expression to be returned.
	 * @return the expression to be returned.
	 */
	public Expression getExpression() {
		return expression;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
