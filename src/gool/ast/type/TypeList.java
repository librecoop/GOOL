package gool.ast.type;

import gool.ast.constructs.ClassDef;
import gool.ast.printer.GoolGeneratorController;


/**
 * This is the basic type for classes defined in the intermediate language.
 */
public class TypeList extends ReferenceType {
	
	/**
	 * The class where the list was defined.
	 */
	private ClassDef classDef;
	
	public TypeList() {
	}

	public TypeList(IType elementType) {
		this();
		addArgument(elementType);
	}
	

	public IType getElementType() {
		if (getTypeArguments().size() > 0) {
			return getTypeArguments().get(0);
		}
		return null;
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
		return obj instanceof TypeList && getName().equals(((IType)obj).getName());
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
