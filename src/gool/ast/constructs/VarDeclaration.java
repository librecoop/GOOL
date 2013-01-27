package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

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
	public boolean updateFrom(Dec source) {
		if (! super.updateFrom(source))
			return false;
		this.initialValue = ((VarDeclaration)source).getInitialValue();
		return true;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
