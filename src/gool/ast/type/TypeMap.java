package gool.ast.type;

import gool.ast.constructs.ClassDef;
import gool.generator.GoolGeneratorController;


/**
 * This is the basic type for classes defined in the intermediate language.
 */
public class TypeMap extends ReferenceType {

	/**
	 * The class where the list was defined.
	 */
	private ClassDef classDef;
	
	public TypeMap() {
	}

	protected TypeMap(IType keyType, IType elementType) {
		this();
		addArgument(keyType);
		addArgument(elementType);
	}
	

	public IType getElementType() {
		if (getTypeArguments().size() > 1) {
			return getTypeArguments().get(1);
		} 
		return TypeObject.INSTANCE;
	}
	
	public IType getKeyType() {
		if (getTypeArguments().size() > 1) {
			return getTypeArguments().get(0);
		} 
		return TypeObject.INSTANCE;
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
		return obj instanceof TypeMap && getName().equals(((IType)obj).getName());
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
