package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

public class Try extends Statement{
	/**
	 * The statement in the bloc finally.
	 */
	private Statement blocStatement;
	
	public Try(Statement statement){
		this.blocStatement = statement;
	}
	
	public Statement getStatement() {
		return blocStatement;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
