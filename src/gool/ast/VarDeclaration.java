package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.IType;

/**
 * This class captures a variable declaration in the intermediate language.
 * Hence it inherits of Dec.
 */
public class VarDeclaration extends Dec {
	private Expression initialValue;

	public VarDeclaration(Dec var) {
		this(var.getType(), var.getName());
	}

	public VarDeclaration(IType type, String name) {
		super(type, name);
	}

	public void setInitialValue(Expression initialValue) {
		this.initialValue = initialValue;
	}

	public Expression getInitialValue() {
		return initialValue;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
