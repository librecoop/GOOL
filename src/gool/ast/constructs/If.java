package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;


/**
 * This captures the if statements of the intermediate language.
 * Hence it is an OOTStatement.
 * Notice the type checking achieved through generics.
 */
public class If extends Statement {

	/**
	 * The condition expression.
	 */
	private Expression condition;
	/**
	 * The statement that is evaluated when the condition is true.
	 */
	private Statement thenStatement;
	/**
	 * The statement that is evaluated when the condition is false.
	 */
	private Statement elseStatement;
	
	/**
	 * @param condition 
	 * @param statements
	 */
	public If(Expression condition, Statement thenStatement, Statement elseStatement){
		this.condition=condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}
	
	public Expression getCondition() {
		return condition;
	}
	
	public Statement getThenStatement() {
		return thenStatement;
	}
	
	public Statement getElseStatement() {
		return elseStatement;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
}
