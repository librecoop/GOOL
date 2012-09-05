package gool.ast.gool;

import gool.GoolGeneratorController;
import gool.ast.Dependency;
import gool.ast.type.IType;

public class TypeDependency extends Dependency {

	private IType type;

	public TypeDependency(IType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public IType getType() {
		return type;
	}
}
