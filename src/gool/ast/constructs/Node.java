package gool.ast.constructs;


import gool.ast.printer.GoolGeneratorController;


/**
 * The type of the nodes which allow us to represent the Abstract Syntax Tree of the 
 * intermediate language.
 */

public abstract class Node implements INode {

	@Override
	public String toString() {
		System.out.println();
		return GoolGeneratorController.generator().getCode(this.getClass().cast(this));
	}

}
