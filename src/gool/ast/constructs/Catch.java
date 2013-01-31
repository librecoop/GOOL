package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

public class Catch extends Statement{
	/**
	 * The condition expression.
	 */
	private Expression dec;
	/**
	 * The statement in the bloc catch.
	 */
	private Statement blocStatement;
	
	public Catch(Expression condition, Statement statement){
		this.dec=condition;
		this.blocStatement = statement;
	}
	
	public Expression getCondition() {
		return dec;
	}
	
	public Statement getStatement() {
		return blocStatement;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
