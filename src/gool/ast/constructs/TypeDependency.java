package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

public class TypeDependency extends Dependency {

	private IType type;

	public TypeDependency(IType type) {
		this.type = type;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public IType getType() {
		return type;
	}

}
