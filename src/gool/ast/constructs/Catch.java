package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

public class Catch extends Statement {
	/**
	 * Catch parameter
	 */
	private VarDeclaration parameter;
	
	/**
	 * Catch instruction block
	 */
	private Block block;

	public Catch(VarDeclaration parameter, Block block) {
		this.parameter = parameter;
		this.block = block;
	}

	public VarDeclaration getParameter() {
		return parameter;
	}

	public Block getBlock() {
		return block;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
