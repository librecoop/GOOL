package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

public class CompoundAssign extends Assign {

	private Operator operator;
	private String textualoperator;
	private IType type;
		

	public CompoundAssign(Node var, Expression value, Operator operator, String textualoperator, IType type) {
		super(var, value);
		this.operator = operator;
		this.textualoperator = textualoperator;
		this.type = type;
	}

	public Operator getOperator() {
		return operator;
	}

	public String getTextualoperator() {
		return textualoperator;
	}

	public IType getType() {
		return type;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
