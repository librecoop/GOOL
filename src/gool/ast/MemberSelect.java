package gool.ast;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;

public class MemberSelect extends Expression{
	private Expression target;
	private String identifier;
	
	public MemberSelect(IType goolType, Expression target, String identifier) {
		super(goolType);
		this.target = target;
		this.identifier = identifier;
	}

	public Expression getTarget() {
		return target;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
