package gool.ast.type;

import gool.ast.ClassDef;
import gool.ast.printer.GoolGeneratorController;


/**
 * This is the basic type for classes defined in the intermediate language.
 */
public class TypeEntry extends ReferenceType {
	
	/**
	 * The class where the list was defined.
	 */
	private ClassDef classDef;
	
	public TypeEntry() {
	}

	public TypeEntry(IType keyType, IType elementType) {
		this();
		addArgument(keyType);
		addArgument(elementType);
	}
	

	public IType getElementType() {
		if (getTypeArguments().size() > 1) {
			return getTypeArguments().get(1);
		} 
		return null;
	}
	
	public IType getKeyType() {
		return getTypeArguments().get(0);
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public ClassDef getClassDef() {
		return classDef;
	}

	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeEntry && getName().equals(((IType)obj).getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String getName() {
		return this.toString();
	}

}