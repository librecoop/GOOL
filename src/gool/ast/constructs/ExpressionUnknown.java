package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;

/**
 * This interface accounts for all statements of the intermediate language.
 */
public class ExpressionUnknown extends Expression {

	String textual;

	public String getTextual() {
		return textual;
	}

	public ExpressionUnknown(IType type, String textual) {
		super(type);
		this.textual = textual;
	}

}
