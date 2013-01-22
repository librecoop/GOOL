package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

public class MemberSelect extends VarAccess{
	private Expression target;
	
	public MemberSelect(Expression target, Dec var) {
		super(var);
		this.target = target;
	}

	public Expression getTarget() {
		return target;
	}
	
	public String getIdentifier() {
		return var.getName();
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
