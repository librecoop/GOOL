package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

/**
 * A while statement.
 */
public class While extends Statement{

	/**
	 * The condition expression.
	 */
	private Expression condition;
	/**
	 * The statement that is evaluated when the condition is true.
	 */
	private Statement whileStatement;
	
	/**
	 * @param condition 
	 * @param statements
	 */
	public While(Expression condition, Statement whileStatement){
		this.condition=condition;
		this.whileStatement = whileStatement;
	}
	
	public Expression getCondition() {
		return condition;
	}
	
	public Statement getWhileStatement() {
		return whileStatement;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
