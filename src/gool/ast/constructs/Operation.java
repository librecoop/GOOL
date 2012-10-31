package gool.ast.constructs;

import gool.ast.type.IType;


public abstract class Operation extends Expression {

	private Operator operator;
	private String textualoperator;

	protected Operation(Operator operator, IType type, String textualoperator) {
		super(type);
		this.operator = operator;
		this.textualoperator = textualoperator;
	}

	public Operator getOperator() {
		return operator;
	}

	public String getTextualoperator() {
		return textualoperator;
	}

}
