package gool.ast.gool;

import gool.GoolGeneratorController;
import gool.ast.Dependency;

public class SystemOutDependency extends Dependency {

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
