package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

/**
 * See Dependency for comments.
 */
public class CustomDependency extends Dependency {

	private String name;

	public CustomDependency(String name) {
		this.name = name;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public String getName() {
		return name;
	}

}
