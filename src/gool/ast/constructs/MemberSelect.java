package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

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
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
