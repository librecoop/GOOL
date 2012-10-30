package gool.ast.gool;

import gool.ast.Dependency;
import gool.ast.printer.GoolGeneratorController;

public class CustomDependency extends Dependency {

	private String name;

	public CustomDependency(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String getFullName() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public String getName() {
		return name;
	}

}
