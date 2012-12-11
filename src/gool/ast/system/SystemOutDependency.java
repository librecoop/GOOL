package gool.ast.system;

import gool.ast.constructs.Dependency;
import gool.generator.GoolGeneratorController;

public class SystemOutDependency extends Dependency {

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
