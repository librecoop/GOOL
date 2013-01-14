package gool.ast.type;

import gool.generator.GoolGeneratorController;

public class TypeArray extends ReferenceType {

	/**
	 * The type of the elements.
	 */
	private IType elementType;
	
	public TypeArray(IType elementType) {
		setElementType(elementType);
	}

	@Override
	public String getName() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public final void setElementType(IType elementType) {
		this.elementType = elementType;
	}

	public final IType getElementType() {
		return elementType;
	}
	
	@Override
	public String callGetCode() {
		return getName();
	}

}
