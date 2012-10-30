package gool.ast.constructs;


public abstract class Operation extends Expression {

	private Operator operator;

	
	protected Operation(Operator operator) {
		super(operator.getReturnType());
		this.operator = operator;
	}

	public Operator getOperator() {
		return operator;
	}

}
