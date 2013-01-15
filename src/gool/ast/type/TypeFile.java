package gool.ast.type;

import gool.ast.constructs.ClassDef;
import gool.generator.GoolGeneratorController;


/**
 * This is the basic type for classes defined in the intermediate language.
 */
public class TypeFile extends ReferenceType {

	/**
	 * The class where the list was defined.
	 */
	private ClassDef classDef;
	
	public TypeFile() {
	}

	public TypeFile(IType keyType, IType elementType) {
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
	public String callGetCode() {
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
		return this.callGetCode();
	}

}
