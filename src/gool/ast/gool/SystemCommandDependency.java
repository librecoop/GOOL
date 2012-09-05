package gool.ast.gool;

import gool.GoolGeneratorController;
import gool.ast.Dependency;

public class SystemCommandDependency extends Dependency {

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
